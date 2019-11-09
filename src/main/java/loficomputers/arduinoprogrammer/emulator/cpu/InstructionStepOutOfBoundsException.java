package loficomputers.arduinoprogrammer.emulator.cpu;

public class InstructionStepOutOfBoundsException extends RuntimeException {
  public InstructionStepOutOfBoundsException(final String message) {
    super(message);
  }

  public InstructionStepOutOfBoundsException(final String message, final Throwable cause) {
    super(message, cause);
  }

  protected InstructionStepOutOfBoundsException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
