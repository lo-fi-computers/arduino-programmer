package loficomputers.arduinoprogrammer.emulator.cpu;

public class InstructionNotImplementedException extends RuntimeException {
  public InstructionNotImplementedException(final String message) {
    super(message);
  }

  public InstructionNotImplementedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected InstructionNotImplementedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
