package loficomputers.arduinoprogrammer.programmers;

import loficomputers.arduinoprogrammer.compiler.Program;
import loficomputers.arduinoprogrammer.emulator.Emulator;

import java.util.Map;
import java.util.function.IntSupplier;

public class EmulatorProgrammer implements Programmer {
  private final Emulator emulator;

  public EmulatorProgrammer(final Emulator emulator) {
    this.emulator = emulator;
  }

  @Override
  public void program(final Program program) {
    for(final Map.Entry<Integer, IntSupplier> entry : program.instructions.entrySet()) {
      final int address = entry.getKey();
      final IntSupplier instruction = entry.getValue();

      this.emulator.ram.set(address, instruction.getAsInt());
    }
  }

  @Override
  public void close() {

  }
}
