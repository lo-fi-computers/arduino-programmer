package loficomputers.arduinoprogrammer;

public class UnknownInstructionException extends RuntimeException {
  public UnknownInstructionException(final String message) {
    super(message);
  }

  public UnknownInstructionException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected UnknownInstructionException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
