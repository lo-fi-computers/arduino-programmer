package loficomputers.arduinoprogrammer.emulator.modules;

import loficomputers.arduinoprogrammer.emulator.components.Transceiver;
import loficomputers.arduinoprogrammer.emulator.connections.Pins;

public abstract class Module {
  private final Transceiver transceiver;

  public final int size;

  protected Module(final int size) {
    this.size = size;
    this.transceiver = new Transceiver(size);
    this.transceiver.dir.connectTo(Pins.GND);
    this.transceiver.enable.connectTo(Pins.GND);
  }

  Transceiver getTransceiver() {
    return this.transceiver;
  }
}
