package loficomputers.arduinoprogrammer.compiler;

import loficomputers.arduinoprogrammer.Instruction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class Compiler {
  public static Program compile(final Consumer<? super Compiler> compiler) {
    final Compiler c = new Compiler();
    compiler.accept(c);
    return new Program(c.instructions, c.marks);
  }

  private final Map<Integer, IntSupplier> instructions = new HashMap<>();
  private final Map<String, Integer> marks = new HashMap<>();
  private int address;

  public Compiler set(final Instruction instruction, final int... values) {
    this.set(instruction.ordinal());

    for(final int value : values) {
      this.set(value);
    }

    return this;
  }

  public Compiler set(final Instruction instruction, final String label) {
    this.set(instruction.ordinal());
    this.instructions.put(this.address++, () -> this.marks.get(label));
    return this;
  }

  public Compiler set(final int value) {
    this.instructions.put(this.address++, () -> value);
    return this;
  }

  public Compiler mark(final String label) {
    this.marks.put(label, this.address);
    return this;
  }

  public Compiler move(final int address) {
    this.address = address;
    return this;
  }
}
