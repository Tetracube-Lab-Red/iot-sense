package red.tetracube.iotsense.modules.ups;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import red.tetracube.iotsense.modules.ups.dto.UPSPulsarDeviceProvisioningRequest;
import red.tetracube.iotsense.modules.ups.dto.UPSTelemetryData;

@Path("/ups-pulsar")
@RegisterRestClient(configKey = "ups-pulsar-api")
public interface UPSPulsarAPIClient {

    @Path("/device/provisioning")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void deviceProvisioning(UPSPulsarDeviceProvisioningRequest request);

    @Path("/device/{internalName}/telemetry")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    UPSTelemetryData getUPSTelemetry(@PathParam("internalName") String internalName);

}
