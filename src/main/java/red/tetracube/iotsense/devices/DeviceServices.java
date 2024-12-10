package red.tetracube.iotsense.devices;

import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import red.tetracube.iotsense.database.entities.DeviceEntity;
import red.tetracube.iotsense.devices.mappers.ProvisioningAPIToKafkaPayloads;
import red.tetracube.iotsense.devices.payloads.api.DeviceCreateRequest;
import red.tetracube.iotsense.devices.payloads.api.DevicePayload;
import red.tetracube.iotsense.devices.payloads.api.DeviceRoomJoinPayload;
import red.tetracube.iotsense.devices.payloads.kafka.UPSProvisioning;
import red.tetracube.iotsense.dto.*;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.enumerations.DeviceType;
import red.tetracube.iotsense.enumerations.ProvisioningStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DeviceServices {

    @Inject
    EventBus eventBus;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceServices.class);

    @Transactional
    public List<DevicePayload> getDevices(UUID hubId) {
        return DeviceEntity.findByHub(hubId).stream()
                .map(deviceEntity ->
                        new DevicePayload(
                                deviceEntity.id,
                                deviceEntity.deviceType,
                                deviceEntity.humanName,
                                deviceEntity.roomId,
                                deviceEntity.deviceType,
                                deviceEntity.provisioningStatus
                        )
                )
                .toList();
    }

    @Transactional
    public Optional<DevicePayload> getDevice(UUID deviceId) {
        return DeviceEntity.<DeviceEntity>findByIdOptional(deviceId)
                .map(deviceEntity ->
                        new DevicePayload(
                                deviceEntity.id,
                                deviceEntity.deviceType,
                                deviceEntity.humanName,
                                deviceEntity.roomId,
                                deviceEntity.deviceType,
                                deviceEntity.provisioningStatus
                        )
                );
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

    @Transactional
    public void updateDeviceProvisioningStatus(UUID deviceId, ProvisioningStatus provisioningStatus) {
        DeviceEntity.<DeviceEntity>findByIdOptional(deviceId)
                .ifPresent(deviceEntity -> {
                    deviceEntity.provisioningStatus = provisioningStatus;
                    deviceEntity.persist();
                });
    }

    @Transactional(rollbackOn = {Exception.class})
    public Result<DevicePayload> createDevice(UUID hubId, DeviceCreateRequest request) {
        LOGGER.info("Check if device already registered");
        var deviceExists = DeviceEntity.existsByName(request.deviceName);
        if (deviceExists) {
            return Result.failed(new IoTSenseException.EntityExistsException("Device already exists"));
        }

        LOGGER.info("Store device in database");
        var device = new DeviceEntity();
        device.id = UUID.randomUUID();
        device.humanName = request.deviceName;
        device.hubId = hubId;
        device.roomId = request.roomId;
        device.deviceType = request.deviceType;
        device.provisioningStatus = ProvisioningStatus.CREATED;
        device.persist();

        LOGGER.info("Calling right module for device provisioning");
        var response = new DevicePayload(
                device.id,
                device.deviceType,
                device.humanName,
                device.roomId,
                device.deviceType,
                device.provisioningStatus
        );
        publishDeviceProvisioning(device.id, request);

        return Result.success(response);
    }

    private void publishDeviceProvisioning(UUID deviceId, DeviceCreateRequest deviceCreateRequest) {
        switch (deviceCreateRequest.deviceType) {
            case DeviceType.UPS:
                var kafkaPayload = ProvisioningAPIToKafkaPayloads.doMapping(UPSProvisioning.class, deviceId, deviceCreateRequest);
                eventBus.publish("device-provisioning", kafkaPayload);
                break;
        }
    }
}
