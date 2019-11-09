package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.Counter;
import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;

public class CounterModule extends Module {
  public final String name;
  private final Counter counter;

  public final InputConnection enable;
  public final InputConnection clock;
  public final InputConnection input;
  public final InputConnection count;

  public CounterModule(final String name, final int size) {
    super(size);

    this.name = name;

    this.counter = new Counter(size);
    this.enable = this.getTransceiver().enable;
    this.clock = this.counter.clock;
    this.count = this.counter.count;
    this.input = InputConnection.aggregate(1, this.counter.load, this.getTransceiver().dir);

    this.getTransceiver().in(Transceiver.Side.B).connectTo(this.counter.out);
    this.counter.in.connectTo(this.getTransceiver().out(Transceiver.Side.B));
  }

  public void clear() {
    this.counter.clear();
  }

  @Override
  public String toString() {
    return this.name + ": " + this.counter;
  }
}
