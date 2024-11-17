package red.tetracube.iotsense.modules.ups.dto;


import red.tetracube.iotsense.enumerations.ConnectivityStatus;
import red.tetracube.iotsense.enumerations.TelemetryStatus;
import red.tetracube.iotsense.enumerations.UPSStatus;

import java.time.Instant;
import java.util.List;

public record UPSBasicTelemetryData(
        String deviceInternalName,
        List<UPSStatus> statuses,
        ConnectivityStatus connectivityStatus,
        TelemetryStatus telemetryStatus,
        Instant telemetryTS
) {
}
