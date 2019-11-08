package loficomputers.arduinoprogrammer.compiler;

import java.util.Collections;
import java.util.Map;
import java.util.function.IntSupplier;

public class Program {
  public final Map<Integer, IntSupplier> instructions;
  public final Map<String, Integer> marks;

  public Program(final Map<Integer, IntSupplier> instructions, final Map<String, Integer> marks) {
    this.instructions = Collections.unmodifiableMap(instructions);
    this.marks = Collections.unmodifiableMap(marks);
  }
}
