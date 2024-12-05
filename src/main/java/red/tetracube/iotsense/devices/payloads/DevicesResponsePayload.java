package red.tetracube.iotsense.devices.payloads;

import java.util.List;

public record DevicesResponsePayload(
        List<DevicePayload> devices
) {
}
