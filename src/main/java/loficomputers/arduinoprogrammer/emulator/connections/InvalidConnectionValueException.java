package loficomputers.arduinoprogrammer.emulator.connections;

public class InvalidConnectionValueException extends RuntimeException {
  public InvalidConnectionValueException(final String message) {
    super(message);
  }

  public InvalidConnectionValueException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected InvalidConnectionValueException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
