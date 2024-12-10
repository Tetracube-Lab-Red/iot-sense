package red.tetracube.iotsense.devices;

import io.quarkus.security.Authenticated;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import red.tetracube.iotsense.config.IoTSenseConfig;
import red.tetracube.iotsense.devices.payloads.api.DeviceCreateRequest;
import red.tetracube.iotsense.devices.payloads.api.DevicePayload;
import red.tetracube.iotsense.devices.payloads.api.DeviceRoomJoinPayload;
import red.tetracube.iotsense.devices.payloads.api.DevicesResponsePayload;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.UUID;

@RequestScoped
@Authenticated
@Path("/devices")
public class DeviceResource {

    @Inject
    @Claim(value = "hub_id")
    String hubId;

    @Inject
    DeviceServices deviceServices;

    @Inject
    IoTSenseConfig ioTSenseConfig;

    @RunOnVirtualThread
    @POST
    @Path("/provisioning")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DevicePayload deviceCreate(@RequestBody @Valid DeviceCreateRequest request) {
        if (request.deviceType == DeviceType.UPS && !ioTSenseConfig.modules().ups().enabled()) {
            throw new ServerErrorException(Response.Status.NOT_IMPLEMENTED, null);
        }

        var deviceCreateResult = deviceServices.createDevice(getHubId(), request);
        if (deviceCreateResult.isSuccess()) {
            return deviceCreateResult.getContent();
        }

        if (deviceCreateResult.getException() instanceof IoTSenseException.EntityExistsException) {
            throw new ClientErrorException("Device already exists", Response.Status.CONFLICT);
        } else {
            throw new InternalServerErrorException(deviceCreateResult.getException());
        }
    }

    @RunOnVirtualThread
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public DevicesResponsePayload getDevices() {
        var devices = deviceServices.getDevices(getHubId());
        return new DevicesResponsePayload(devices);
    }

    @RunOnVirtualThread
    @PATCH
    @Path("/room")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DeviceRoomJoinPayload deviceRoomJoin(@RequestBody @Valid DeviceRoomJoinPayload request) {
        var deviceRoomJoinResult = deviceServices.deviceRoomJoin(getHubId(), request);
        if (deviceRoomJoinResult.isSuccess()) {
            return deviceRoomJoinResult.getContent();
        }

        if (deviceRoomJoinResult.getException() instanceof IoTSenseException.EntityNotFoundException) {
            throw new ClientErrorException("Device not found", Response.Status.NOT_FOUND);
        } else if (deviceRoomJoinResult.getException() instanceof IoTSenseException.UnauthorizedException) {
            throw new ClientErrorException("No authorized to update the room", Response.Status.UNAUTHORIZED);
        } else {
            throw new InternalServerErrorException(deviceRoomJoinResult.getException());
        }
    }

    private UUID getHubId() {
        return UUID.fromString(hubId);
    }

}
