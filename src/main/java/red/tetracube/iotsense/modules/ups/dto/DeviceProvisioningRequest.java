package red.tetracube.iotsense.modules.ups.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeviceProvisioningRequest(
        @JsonProperty String hostname,
        @JsonProperty Integer port,
        @JsonProperty String internalName
) {
}
