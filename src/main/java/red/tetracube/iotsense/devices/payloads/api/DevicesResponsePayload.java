package red.tetracube.iotsense.devices.payloads.api;

import java.util.List;

public record DevicesResponsePayload(
        List<DevicePayload> devices
) {
}
