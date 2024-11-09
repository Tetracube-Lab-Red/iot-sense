package red.tetracube.iotsense.dto;

import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

public record DeviceCreateResponse(
        UUID deviceId,
        DeviceType deviceType,
        String deviceSlug,
        String deviceName,
        String roomSlug
) {
}
