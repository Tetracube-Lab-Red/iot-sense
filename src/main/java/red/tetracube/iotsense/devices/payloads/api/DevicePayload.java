package red.tetracube.iotsense.devices.payloads.api;

import red.tetracube.iotsense.enumerations.DeviceType;
import red.tetracube.iotsense.enumerations.ProvisioningStatus;

import java.util.UUID;

public record DevicePayload(
        UUID id,
        String name,
        UUID roomId,
        DeviceType deviceType,
        ProvisioningStatus provisioningStatus
) {
}
