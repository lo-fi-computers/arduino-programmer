package loficomputers.arduinoprogrammer.emulator.connections;

public class FloatingConnectionException extends RuntimeException {
  public FloatingConnectionException(final String message) {
    super(message);
  }

  public FloatingConnectionException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected FloatingConnectionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
