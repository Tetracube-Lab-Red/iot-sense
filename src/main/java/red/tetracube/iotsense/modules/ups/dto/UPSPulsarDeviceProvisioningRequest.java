package red.tetracube.iotsense.modules.ups.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UPSPulsarDeviceProvisioningRequest(
        @JsonProperty String hostname,
        @JsonProperty Integer port,
        @JsonProperty String internalName
) {
}
