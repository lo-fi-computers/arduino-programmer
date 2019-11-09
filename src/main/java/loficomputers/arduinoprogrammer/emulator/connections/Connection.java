package loficomputers.arduinoprogrammer.emulator.connections;

import java.util.OptionalInt;

public abstract class Connection {
  public final int size;

  protected Connection(final int size) {
    this.size = size;
  }

  public abstract OptionalInt getValue();
}
