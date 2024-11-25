package red.tetracube.iotsense.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeviceRoomJoin (
        @NotNull
        @NotEmpty
        @JsonProperty
        String deviceSlug,

        @NotNull
        @NotEmpty
        @JsonProperty
        String roomSlug
) {
    
}
