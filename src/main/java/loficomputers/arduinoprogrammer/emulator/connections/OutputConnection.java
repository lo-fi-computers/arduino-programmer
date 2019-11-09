package loficomputers.arduinoprogrammer.emulator.connections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Consumer;

public class OutputConnection extends Connection {
  private static boolean enableStateCallbacks = true;

  public static void enableStateCallbacks() {
    enableStateCallbacks = true;
  }

  public static void disableStateCallbacks() {
    enableStateCallbacks = false;
  }

  public static OutputConnection widen(final int size, final OutputConnection source) {
    return new OutputWidenerConnection(size, source);
  }

  public static OutputConnection combine(final OutputConnection... sources) {
    return new OutputCombinerConnection(sources);
  }

  public static OutputConnection[] split(final OutputConnection source) {
    final OutputConnection[] connections = new OutputConnection[source.size];

    for(int bit = 0; bit < source.size; bit++) {
      connections[bit] = new OutputSplitterConnection(source, bit);
    }

    return connections;
  }

  private final Map<InputConnection, Consumer<OptionalInt>> onStateChange = new LinkedHashMap<>();

  private final int max;
  private int value;
  private boolean disabled;

  public OutputConnection(final int size) {
    super(size);
    this.max = (int)Math.pow(2, size);
  }

  public OutputConnection onStateChange(final InputConnection connection, final Consumer<OptionalInt> onStateChange) {
    this.onStateChange.put(connection, onStateChange);

    if(enableStateCallbacks) {
      onStateChange.accept(this.getValue());
    }

    return this;
  }

  @Override
  public OptionalInt getValue() {
    if(this.disabled) {
      return OptionalInt.empty();
    }

    return OptionalInt.of(this.value);
  }

  public OutputConnection disable() {
    this.disabled = true;

    if(enableStateCallbacks) {
      this.onStateChange.forEach((pin, callback) -> callback.accept(this.getValue()));
    }

    return this;
  }

  public OutputConnection enable() {
    this.disabled = false;

    if(enableStateCallbacks) {
      this.onStateChange.forEach((pin, callback) -> callback.accept(this.getValue()));
    }

    return this;
  }

  public OutputConnection setValue(final int value) {
    if(value > this.max || value < 0) {
      throw new InvalidConnectionValueException("Attempted to set value to " + value);
    }

    this.value = value;

    if(!this.disabled && enableStateCallbacks) {
      this.onStateChange.forEach((pin, callback) -> callback.accept(this.getValue()));
    }

    return this;
  }

  @Override
  public String toString() {
    if(!this.getValue().isPresent()) {
      throw new FloatingConnectionException("Output connection is floating");
    }

    return "Output [" + this.value + " (" + Pins.toBits(this) + ")]";
  }

  public static class OutputWidenerConnection extends OutputConnection {
    private final OutputConnection source;

    public OutputWidenerConnection(final int size, final OutputConnection source) {
      super(size);

      if(source.size >= size) {
        throw new ConnectionMismatchException("Source size must be smaller than widened size");
      }

      this.source = source;
    }

    @Override
    public OutputConnection onStateChange(final InputConnection connection, final Consumer<OptionalInt> onStateChange) {
      this.source.onStateChange(connection, onStateChange);
      return this;
    }

    @Override
    public OptionalInt getValue() {
      if(!this.source.getValue().isPresent()) {
        return OptionalInt.empty();
      }

      int value = 0;

      for(int newBit = 0; newBit < this.size; newBit++) {
        final int oldBit = newBit % this.source.size;

        value |= (this.source.value >> oldBit & 0b1) << newBit;
      }

      return OptionalInt.of(value);
    }

    @Override
    public OutputConnection disable() {
      throw new RuntimeException("Can't disable widened pin");
    }

    @Override
    public OutputConnection enable() {
      throw new RuntimeException("Can't enable widened pin");
    }

    @Override
    public OutputConnection setValue(final int value) {
      throw new RuntimeException("Can't set value of widened pin");
    }
  }

  public static class OutputCombinerConnection extends OutputConnection {
    private final OutputConnection[] sources;

    public OutputCombinerConnection(final OutputConnection... sources) {
      super(sources.length);

      int size = 0;

      for(final OutputConnection source : sources) {
        size += source.size;
      }

      if(size != this.size) {
        throw new ConnectionMismatchException("Sum of source sizes (" + size + ") must equal new size (" + this.size + ')');
      }

      this.sources = sources;
    }

    @Override
    public OutputConnection onStateChange(final InputConnection connection, final Consumer<OptionalInt> onStateChange) {
      for(final OutputConnection source : this.sources) {
        source.onStateChange(connection, onStateChange);
      }

      return this;
    }

    @Override
    public OptionalInt getValue() {
      int value = 0;
      int newBitIndex = 0;

      for(final OutputConnection source : this.sources) {
        if(!source.getValue().isPresent()) {
          return OptionalInt.empty();
        }

        for(int oldBitIndex = 0; oldBitIndex < source.size; oldBitIndex++) {
          value |= (source.value >> oldBitIndex & 0b1) << newBitIndex;
          newBitIndex++;
        }
      }

      return OptionalInt.of(value);
    }

    @Override
    public OutputConnection disable() {
      throw new RuntimeException("Can't disable combined connection");
    }

    @Override
    public OutputConnection enable() {
      throw new RuntimeException("Can't enable combined connection");
    }

    @Override
    public OutputConnection setValue(final int value) {
      throw new RuntimeException("Can't set value of combined connection");
    }
  }

  public static class OutputSplitterConnection extends OutputConnection {
    private final OutputConnection source;
    private final int bit;

    public OutputSplitterConnection(final OutputConnection source, final int bit) {
      super(1);
      this.source = source;
      this.bit = bit;
    }

    @Override
    public OutputConnection onStateChange(final InputConnection connection, final Consumer<OptionalInt> onStateChange) {
      this.source.onStateChange(connection, onStateChange);
      return this;
    }

    @Override
    public OptionalInt getValue() {
      if(!this.source.getValue().isPresent()) {
        return OptionalInt.empty();
      }

      return OptionalInt.of(this.source.value >> this.bit & 0b1);
    }

    @Override
    public OutputConnection disable() {
      throw new RuntimeException("Can't disable combined connection");
    }

    @Override
    public OutputConnection enable() {
      throw new RuntimeException("Can't enable combined connection");
    }

    @Override
    public OutputConnection setValue(final int value) {
      throw new RuntimeException("Can't set value of combined connection");
    }
  }
}
