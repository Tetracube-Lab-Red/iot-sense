package red.tetracube.iotsense.dto.exceptions;

public sealed class IoTSenseException extends Exception permits
        IoTSenseException.EntityExistsException,
        IoTSenseException.EntityNotFoundException,
        IoTSenseException.RepositoryException,
        IoTSenseException.UnauthorizedException {

    public static final class RepositoryException extends IoTSenseException {
        public RepositoryException(Throwable throwable) {
            super(throwable);
        }
    }

    public static final class EntityNotFoundException extends IoTSenseException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static final class EntityExistsException extends IoTSenseException {
        public EntityExistsException(String message) {
            super(message);
        }
    }

    public static final class UnauthorizedException extends IoTSenseException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    protected IoTSenseException(Throwable throwable) {
        super(throwable);
    }

    protected IoTSenseException(String message) {
        super(message);
    }

}
