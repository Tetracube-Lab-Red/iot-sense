package red.tetracube.iotsense.telemetry;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import red.tetracube.iotsense.database.entities.DeviceEntity;
import red.tetracube.iotsense.dto.Result;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;

@ApplicationScoped
public class TelemetryServices {

    @Inject
    @Channel("device-telemetry-request")
    Emitter<UUID> telemetryRequestEmitter;

    public Result<Void> requestDeviceTelemetry(UUID deviceId) {
        var optionalDevice = DeviceEntity.<DeviceEntity>findByIdOptional(deviceId);
        if (optionalDevice.isEmpty()) {
            return Result.failed(new IoTSenseException.EntityNotFoundException("No device found with given slug"));
        }

        telemetryRequestEmitter.send(deviceId);
        return Result.success(null);
    }

}
