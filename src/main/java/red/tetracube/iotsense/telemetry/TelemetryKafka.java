package red.tetracube.iotsense.telemetry;

import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import red.tetracube.iotsense.telemetry.payloads.DeviceTelemetryData;

@ApplicationScoped
public class TelemetryKafka {

    @Inject
    RedisDataSource redisDataSource;

    @Incoming("device-telemetry-response")
    public void receiveTelemetry(DeviceTelemetryData data) {
        redisDataSource.pubsub(DeviceTelemetryData.class).publish("device-telemetry", data);
    }
}
