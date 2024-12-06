package red.tetracube.iotsense.devices.mappers;

import red.tetracube.iotsense.devices.payloads.api.DeviceCreateRequest;
import red.tetracube.iotsense.devices.payloads.kafka.BaseInternalProvisioningPayload;
import red.tetracube.iotsense.devices.payloads.kafka.UPSProvisioning;
import red.tetracube.iotsense.enumerations.DeviceType;

public class ProvisioningAPIToKafkaPayloads<S extends BaseInternalProvisioningPayload> {

    private final DeviceCreateRequest apiRequest;

    public ProvisioningAPIToKafkaPayloads(DeviceCreateRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public BaseInternalProvisioningPayload doMapping() {
        return switch (apiRequest.deviceType) {
            case DeviceType.UPS:
                yield new UPSProvisioning(
                        apiRequest.upsProvisioning.deviceAddress,
                        apiRequest.upsProvisioning.devicePort,
                        apiRequest.upsProvisioning.internalName
                    );
            default:
                yield null;
        };
    }

}
