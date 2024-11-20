package red.tetracube.iotsense;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import red.tetracube.iotsense.dto.DeviceTelemetryData;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
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
    public DeviceTelemetryData getDeviceTelemetry(@PathParam("deviceSlug") String deviceSlug) {
        var getTelemetryResult = telemetryServices.getLatestDeviceTelemetry(deviceSlug);
        if (getTelemetryResult.isSuccess()) {
            return getTelemetryResult.getContent();
        }

        if (getTelemetryResult.getException() instanceof IoTSenseException.EntityExistsException) {
            throw new ClientErrorException("Telemetry or device not found", Response.Status.NOT_FOUND);
        } else {
            throw new InternalServerErrorException(getTelemetryResult.getException());
        }
    }

}
