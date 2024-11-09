package red.tetracube.iotsense.dto;

import red.tetracube.iotsense.enumerations.DeviceType;

public record DeviceCreateResponse(
        Long deviceId,
        DeviceType deviceType,
        String deviceSlug,
        String deviceName,
        String roomSlug
) {
}
