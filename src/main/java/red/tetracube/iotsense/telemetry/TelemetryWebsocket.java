package red.tetracube.iotsense.telemetry;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import jakarta.inject.Inject;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Multi;
import red.tetracube.iotsense.telemetry.payloads.DeviceTelemetryData;

@WebSocket(path = "/devices/telemetry")
public class TelemetryWebsocket {

    @Inject
    ReactiveRedisDataSource redisDataSource;

    @OnOpen
    public Multi<DeviceTelemetryData> streamDeviceTelemetry() {
        return redisDataSource.pubsub(DeviceTelemetryData.class)
                .subscribe("device-telemetry");
    }

}
