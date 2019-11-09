package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

import java.util.Arrays;
import java.util.OptionalInt;

public class RAM {
  private final int[] values;

  public final InputConnection bank;
  public final InputConnection address;

  public final InputConnection in;
  public final OutputConnection out;

  public final InputConnection load = new InputConnection(1);
  public final InputConnection clock = new InputConnection(1, this::onClock);

  public final int size;

  public RAM(final int size) {
    this.size = size;

    final int addressCount = (int)Math.pow(2, size * 2);

    this.bank = new InputConnection(size);
    this.address = new InputConnection(size);

    this.values = new int[addressCount];

    this.in = new InputConnection(size);
    this.out = new OutputConnection(size).setValue(0);
  }

  public void set(final int address, final int value) {
    this.values[address] = value;
  }

  public int get(final int address) {
    return this.values[address];
  }

  public void clear() {
    Arrays.fill(this.values, 0);
    this.out.setValue(0);
  }

  private void onClock(final OptionalInt value) {
    if(value.getAsInt() == 0) {
      return;
    }

    final int address = this.bank.getValue().orElseThrow(() -> new FloatingConnectionException("RAM bank is floating")) << this.size | this.address.getValue().orElseThrow(() -> new FloatingConnectionException("RAM address is floating"));

    if(this.load.getValue().orElseThrow(() -> new FloatingConnectionException("RAM load is floating")) != 0) {
      this.values[address] = this.in.getValue().orElseThrow(() -> new FloatingConnectionException("RAM in is floating"));
    }

    this.out.setValue(this.values[address]);
  }

  @Override
  public String toString() {
    return "RAM: @" + Pins.toBits(this.bank) + Pins.toBits(this.address) + ": " + this.values[this.address.getValue().getAsInt()];
  }
}
