package red.tetracube.iotsense.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.iotsense.config.IoTSenseConfig;
import red.tetracube.iotsense.dto.DeviceTelemetryData;
import red.tetracube.iotsense.enumerations.DeviceType;
import red.tetracube.iotsense.services.TelemetryServices;

import java.util.*;

@ApplicationScoped
public class BrokerClient {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    TelemetryServices telemetryServices;

    private final Mqtt5AsyncClient client;
    private final BroadcastProcessor<Map.Entry<DeviceType, String>> deviceTelemetryIdStream = BroadcastProcessor.create();

    private final static Logger LOGGER = LoggerFactory.getLogger(BrokerClient.class);

    public BrokerClient(IoTSenseConfig ioTSenseConfig) {
        client = MqttClient.builder()
                .identifier(ioTSenseConfig.mqtt().clientName() + "-" + UUID.randomUUID())
                .automaticReconnectWithDefaultConfig()
                .automaticReconnect()
                .applyAutomaticReconnect()
                .serverHost(ioTSenseConfig.mqtt().address())
                .useMqttVersion5()
                .build()
                .toAsync();
    }

    void startup(@Observes StartupEvent event) {
        Uni.createFrom()
                .completionStage(
                        client.connect()
                )
                .call(mqtt5ConnAck -> {
                    if (mqtt5ConnAck.getReasonCode() == Mqtt5ConnAckReasonCode.SUCCESS) {
                        return listenDevicesEvents();
                    }
                    return Uni.createFrom().voidItem();
                })
                .subscribe()
                .with(connectAck -> {
                    LOGGER.info("MQTT connection result code {}", connectAck.getReasonCode());
                });
    }

    public Multi<DeviceTelemetryData> getDeviceTelemetryIdStream() {
        return deviceTelemetryIdStream
                .invoke(telemetryEntry -> LOGGER.info("Arrived from device device {}", telemetryEntry.getValue()))
                .<Optional<DeviceTelemetryData>>map(telemetryEntry -> {
                    try {
                        return Optional.ofNullable(
                                telemetryServices.getLatestD    eviceTelemetry(telemetryEntry.getKey(), telemetryEntry.getValue())
                        );
                    } catch (Exception e) {
                        LOGGER.error("Cannot retrieve telemetry due error: ", e);
                        return Optional.empty();
                    }
                })
                .filter(optionalTelemetry -> {
                    if (optionalTelemetry.isEmpty()) {
                        LOGGER.warn("Incoming device telemetry is null");
                    }
                    return optionalTelemetry.isPresent();
                })
                .map(Optional::get);
    }

    private Uni<Mqtt5SubAck> listenDevicesEvents() {
        var telemetrySubscription = client.subscribeWith()
                .topicFilter("devices/telemetry/#")
                .qos(MqttQos.AT_LEAST_ONCE)
                .callback(mqtt5Publish -> {
                    try {
                        var topicLevels = mqtt5Publish.getTopic().getLevels();
                        var deviceType = DeviceType.valueOf(topicLevels.getLast());
                        var deviceInternalName = new String(mqtt5Publish.getPayloadAsBytes());
                        deviceTelemetryIdStream.onNext(
                                new AbstractMap.SimpleEntry<>(
                                        deviceType,
                                        deviceInternalName
                                )
                        );
                    } catch (Exception e) {
                        LOGGER.error("Invalid telemetry entry due error:", e);
                    }
                })
                .send();
        return Uni.createFrom().completionStage(telemetrySubscription)
                .invoke(mqtt5SubAck -> {
                    LOGGER.info("Telemetry subscription completed with status {}", mqtt5SubAck.getReasonCodes());
                });
    }

    private void publishMessage(String topic, Object message) throws JsonProcessingException {
        var serializedMessage = objectMapper.writeValueAsBytes(message);
        var publishPublisher = this.client
                .publishWith()
                .topic(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .payload(serializedMessage)
                .send();
        Uni.createFrom().completionStage(publishPublisher)
                .subscribe()
                .with(publishResult -> {
                    if (publishResult.getError().isPresent()) {
                        LOGGER.error("Publish finished with error {}", publishResult.getError());
                        return;
                    }
                    LOGGER.info("Publish completes successfully");
                });
    }

}
