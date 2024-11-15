package red.tetracube.iotsense.dto;

import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

public record DeviceDataItem(
        UUID id,
        DeviceType type,
        String slug,
        String name,
        String roomSlug
) {
}
