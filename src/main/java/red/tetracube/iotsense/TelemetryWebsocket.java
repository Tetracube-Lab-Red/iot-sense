package red.tetracube.iotsense;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import jakarta.inject.Inject;
import red.tetracube.iotsense.broker.BrokerClient;
import red.tetracube.iotsense.dto.DeviceTelemetryData;

@WebSocket(path = "/devices/telemetry")
public class TelemetryWebsocket {

    @Inject
    WebSocketConnection connection;

    @Inject
    BrokerClient brokerClient;

    @Inject
    EventBus eventBus;

    @OnOpen
    public Multi<DeviceTelemetryData> streamDeviceTelemetry() {
        return eventBus
                .<DeviceTelemetryData>consumer("realtime-device")
                .toMulti()
                .map(Message::body);
        //return brokerClient.getDeviceTelemetryIdStream();
    }

}
