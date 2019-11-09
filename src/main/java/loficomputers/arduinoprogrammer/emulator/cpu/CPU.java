package loficomputers.arduinoprogrammer.emulator.cpu;

import loficomputers.arduinoprogrammer.Instruction;
import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

import java.util.OptionalInt;

public class CPU {
  public final OutputConnection halt = new OutputConnection(1);
  public final OutputConnection aIn = new OutputConnection(1);
  public final OutputConnection aEnable = new OutputConnection(1);
  public final OutputConnection bIn = new OutputConnection(1);
  public final OutputConnection bEnable = new OutputConnection(1);
  public final OutputConnection aluEnable = new OutputConnection(1);
  public final OutputConnection aluSubtract = new OutputConnection(1);
  public final OutputConnection addressIn = new OutputConnection(1);
  public final OutputConnection addressEnable = new OutputConnection(1);
  public final OutputConnection bankIn = new OutputConnection(1);
  public final OutputConnection bankEnable = new OutputConnection(1);
  public final OutputConnection bankDisable = new OutputConnection(1);
  public final OutputConnection ramIn = new OutputConnection(1);
  public final OutputConnection ramEnable = new OutputConnection(1);
  public final OutputConnection countIn = new OutputConnection(1);
  public final OutputConnection countEnable = new OutputConnection(1);
  public final OutputConnection countCount = new OutputConnection(1);
  public final OutputConnection instructionIn = new OutputConnection(1);
  public final OutputConnection instructionEnable = new OutputConnection(1);
  public final OutputConnection outIn = new OutputConnection(1);
  public final OutputConnection outEnable = new OutputConnection(1);
  public final OutputConnection flagsIn = new OutputConnection(1);

  public final InputConnection flagZero = new InputConnection(1);
  public final InputConnection flagCarry = new InputConnection(1);

  public final InputConnection instruction;

  private int step;

  public final InputConnection clock = new InputConnection(1, this::onClock);

  private final InstructionDecoder decoder = new InstructionDecoder();

  public CPU(final int size) {
    this.instruction = new InputConnection(size);
  }

  private void onClock(final OptionalInt value) {
    if(!value.isPresent()) {
      throw new FloatingConnectionException("CPU clock connection is floating");
    }

    if(value.getAsInt() == 0) {
      return;
    }

    if(!this.instruction.getValue().isPresent()) {
      throw new FloatingConnectionException("CPU instruction connection is floating");
    }

    final Instruction instruction = Instruction.get(this.instruction.getValue().getAsInt());
    final InstructionDecoder.MicroInstruction decoded = this.decoder.decode(instruction);

    ControlSignal.reset(this);
    if(decoded.activate(this, this.step)) {
      this.step = 0;
    } else {
      this.step = (this.step + 1) % 8;
    }
  }
}
