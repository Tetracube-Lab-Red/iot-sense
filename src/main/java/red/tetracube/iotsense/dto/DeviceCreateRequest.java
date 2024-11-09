package red.tetracube.iotsense.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import red.tetracube.iotsense.enumerations.DeviceType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceCreateRequest.UPS.class, name = "ups")
})
public sealed class DeviceCreateRequest permits DeviceCreateRequest.UPS {

    @NotNull
    public DeviceType deviceType;

    @NotEmpty
    @NotNull
    public String deviceName;

    public String roomSlug;

    @JsonTypeName("ups")
    public static final class UPS extends DeviceCreateRequest {
        @NotEmpty
        @NotNull
        public String nutAddress;

        @NotNull
        @Min(1000)
        @Max(9999)
        public Integer nutPort;

        @NotEmpty
        @NotNull
        public String nutAlias;
    }

}