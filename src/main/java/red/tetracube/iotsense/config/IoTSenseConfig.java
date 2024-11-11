package red.tetracube.iotsense.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "iot-sense")
public interface IoTSenseConfig {
    ModulesConfig modules();
    MqttConfig mqtt();
}
