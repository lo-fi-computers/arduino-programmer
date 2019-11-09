package loficomputers.arduinoprogrammer.emulator.connections;

public class ConnectionMismatchException extends RuntimeException {
  public ConnectionMismatchException(final String message) {
    super(message);
  }

  public ConnectionMismatchException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected ConnectionMismatchException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
