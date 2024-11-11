package red.tetracube.iotsense.modules.ups;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import red.tetracube.iotsense.modules.ups.dto.DeviceProvisioningRequest;

@Path("/ups-pulsar")
@RegisterRestClient(configKey="ups-pulsar-api")
public interface UPSPulsarAPIClient {

    @Path("/device-provisioning")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void deviceProvisioning(DeviceProvisioningRequest request);

}
