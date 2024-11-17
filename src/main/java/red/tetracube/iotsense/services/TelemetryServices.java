package red.tetracube.iotsense.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import red.tetracube.iotsense.database.entities.Device;
import red.tetracube.iotsense.dto.Result;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.modules.ups.UPSPulsarAPIClient;

@ApplicationScoped
public class TelemetryServices {

    @RestClient
    @Inject
    UPSPulsarAPIClient upsPulsarAPIClient;

    public Result<Object> getLatestDeviceTelemetry(String deviceSlug) {
        var optionalDevice = Device.<Device>find("slug", deviceSlug).firstResultOptional();
        if (optionalDevice.isEmpty()) {
            return Result.failed(new IoTSenseException.EntityNotFoundException("No device found with given slug"));
        }
        var internalName = optionalDevice.get().internalName;
        var deviceType = optionalDevice.get().deviceType;
        var telemetry = switch (deviceType) {
            case UPS -> upsPulsarAPIClient.getBasicUPSTelemetry(internalName);
        };
    }

}
