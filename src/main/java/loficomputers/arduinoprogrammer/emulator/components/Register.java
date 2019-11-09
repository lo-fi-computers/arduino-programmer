package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

import java.util.OptionalInt;

public class Register {
  public final InputConnection in;
  public final OutputConnection out;

  public final InputConnection load = new InputConnection(1);
  public final InputConnection clock = new InputConnection(1, this::onClock);

  public final int size;

  public Register(final int size) {
    this.size = size;

    this.in  = new InputConnection(size);
    this.out = new OutputConnection(size).setValue(0);
  }

  public void clear() {
    this.out.setValue(0);
  }

  private void onClock(final OptionalInt value) {
    if(value.getAsInt() != 0) {
      if(this.load.getValue().orElseThrow(() -> new FloatingConnectionException("Register load is floating")) != 0) {
        this.out.setValue(this.in.getValue().orElseThrow(() -> new FloatingConnectionException("Register in is floating")));
      }
    }
  }

  @Override
  public String toString() {
    return "Register [in -> " + Pins.toBits(this.in) + ", out -> " + Pins.toBits(this.out) + ']';
  }

  public String toBits() {
    return Pins.toBits(this.out);
  }

  public int toInt() {
    return this.out.getValue().getAsInt();
  }
}
