package red.tetracube.iotsense.devices.payloads.kafka;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "provisioning_type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = UPSProvisioning.class, name = "UPSProvisioning")
})
public sealed class BaseInternalProvisioningPayload permits UPSProvisioning {
    
}
