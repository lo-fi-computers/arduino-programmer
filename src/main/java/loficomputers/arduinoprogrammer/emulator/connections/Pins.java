package loficomputers.arduinoprogrammer.emulator.connections;

public final class Pins {
  public static final OutputConnection VCC = new OutputConnection(1).setValue(1);
  public static final OutputConnection GND = new OutputConnection(1).setValue(0);

  private Pins() { }

  public static String toBits(final Connection connection) {
    if(!connection.getValue().isPresent()) {
      return "disconnected";
    }

    final int value = connection.getValue().getAsInt();
    final StringBuilder out = new StringBuilder(connection.size);

    for(int bit = connection.size - 1; bit >= 0; bit--) {
      out.append(value >> bit & 0b1);
    }

    return out.toString();
  }
}
