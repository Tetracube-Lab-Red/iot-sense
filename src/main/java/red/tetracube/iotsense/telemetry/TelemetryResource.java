package red.tetracube.iotsense.telemetry;

import java.util.UUID;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;

@RequestScoped
@Authenticated
@Path("/devices/{deviceId}/telemetry")
public class TelemetryResource {

    @Inject
    TelemetryServices telemetryServices;

    @RunOnVirtualThread
    @HEAD
    @Path("/")
    public void requestDeviceTelemetry(@PathParam("deviceId") UUID deviceId) {
        var requestTelemetryResult = telemetryServices.requestDeviceTelemetry(deviceId);
        if (!requestTelemetryResult.isSuccess()) {
            if (requestTelemetryResult.getException() instanceof IoTSenseException.EntityExistsException) {
                throw new ClientErrorException("Telemetry or device not found", Response.Status.NOT_FOUND);
            } else {
                throw new InternalServerErrorException(requestTelemetryResult.getException());
            }
        }
    }

}
