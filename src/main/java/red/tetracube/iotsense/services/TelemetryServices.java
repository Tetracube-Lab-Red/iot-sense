package red.tetracube.iotsense.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.smallrye.reactive.messaging.kafka.Record;

import red.tetracube.iotsense.database.entities.DeviceEntity;
import red.tetracube.iotsense.dto.Result;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.enumerations.DeviceType;

@ApplicationScoped
public class TelemetryServices {

    @Inject
    @Channel("device-telemetry-request")
    Emitter<Record<DeviceType, UUID>> telemetryRequestEmitter;

    public Result<Void> requestDeviceTelemetry(UUID deviceId) {
        var optionalDevice = DeviceEntity.<DeviceEntity>findByIdOptional(deviceId);
        if (optionalDevice.isEmpty()) {
            return Result.failed(new IoTSenseException.EntityNotFoundException("No device found with given slug"));
        }
        
        var deviceType = optionalDevice.get().deviceType;
        telemetryRequestEmitter.send(
            Record.of(
                deviceType, 
                deviceId
            )
        );
        return Result.success(null);
    }

}
