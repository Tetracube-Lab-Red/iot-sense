package red.tetracube.iotsense.devices.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeviceRoomJoinPayload(
        @NotNull
        @JsonProperty
        UUID deviceId,

        @JsonProperty
        UUID roomId
) {
    
}
