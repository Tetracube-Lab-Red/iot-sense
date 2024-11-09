package red.tetracube.iotsense.dto;

import red.tetracube.iotsense.enumerations.DeviceType;

public record DeviceCreateResponse(
        DeviceType deviceType,
        String deviceAlias,
        String deviceName
) {
}
