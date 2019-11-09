package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.Output;
import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

public class OutputModule extends Module {
  public final String name;
  private final Output output;

  public final InputConnection enable;
  public final InputConnection input;
  public final InputConnection clock;

  public OutputModule(final String name, final int size) {
    super(size);

    this.name = name;

    this.output = new Output(size);
    this.enable = this.getTransceiver().enable;
    this.input = this.output.load;
    this.clock = this.output.clock;

    this.getTransceiver().dir.connectTo(Pins.VCC);

    this.output.in.connectTo(this.getTransceiver().out(Transceiver.Side.B));
  }

  public void clear() {
    this.output.clear();
  }
}
