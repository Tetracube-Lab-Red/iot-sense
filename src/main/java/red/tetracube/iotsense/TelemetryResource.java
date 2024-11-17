package red.tetracube.iotsense;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import red.tetracube.iotsense.dto.GetDevicesResponse;
import red.tetracube.iotsense.services.TelemetryServices;

@RequestScoped
@Authenticated
@Path("/devices/{deviceSlug}/telemetry")
public class TelemetryResource {

    @Inject
    TelemetryServices telemetryServices;

    @RunOnVirtualThread
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public GetDevicesResponse getDevices(@PathParam("deviceSlug") String deviceSlug) {

    }

}
