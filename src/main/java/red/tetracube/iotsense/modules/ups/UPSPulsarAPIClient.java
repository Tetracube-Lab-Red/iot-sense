package red.tetracube.iotsense.modules.ups;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import red.tetracube.iotsense.modules.ups.dto.DeviceProvisioningRequest;
import red.tetracube.iotsense.modules.ups.dto.UPSBasicTelemetryData;

@Path("/ups-pulsar")
@RegisterRestClient(configKey="ups-pulsar-api")
public interface UPSPulsarAPIClient {

    @Path("/device/provisioning")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void deviceProvisioning(DeviceProvisioningRequest request);

    @Path("/device/{internalName}/telemetry")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    UPSBasicTelemetryData getBasicUPSTelemetry(@PathParam("internalName") String internalName);

}
