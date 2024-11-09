package red.tetracube.iotsense;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import red.tetracube.iotsense.dto.DeviceCreateRequest;
import red.tetracube.iotsense.dto.DeviceCreateResponse;

@Path("/devices")
public class DeviceResource {

    @RunOnVirtualThread
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DeviceCreateResponse deviceCreate(@RequestBody @Valid DeviceCreateRequest request) {
        return new DeviceCreateResponse(
                request.deviceType,
                request.deviceName.trim().toLowerCase(),
                request.deviceName
        );
    }

    // Assign device to room

}
