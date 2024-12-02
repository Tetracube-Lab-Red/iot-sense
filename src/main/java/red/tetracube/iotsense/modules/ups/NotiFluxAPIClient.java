package red.tetracube.iotsense.modules.ups;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import red.tetracube.iotsense.modules.ups.dto.NotiFluxDeviceProvisioningRequest;

@Path("/notiflux")
@RegisterRestClient(configKey = "notiflux-api")
public interface NotiFluxAPIClient {

    @Path("/device/provisioning")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Void> deviceProvisioning(NotiFluxDeviceProvisioningRequest request);

}
