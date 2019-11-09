package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

import java.util.OptionalInt;

public class Nor {
  private final InputConnection[] in;
  public final OutputConnection out;

  private final int mask;

  public final int size;

  public Nor(final int bitCount, final int inputCount) {
    this.size = inputCount;

    this.in = new InputConnection[inputCount];

    for(int i = 0; i < inputCount; i++) {
      this.in[i] = new InputConnection(bitCount, this::updateOutput);
    }

    this.out = new OutputConnection(bitCount).setValue(1);

    this.mask = (int)Math.pow(2, bitCount) - 1;
  }

  public Nor(final int bitCount) {
    this(bitCount, 2);
  }

  public InputConnection in(final int input) {
    return this.in[input];
  }

  private void updateOutput(final OptionalInt value) {
    int out = 0;

    for(int i = 0; i < this.size; i++) {
      out |= this.in[i].getValue().orElseThrow(() -> new FloatingConnectionException("Nor in is floating"));
    }

    this.out.setValue(~out & this.mask);
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("Nor ~[").append(this.in[0].getValue());

    for(int i = 1; i < this.size; i++) {
      builder.append(" | ").append(this.in[i].getValue());
    }

    builder.append(" = ").append(this.out.getValue()).append(']');

    return builder.toString();
  }
}
