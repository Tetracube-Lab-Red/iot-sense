package red.tetracube.iotsense.telemetry.mappers;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import red.tetracube.iotsense.telemetry.payloads.DeviceTelemetryData;

public class DeviceTelemetryDataDeserializer extends ObjectMapperDeserializer<DeviceTelemetryData> {
    public DeviceTelemetryDataDeserializer() {
        super(DeviceTelemetryData.class);
    }
}
