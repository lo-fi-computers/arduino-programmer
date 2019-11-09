package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

import java.util.HashMap;
import java.util.Map;

public class Bus {
  private final Map<Transceiver, InputConnection> in = new HashMap<>();
  public final OutputConnection out;

  public final int size;

  public static Bus eightBit() {
    return new Bus(8);
  }

  public Bus(final int size) {
    this.size = size;
    this.out = new OutputConnection(size);
  }

  public void connect(final Module board) {
    final InputConnection in = new InputConnection(this.size, state -> this.updateOutput());
    in.connectTo(board.getTransceiver().out(Transceiver.Side.A));
    board.getTransceiver().in(Transceiver.Side.A).connectTo(this.out);

    this.in.put(board.getTransceiver(), in);
  }

  private void updateOutput() {
    int output = 0;

    for(final InputConnection entry : this.in.values()) {
      if(entry.getValue().isPresent()) {
        output = entry.getValue().getAsInt();
        break;
      }
    }

    if(this.out.getValue().getAsInt() != output) {
      this.out.setValue(output);
    }
  }

  @Override
  public String toString() {
    return "Bus: " + Pins.toBits(this.out);
  }
}
