package red.tetracube.iotsense.modules.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

public record NotiFluxDeviceProvisioningRequest(
        @JsonProperty UUID deviceId,
        @JsonProperty DeviceType deviceType,
        @JsonProperty String internalName,
        @JsonProperty String slug
) {
}
