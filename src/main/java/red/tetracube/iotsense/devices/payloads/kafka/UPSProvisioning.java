package red.tetracube.iotsense.devices.payloads.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class UPSProvisioning extends BaseInternalProvisioningPayload {

    @JsonProperty
    public String deviceAddress;

    @JsonProperty
    public Integer devicePort;

    @JsonProperty
    public String internalName;

    public UPSProvisioning(String deviceAddress, Integer devicePort, String internalName) {
        this.deviceAddress = deviceAddress;
        this.devicePort = devicePort;
        this.internalName = internalName;
    }    

}
