package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

import java.util.EnumMap;
import java.util.Map;
import java.util.OptionalInt;

public class Transceiver {
  private final Map<Side, InputConnection> in = new EnumMap<>(Side.class);
  private final Map<Side, OutputConnection> out = new EnumMap<>(Side.class);

  /**
   * LOW = b->a, HIGH = a->b
   */
  public final InputConnection dir = new InputConnection(1, this::changeOutput);

  public final InputConnection enable = new InputConnection(1, this::changeOutput);

  public final int size;

  public Transceiver(final int size) {
    this.size = size;

    final OutputConnection aOut = new OutputConnection(size).setValue(0);
    final OutputConnection bOut = new OutputConnection(size).setValue(0);
    final InputConnection aIn = new InputConnection(size, value -> this.changeState(bOut, value));
    final InputConnection bIn = new InputConnection(size, value -> this.changeState(aOut, value));

    this.in.put(Side.A, aIn);
    this.in.put(Side.B, bIn);
    this.out.put(Side.A, aOut);
    this.out.put(Side.B, bOut);
  }

  public InputConnection in(final Side side) {
    return this.in.get(side);
  }

  public OutputConnection out(final Side side) {
    return this.out.get(side);
  }

  private void changeOutput(final OptionalInt value) {
    final OutputConnection aOut = this.out.get(Side.A);
    final OutputConnection bOut = this.out.get(Side.B);

    aOut.disable();
    bOut.disable();

    if(this.enable.getValue().orElseThrow(() -> new FloatingConnectionException("Transceiver enable is floating")) != 0) {
      if(this.dir.getValue().orElseThrow(() -> new FloatingConnectionException("Transceiver dir is floating")) != 0) {
        bOut.enable();
      } else {
        aOut.enable();
      }
    }
  }

  private void changeState(final OutputConnection out, final OptionalInt value) {
    out.setValue(value.getAsInt());
  }

  public enum Side {
    A, B
  }
}
