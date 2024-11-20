package red.tetracube.iotsense;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import red.tetracube.iotsense.broker.BrokerClient;

import java.time.Duration;

@WebSocket(path = "/devices/telemetry")
public class TelemetryWebsocket {

    @Inject
    WebSocketConnection connection;

    @Inject
    BrokerClient brokerClient;

    @OnOpen
    public Multi<Long> streamDeviceTelemetry() {
        return brokerClient.getDeviceTelemetryIdStream();
    }

}
