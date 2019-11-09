package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

import java.util.OptionalInt;

public class ALU {
  public final InputConnection a;
  public final InputConnection b;
  public final InputConnection sub;
  public final OutputConnection out;

  public final InputConnection carryIn;
  public final OutputConnection carryOut;

  public final int size;
  private final int max;

  public ALU(final int size) {
    this.size = size;
    this.max = (int)Math.pow(2, size) - 1;

    this.a = new InputConnection(size, this::onInputChanged);
    this.b = new InputConnection(size, this::onInputChanged);
    this.sub = new InputConnection(1);
    this.out = new OutputConnection(size).setValue(0);

    this.carryIn = new InputConnection(1, this::onInputChanged);
    this.carryOut = new OutputConnection(1).setValue(0);
  }

  private void onInputChanged(final OptionalInt value) {
    final boolean subtract = this.sub.getValue().orElseThrow(() -> new RuntimeException("ALU sub was floating")) != 0;
    final int a = this.a.getValue().orElseThrow(() -> new RuntimeException("ALU a was floating"));
    final int b = this.b.getValue().orElseThrow(() -> new RuntimeException("ALU b was floating"));
    final int carryIn = this.carryIn.getValue().orElseThrow(() -> new RuntimeException("ALU carry in was floating"));

    final int sum = a + (subtract ? -b : b) + carryIn;

    if(sum > this.max) {
      this.carryOut.setValue(1);
    } else {
      this.carryOut.setValue(0);
    }

    this.out.setValue(Math.floorMod(sum, this.max + 1));
  }

  @Override
  public String toString() {
    return "ALU: [" + Pins.toBits(this.a) + " + " + Pins.toBits(this.b) + " + " + Pins.toBits(this.carryIn) + " = " + Pins.toBits(this.out) + ']';
  }

  public String toBits() {
    return Pins.toBits(this.out);
  }

  public int toInt() {
    return this.out.getValue().getAsInt();
  }
}
