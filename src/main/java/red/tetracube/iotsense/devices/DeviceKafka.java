package red.tetracube.iotsense.devices;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import red.tetracube.iotsense.devices.payloads.kafka.UPSProvisioning;
import red.tetracube.iotsense.enumerations.DeviceType;
import red.tetracube.iotsense.enumerations.ProvisioningStatus;

import java.util.UUID;

@ApplicationScoped
public class DeviceKafka {

    @Inject
    DeviceServices deviceServices;

    @Inject
    @Channel("device-provisioning-ups")
    Emitter<Record<DeviceType, UPSProvisioning>> upsProvisioningEmitter;

    @Inject
    RedisDataSource redisDataSource;

    @RunOnVirtualThread
    @Incoming("device-provisioning-ups-ack")
    public void consumeUPSProvisioningAck(UUID deviceId) {
        deviceServices.updateDeviceProvisioningStatus(deviceId, ProvisioningStatus.COMPLETED);
        redisDataSource.pubsub(UUID.class).publish("device-update", deviceId);
    }

    @ConsumeEvent("device-provisioning")
    public void sendDeviceProvisioningUps(UPSProvisioning upsProvisioning) {
        upsProvisioningEmitter.send(
                Record.of(
                        upsProvisioning.deviceType,
                        upsProvisioning
                )
        );
    }

}
