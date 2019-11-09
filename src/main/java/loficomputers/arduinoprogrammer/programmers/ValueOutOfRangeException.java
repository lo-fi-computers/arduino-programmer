package loficomputers.arduinoprogrammer.programmers;

public class ValueOutOfRangeException extends RuntimeException {
  public ValueOutOfRangeException(final String message) {
    super(message);
  }

  public ValueOutOfRangeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected ValueOutOfRangeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
