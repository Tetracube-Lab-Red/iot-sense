package red.tetracube.iotsense.devices.payloads.api;

import jakarta.validation.constraints.*;
import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

public class DeviceCreateRequest {

    @NotNull
    public DeviceType deviceType;

    @NotEmpty
    @NotNull
    @Pattern(regexp = "^[ \\w]+$") @Size(min = 5, max = 50)
    public String deviceName;

    public UUID roomId;

    public UPSProvisioningFields upsProvisioning;

    public static final class UPSProvisioningFields {
        @NotEmpty
        @NotNull
        public String deviceAddress;

        @NotNull
        @Min(1000)
        @Max(9999)
        public Integer devicePort;

        @NotEmpty
        @NotNull
        public String internalName;
    }

}