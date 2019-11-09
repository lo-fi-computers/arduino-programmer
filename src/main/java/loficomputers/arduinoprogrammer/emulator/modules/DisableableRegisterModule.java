package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.And;
import loficomputers.arduinoprogrammer.emulator.components.Not;
import loficomputers.arduinoprogrammer.emulator.components.Register;
import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

public class DisableableRegisterModule extends Module {
  public final String name;
  private final Register register;
  private final And and;
  private final Not not;

  public final InputConnection enable;
  public final InputConnection clock;
  public final InputConnection input;
  public final InputConnection disable;
  public final OutputConnection out;

  public DisableableRegisterModule(final String name, final int size) {
    super(size);

    this.name = name;

    this.not = new Not(size);

    this.register = new Register(size);
    this.enable = this.getTransceiver().enable;
    this.clock = this.register.clock;
    this.input = InputConnection.aggregate(1, this.register.load, this.getTransceiver().dir);
    this.disable = InputConnection.shrinker(1, this.not.in);

    this.and = new And(size);
    this.and.in(0).connectTo(this.not.out);
    this.and.in(1).connectTo(this.register.out);
    this.out = this.and.out;

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
