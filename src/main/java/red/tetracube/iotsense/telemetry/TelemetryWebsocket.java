package red.tetracube.iotsense.telemetry;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Channel;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import red.tetracube.iotsense.telemetry.payloads.DeviceTelemetryData;

import java.util.UUID;

@WebSocket(path = "/devices/telemetry")
public class TelemetryWebsocket {

    @Inject
    WebSocketConnection connection;

    @Channel("device-telemetry-response")
    Multi<ConsumerRecord<UUID, DeviceTelemetryData>> telemetries;

    @OnOpen
    public Multi<DeviceTelemetryData> streamDeviceTelemetry() {
        return telemetries.map(ConsumerRecord::value);
    }

}
