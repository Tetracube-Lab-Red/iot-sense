package red.tetracube.iotsense.dto.broker;

public record DeviceUPSProvisioning(
        String nutAddress,
        Integer nutPort,
        String nutAlias
) {
}
