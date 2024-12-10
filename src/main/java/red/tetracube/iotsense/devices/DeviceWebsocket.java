package red.tetracube.iotsense.devices;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import red.tetracube.iotsense.devices.payloads.api.DevicePayload;

import java.util.Optional;
import java.util.UUID;

@WebSocket(path = "/devices")
public class DeviceWebsocket {

    @Inject
    ReactiveRedisDataSource redisDataSource;

    @Inject
    DeviceServices deviceServices;

    @OnOpen
    public Multi<DevicePayload> streamDevice() {
        return redisDataSource.pubsub(UUID.class)
                .subscribe("device-update")
                .emitOn(Infrastructure.getDefaultExecutor())
                .map(deviceId -> deviceServices.getDevice(deviceId))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

}
