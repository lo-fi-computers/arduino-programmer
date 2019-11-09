package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.RAM;
import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;

public class RAMModule extends Module {
  public final String name;
  private final RAM ram;

  public final InputConnection enable;
  public final InputConnection clock;
  public final InputConnection input;
  public final InputConnection bank;
  public final InputConnection address;

  public RAMModule(final String name, final int size) {
    super(size);

    this.name = name;

    this.ram = new RAM(size);
    this.enable = this.getTransceiver().enable;
    this.clock = this.ram.clock;
    this.input = InputConnection.aggregate(1, this.ram.load, this.getTransceiver().dir);
    this.bank = this.ram.bank;
    this.address = this.ram.address;

    this.getTransceiver().in(Transceiver.Side.B).connectTo(this.ram.out);
    this.ram.in.connectTo(this.getTransceiver().out(Transceiver.Side.B));
  }

  public void set(final int address, final int value) {
    this.ram.set(address, value);
  }

  public int get(final int address) {
    return this.ram.get(address);
  }

  public void clear() {
    this.ram.clear();
  }

  @Override
  public String toString() {
    return this.name + ": " + this.ram;
  }
}
