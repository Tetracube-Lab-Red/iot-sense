package red.tetracube.iotsense.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.iotsense.database.entities.Device;
import red.tetracube.iotsense.database.entities.DeviceSupportedCommand;
import red.tetracube.iotsense.dto.*;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.enumerations.DeviceType;
import red.tetracube.iotsense.modules.ups.UPSPulsarAPIClient;
import red.tetracube.iotsense.modules.ups.dto.DeviceProvisioningRequest;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DeviceServices {

    @Inject
    @RestClient
    UPSPulsarAPIClient upsPulsarAPIClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

    @Transactional
    public List<DeviceDataItem> getDevices(String hubSlug) {
        return Device.<Device>findAll().stream()
                .map(device ->
                        new DeviceDataItem(
                                device.id,
                                device.deviceType,
                                device.slug,
                                device.humanName,
                                device.roomSlug
                        )
                )
                .toList();
    }

    @Transactional
    public Result<DeviceRoomJoin> deviceRoomJoin(String hubSlug, String deviceSlug, String roomSlug) {
        var optionalDevice = Device.<Device>find("slug", deviceSlug)
                .firstResultOptional();
        if(optionalDevice.isEmpty()) {
            return Result.failed(new IoTSenseException.EntityNotFoundException("Device not found"));
        }
        var device = optionalDevice.get();
        if (!device.hubSlug.equals(hubSlug)) {
            return Result.failed(new IoTSenseException.UnauthorizedException("Cannot edit the device"));
        }

        device.roomSlug = roomSlug;
        device.persist();
    }

    @Transactional(rollbackOn = {Exception.class})
    public Result<DeviceCreateResponse> createDevice(String hubSlug, DeviceCreateRequest request) {
        LOGGER.info("Create device slug from the name");
        var deviceSlug = request.deviceName.trim().replaceAll(" ", "_").toLowerCase();

        LOGGER.info("Check if device already registered");
        var deviceExists = Device.existsBySlug(deviceSlug);
        if (deviceExists) {
            return Result.failed(new IoTSenseException.EntityExistsException("Device already exists"));
        }

        LOGGER.info("Store device in database");
        var device = new Device();
        device.internalName = switch (request.deviceType) {
            case UPS -> request.upsProvisioning.internalName;
        };
        device.id = UUID.randomUUID();
        device.slug = deviceSlug;
        device.humanName = request.deviceName;
        device.hubSlug = hubSlug;
        device.roomSlug = request.roomSlug;
        device.deviceType = request.deviceType;
        device.deviceSupportedCommands = buildDeviceInteractionsForDeviceType(request.deviceType);
        device.persist();

        LOGGER.info("Calling right module for device provisioning");
        var response = new DeviceCreateResponse(
                device.id,
                device.deviceType,
                device.slug,
                device.humanName,
                device.roomSlug
        );
        publishDeviceProvisioning(request);

        return Result.success(response);
    }

    private List<DeviceSupportedCommand> buildDeviceInteractionsForDeviceType(DeviceType deviceType) {
        return switch (deviceType) {
            case UPS -> null;
        };
    }

    private void publishDeviceProvisioning(DeviceCreateRequest deviceCreateRequest) {
        if (deviceCreateRequest.deviceType == DeviceType.UPS) {
            LOGGER.info("Transmitting UPS provisioning to ups pulsar module");
            var deviceProvisioningRequest = new DeviceProvisioningRequest(
                    deviceCreateRequest.upsProvisioning.deviceAddress,
                    deviceCreateRequest.upsProvisioning.devicePort,
                    deviceCreateRequest.upsProvisioning.internalName
            );
            // ToDo: if the device already provisioned, verify that there is another device registered with the same internal name, in that case go on storing the missing data in iot-sense schema
            upsPulsarAPIClient.deviceProvisioning(deviceProvisioningRequest);
        }
    }

}
