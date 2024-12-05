package red.tetracube.iotsense.devices.payloads;

import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

public record DevicePayload(
        UUID id,
        DeviceType type,
        String name,
        UUID roomId
) {
}
