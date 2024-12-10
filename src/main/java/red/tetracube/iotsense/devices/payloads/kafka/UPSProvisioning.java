package red.tetracube.iotsense.devices.payloads.kafka;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import red.tetracube.iotsense.enumerations.DeviceType;

public final class UPSProvisioning {

    @JsonProperty
    public UUID deviceId;

    @JsonProperty
    public String deviceAddress;

    @JsonProperty
    public Integer devicePort;

    @JsonProperty
    public String internalName;

    @JsonProperty
    public DeviceType deviceType;

    public UPSProvisioning(UUID deviceId, String deviceAddress, Integer devicePort, String internalName, DeviceType deviceType) {
        this.deviceAddress = deviceAddress;
        this.devicePort = devicePort;
        this.internalName = internalName;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }    

}
