package red.tetracube.iotsense.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import red.tetracube.iotsense.database.entities.Device;
import red.tetracube.iotsense.dto.DeviceTelemetryData;
import red.tetracube.iotsense.dto.Result;
import red.tetracube.iotsense.dto.exceptions.IoTSenseException;
import red.tetracube.iotsense.modules.ups.UPSPulsarAPIClient;

@ApplicationScoped
public class TelemetryServices {

    @RestClient
    @Inject
    UPSPulsarAPIClient upsPulsarAPIClient;

    public Result<DeviceTelemetryData> getLatestDeviceTelemetry(String deviceSlug) {
        var optionalDevice = Device.<Device>find("slug", deviceSlug).firstResultOptional();
        if (optionalDevice.isEmpty()) {
            return Result.failed(new IoTSenseException.EntityNotFoundException("No device found with given slug"));
        }
        var internalName = optionalDevice.get().internalName;
        var deviceType = optionalDevice.get().deviceType;
        var telemetry = switch (deviceType) {
            case UPS -> {
                var rawTelemetry = upsPulsarAPIClient.getUPSTelemetry(internalName);
                var response = new DeviceTelemetryData.UPSTelemetryData();
                response.deviceSlug = optionalDevice.get().slug;
                response.telemetryTS = rawTelemetry.telemetryTS();
                response.outFrequency = rawTelemetry.outFrequency();
                response.outVoltage = rawTelemetry.outVoltage();
                response.outCurrent = rawTelemetry.outCurrent();
                response.batteryVoltage = rawTelemetry.batteryVoltage();
                response.batteryRuntime = rawTelemetry.batteryRuntime();
                response.load = rawTelemetry.load();
                response.temperature = rawTelemetry.temperature();
                response.inFrequency = rawTelemetry.inFrequency();
                response.inVoltage = rawTelemetry.inVoltage();
                response.powerFactor = rawTelemetry.powerFactor();
                response.batteryCharge = rawTelemetry.batteryCharge();
                response.statuses = rawTelemetry.statuses();
                response.connectivityStatus = rawTelemetry.connectivityStatus();
                response.telemetryStatus = rawTelemetry.telemetryStatus();
                yield response;
            }
        };
        return Result.success(telemetry);
    }

}
