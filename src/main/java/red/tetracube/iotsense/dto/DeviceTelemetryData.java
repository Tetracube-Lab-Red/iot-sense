package red.tetracube.iotsense.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import red.tetracube.iotsense.enumerations.ConnectivityStatus;
import red.tetracube.iotsense.enumerations.TelemetryStatus;
import red.tetracube.iotsense.enumerations.UPSStatus;

import java.time.Instant;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "telemetryType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceTelemetryData.UPSTelemetryData.class, name = "UPS_TELEMETRY_DATA")
})
public abstract class DeviceTelemetryData {

    @JsonProperty
    public String deviceSlug;

    @JsonProperty
    public Instant telemetryTS;

    @JsonProperty
    public ConnectivityStatus connectivityStatus;

    @JsonProperty
    public TelemetryStatus telemetryStatus;

    public static class UPSTelemetryData extends DeviceTelemetryData {
        public float outFrequency;
        public float outVoltage;
        public float outCurrent;
        public float batteryVoltage;
        public Long batteryRuntime;
        public Long load;
        public float temperature;
        public float inFrequency;
        public float inVoltage;
        public float powerFactor;
        public float batteryCharge;
        public List<UPSStatus> statuses;
    }

}
