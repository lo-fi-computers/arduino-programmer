package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.ALU;
import loficomputers.arduinoprogrammer.emulator.components.Nor;
import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

public class ALUModule extends Module {
  public final String name;
  private final ALU alu;

  public final InputConnection a;
  public final InputConnection b;
  public final InputConnection enable;
  public final InputConnection sub;

  public final OutputConnection zero;
  public final OutputConnection carry;

  private final Nor zeroNor;

  public ALUModule(final String name, final int size) {
    super(size);

    this.name = name;

    this.alu = new ALU(size);
    this.zeroNor = new Nor(1, size);

    this.sub = this.alu.sub;

    this.a = this.alu.a;
    this.b = this.alu.b;
    this.enable = this.getTransceiver().enable;
    this.carry = this.alu.carryOut;

    this.alu.carryIn.connectTo(Pins.GND);

    int bit = 0;
    for(final OutputConnection outBit : OutputConnection.split(this.alu.out)) {
      this.zeroNor.in(bit).connectTo(outBit);
      bit++;
    }

    this.zero = this.zeroNor.out;

    this.getTransceiver().dir.connectTo(Pins.GND);
    this.getTransceiver().in(Transceiver.Side.B).connectTo(this.alu.out);
  }

  @Override
  public String toString() {
    return this.name + ": " + this.alu;
  }
}
