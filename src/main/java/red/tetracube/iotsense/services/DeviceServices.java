package red.tetracube.iotsense.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.iotsense.broker.BrokerClient;
import red.tetracube.iotsense.database.entities.Device;
import red.tetracube.iotsense.database.entities.DeviceSupportedCommand;
import red.tetracube.iotsense.dto.DeviceCreateRequest;
import red.tetracube.iotsense.dto.DeviceCreateResponse;
import red.tetracube.iotsense.dto.Result;
import red.tetracube.iotsense.dto.broker.DeviceUPSProvisioning;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.enumerations.DeviceType;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DeviceServices {

    @Inject
    BrokerClient brokerClient;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

    @Transactional
    public Result<DeviceCreateResponse> createDevice(String hubSlug, DeviceCreateRequest request){
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
            case UPS -> ((DeviceCreateRequest.UPS) request).nutAlias;
        };
        device.id = UUID.randomUUID();
        device.slug = deviceSlug;
        device.humanName = request.deviceName;
        device.hubSlug = hubSlug;
        device.roomSlug = request.roomSlug;
        device.deviceType = request.deviceType;
        device.deviceSupportedCommands = buildDeviceInteractionsForDeviceType(request.deviceType);
        device.persist();

        LOGGER.info("Emit message in the broker on the right topic");
        var response = new DeviceCreateResponse(
                device.id,
                device.deviceType,
                device.slug,
                device.humanName,
                device.roomSlug
        );

        try {
            publicDeviceProvisioning(request);
        } catch (JsonProcessingException e) {
            return Result.failed(new IoTSenseException.InternalException(e.getMessage()));
        }

        return Result.success(response);
    }

    private List<DeviceSupportedCommand> buildDeviceInteractionsForDeviceType(DeviceType deviceType) {
        return switch (deviceType) {
            case UPS -> null;
        };
    }

    private void publicDeviceProvisioning(DeviceCreateRequest deviceCreateRequest) throws JsonProcessingException {
        if (deviceCreateRequest.deviceType == DeviceType.UPS) {
            var upsCreate = (DeviceCreateRequest.UPS)deviceCreateRequest;
            var deviceUPSProvisioning = new DeviceUPSProvisioning(
                    upsCreate.nutAddress,
                    upsCreate.nutPort,
                    upsCreate.nutAlias
            );
            brokerClient.publishUPSProvisioningMessage(deviceUPSProvisioning);
        }
    }

}
