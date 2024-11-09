package red.tetracube.iotsense.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class BrokerClient {

    @Inject
    ObjectMapper objectMapper;

    private final Mqtt5AsyncClient client;

    private final static Logger LOGGER = LoggerFactory.getLogger(BrokerClient.class);

    public BrokerClient(
            @ConfigProperty(name = "iot-sense.mqtt.client-name") String mqttClientName,
            @ConfigProperty(name = "iot-sense.mqtt.address") String mqttAddress
    ) {
        client = MqttClient.builder()
                .identifier(mqttClientName + "-" + UUID.randomUUID())
                .automaticReconnectWithDefaultConfig()
                .automaticReconnect()
                .applyAutomaticReconnect()
                .serverHost(mqttAddress)
                .useMqttVersion5()
                .build()
                .toAsync();
    }

    void startup(@Observes StartupEvent event) {
        Uni.createFrom()
                .completionStage(
                        client.connect()
                )
                .subscribe()
                .with(connectAck -> LOGGER.info("MQTT connection result code {}", connectAck.getReasonCode()));
    }

    public void publishUPSProvisioningMessage(Object message) throws JsonProcessingException {
       publishMessage("device/provisioning/ups", message);
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
