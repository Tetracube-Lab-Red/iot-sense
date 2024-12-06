package red.tetracube.iotsense.devices.payloads.api;

import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

public record DeviceCreateResponse(
        UUID deviceId,
        DeviceType deviceType,
        String deviceName,
        UUID roomId
) {
}
