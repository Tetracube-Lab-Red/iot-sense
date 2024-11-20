package red.tetracube.iotsense;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import red.tetracube.iotsense.broker.BrokerClient;
import red.tetracube.iotsense.dto.DeviceTelemetryData;

@WebSocket(path = "/devices/telemetry")
public class TelemetryWebsocket {

    @Inject
    WebSocketConnection connection;

    @Inject
    BrokerClient brokerClient;

    @OnOpen
    public Multi<DeviceTelemetryData> streamDeviceTelemetry() {
        return brokerClient.getDeviceTelemetryIdStream();
    }

}
