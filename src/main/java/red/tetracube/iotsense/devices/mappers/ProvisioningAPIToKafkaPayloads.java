package red.tetracube.iotsense.devices.mappers;

import java.util.UUID;

import red.tetracube.iotsense.devices.payloads.api.DeviceCreateRequest;
import red.tetracube.iotsense.devices.payloads.kafka.UPSProvisioning;
import red.tetracube.iotsense.enumerations.DeviceType;

public class ProvisioningAPIToKafkaPayloads {

    public static <S> S doMapping(Class<S> outType, UUID deviceId, DeviceCreateRequest apiRequest) {
        return switch (apiRequest.deviceType) {
            case DeviceType.UPS:
                yield outType.cast(
                        new UPSProvisioning(
                                deviceId,
                                apiRequest.upsProvisioning.deviceAddress,
                                apiRequest.upsProvisioning.devicePort,
                                apiRequest.upsProvisioning.internalName
                        )
                );
        };
    }

}
