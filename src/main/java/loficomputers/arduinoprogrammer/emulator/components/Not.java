package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

import java.util.OptionalInt;

public class Not {
  public final InputConnection in;
  public final OutputConnection out;

  private final int mask;

  public Not(final int size) {
    this.in = new InputConnection(size, this::onStateChange);
    this.out = new OutputConnection(size).setValue(0);

    this.mask = (int)Math.pow(2, size) - 1;
  }

  private void onStateChange(final OptionalInt value) {
    this.out.setValue(~value.orElseThrow(() -> new FloatingConnectionException("Not in is floating")) & this.mask);
  }

  @Override
  public String toString() {
    return "Not [" + this.in.getValue() + " ~ " + this.out.getValue() + ']';
  }
}
