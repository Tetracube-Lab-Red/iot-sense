package red.tetracube.iotsense.dto;

import java.util.List;

public record GetDevicesResponse(
        List<DeviceDataItem> devices
) {
}
