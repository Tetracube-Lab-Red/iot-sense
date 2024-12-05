package red.tetracube.iotsense.devices;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.iotsense.devices.payloads.DeviceCreateRequest;
import red.tetracube.iotsense.config.IoTSenseConfig;
import red.tetracube.iotsense.database.entities.DeviceEntity;
import red.tetracube.iotsense.devices.payloads.DeviceCreateResponse;
import red.tetracube.iotsense.devices.payloads.DevicePayload;
import red.tetracube.iotsense.devices.payloads.DeviceRoomJoinPayload;
import red.tetracube.iotsense.dto.*;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.enumerations.DeviceType;
import red.tetracube.iotsense.modules.notification.NotiFluxAPIClient;
import red.tetracube.iotsense.modules.ups.UPSPulsarAPIClient;
import red.tetracube.iotsense.modules.notification.dto.NotiFluxDeviceProvisioningRequest;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DeviceServices {

    @Inject
    @RestClient
    NotiFluxAPIClient notiFluxAPIClient;

    @Inject
    @RestClient
    UPSPulsarAPIClient upsPulsarAPIClient;

    @Inject
    IoTSenseConfig iotSenseConfig;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

    @Transactional
    public List<DevicePayload> getDevices(UUID hubId) {
        return DeviceEntity.findByHub(hubId).stream()
                .map(deviceEntity ->
                        new DevicePayload(
                                deviceEntity.id,
                                deviceEntity.deviceType,
                                deviceEntity.humanName,
                                deviceEntity.roomId
                        )
                )
                .toList();
    }

    @Transactional
    public Result<DeviceRoomJoinPayload> deviceRoomJoin(UUID hubId, DeviceRoomJoinPayload joinPayload) {
        var optionalDevice = DeviceEntity.<DeviceEntity>findByIdOptional(joinPayload.deviceId());
        if (optionalDevice.isEmpty()) {
            return Result.failed(new IoTSenseException.EntityNotFoundException("Device not found"));
        }
        var device = optionalDevice.get();
        if (!device.hubId.equals(hubId)) {
            return Result.failed(new IoTSenseException.UnauthorizedException("Cannot edit the device"));
        }

        device.roomId = joinPayload.roomId();
        device.persist();

        var response = new DeviceRoomJoinPayload(
                device.id,
                device.roomId
        );
        return Result.success(response);
    }

    @Transactional(rollbackOn = {Exception.class})
    public Result<DeviceCreateResponse> createDevice(UUID hubId, DeviceCreateRequest request) {
        LOGGER.info("Check if device already registered");
        var deviceExists = DeviceEntity.existsByName(request.deviceName);
        if (deviceExists) {
            return Result.failed(new IoTSenseException.EntityExistsException("Device already exists"));
        }

        LOGGER.info("Store device in database");
        var device = new DeviceEntity();
        device.id = UUID.randomUUID();
        device.internalName = switch (request.deviceType) {
            case UPS -> request.upsProvisioning.internalName;
            case SWITCH -> request.deviceName; // ToDo: temporary code
        };
        device.humanName = request.deviceName;
        device.hubId = hubId;
        device.roomId = request.roomId;
        device.deviceType = request.deviceType;
        device.persist();

        LOGGER.info("Calling right module for device provisioning");
        var response = new DeviceCreateResponse(
                device.id,
                device.deviceType,
                device.humanName,
                device.roomId
        );
      //  publishDeviceProvisioning(device.id, device.slug, request);

        return Result.success(response);
    }

    private void publishDeviceProvisioning(UUID deviceId, String deviceSlug, DeviceCreateRequest deviceCreateRequest) {
       /* List<Uni<Void>> requests = new ArrayList<>();
        if (iotSenseConfig.modules().notiflux().enabled()) {
            LOGGER.info("Transmitting device provisioning to notiflux module");
            requests.add(
                    createNotiFluxProvisioningRequest(
                            deviceId,
                            deviceCreateRequest.deviceType,
                            deviceSlug,
                            deviceCreateRequest.upsProvisioning.internalName
                    )
            );
        }
        switch (deviceCreateRequest.deviceType) {
            case UPS -> {
                LOGGER.info("Transmitting UPS provisioning to ups pulsar module");
                var deviceProvisioningRequest = new UPSPulsarDeviceProvisioningRequest(
                        deviceCreateRequest.upsProvisioning.deviceAddress,
                        deviceCreateRequest.upsProvisioning.devicePort,
                        deviceCreateRequest.upsProvisioning.internalName
                );
                requests.add(upsPulsarAPIClient.deviceProvisioning(deviceProvisioningRequest));
            }
            case SWITCH -> {
                LOGGER.info("Transmitting SWITCH provisioning to switch module");
            }
        }
        Uni.join()
                .all(requests)
                .andCollectFailures()
                .await()
                .atMost(Duration.ofSeconds(10));*/
    }

    private Uni<Void> createNotiFluxProvisioningRequest(UUID deviceId, DeviceType deviceType, String deviceSlug, String deviceInternalName) {
        var notiFluxProvisioningRequest = new NotiFluxDeviceProvisioningRequest(
                deviceId,
                deviceType,
                deviceInternalName,
                deviceSlug
        );
        return notiFluxAPIClient.deviceProvisioning(notiFluxProvisioningRequest);
    }
}
