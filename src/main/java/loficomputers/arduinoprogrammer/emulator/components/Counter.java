package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

import java.util.OptionalInt;

public class Counter {
  public final InputConnection in;
  public final OutputConnection out;

  public final InputConnection load = new InputConnection(1);
  public final InputConnection clock = new InputConnection(1, this::onClock);
  public final InputConnection count = new InputConnection(1);

  public final int size;

  private int value;

  public Counter(final int size) {
    this.size = size;

    this.in = new InputConnection(size);
    this.out = new OutputConnection(size).setValue(0);
  }

  public void clear() {
    this.value = 0;
    this.updateOutput();
  }

  private void onClock(final OptionalInt value) {
    if(value.getAsInt() != 0) {
      if(this.count.getValue().orElseThrow(() -> new FloatingConnectionException("Counter count is floating")) != 0) {
        this.value++;
        this.updateOutput();
      }

      if(this.load.getValue().orElseThrow(() -> new FloatingConnectionException("Counter load is floating")) != 0) {
        if(!this.in.getValue().isPresent()) {
          throw new FloatingConnectionException("Counter in is floating");
        }

        this.value = this.in.getValue().orElseThrow(() -> new FloatingConnectionException("Counter in is floating"));
        this.updateOutput();
      }
    }
  }

  private void updateOutput() {
    this.out.setValue(this.value);
  }

  @Override
  public String toString() {
    return "Counter [" + this.out.getValue() + ']';
  }

  public String toBits() {
    return Pins.toBits(this.out);
  }

  public int toInt() {
    return this.out.getValue().getAsInt();
  }
}
