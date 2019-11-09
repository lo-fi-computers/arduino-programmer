package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.Register;
import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

public class RegisterModule extends Module {
  public final String name;
  private final Register register;

  public final InputConnection enable;
  public final InputConnection clock;
  public final InputConnection input;
  public final OutputConnection out;

  public RegisterModule(final String name, final int size) {
    super(size);

    this.name = name;

    this.register = new Register(size);
    this.enable = this.getTransceiver().enable;
    this.clock = this.register.clock;
    this.input = InputConnection.aggregate(1, this.register.load, this.getTransceiver().dir);
    this.out = this.register.out;

    this.getTransceiver().in(Transceiver.Side.B).connectTo(this.register.out);
    this.register.in.connectTo(this.getTransceiver().out(Transceiver.Side.B));
  }

  public void clear() {
    this.register.clear();
  }

  @Override
  public String toString() {
    return this.name + ": " + this.register;
  }
}
