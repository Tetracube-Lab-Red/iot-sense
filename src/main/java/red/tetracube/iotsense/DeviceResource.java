package red.tetracube.iotsense;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import red.tetracube.iotsense.dto.DeviceCreateRequest;
import red.tetracube.iotsense.dto.DeviceCreateResponse;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.services.DeviceServices;

@RequestScoped
@Authenticated
@Path("/devices")
public class DeviceResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(value = "hub_slug")
    String hubSlug;

    @Inject
    DeviceServices deviceServices;

    @RunOnVirtualThread
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceCreateResponse deviceCreate(@RequestBody @Valid DeviceCreateRequest request) {
        var deviceCreateResult = deviceServices.createDevice(hubSlug, request);
        if (deviceCreateResult.isSuccess()) {
            return deviceCreateResult.getContent();
        }

        if (deviceCreateResult.getException() instanceof IoTSenseException.EntityExistsException) {
            throw new ClientErrorException("Device already exists", Response.Status.CONFLICT);
        } else {
            throw new InternalServerErrorException(deviceCreateResult.getException());
        }
    }

    // Update device room

}