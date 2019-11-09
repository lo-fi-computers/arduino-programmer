package loficomputers.arduinoprogrammer.emulator.cpu;

import loficomputers.arduinoprogrammer.Instruction;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Predicate;

public class InstructionDecoder {
  private final Map<Instruction, MicroInstruction> decoder = new EnumMap<>(Instruction.class);

  public InstructionDecoder() {
    this.decoder.put(Instruction.NOOP, op()
      .step(ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.LDA, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.RAM_EN, ControlSignal.FLAGS_IN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.LDB, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.B_EN, ControlSignal.B_IN, ControlSignal.RAM_EN, ControlSignal.FLAGS_IN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.LDAC, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.RAM_EN, ControlSignal.FLAGS_IN, ControlSignal.CNT_CNT, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.LDBC, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.B_EN, ControlSignal.B_IN, ControlSignal.RAM_EN, ControlSignal.FLAGS_IN, ControlSignal.CNT_CNT, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.BNK, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.BNK_EN, ControlSignal.BNK_IN, ControlSignal.RAM_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.BNKC, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.BNK_EN, ControlSignal.BNK_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.STA, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.A_EN, ControlSignal.RAM_EN, ControlSignal.RAM_IN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.STB, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.B_EN, ControlSignal.RAM_EN, ControlSignal.RAM_IN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.ADD, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.B_EN, ControlSignal.B_IN, ControlSignal.RAM_EN, ControlSignal.FLAGS_IN)
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.ALU_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.ADDC, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.B_EN, ControlSignal.B_IN, ControlSignal.RAM_EN, ControlSignal.FLAGS_IN, ControlSignal.CNT_CNT)
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.ALU_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.ADDI, op()
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.ALU_EN, ControlSignal.FLAGS_IN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.SUB, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.RAM_EN, ControlSignal.CNT_CNT)
      .step(ControlSignal.B_EN, ControlSignal.B_IN, ControlSignal.RAM_EN, ControlSignal.ALU_SUB, ControlSignal.FLAGS_IN)
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.ALU_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.SUBC, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.B_EN, ControlSignal.B_IN, ControlSignal.RAM_EN, ControlSignal.ALU_SUB, ControlSignal.FLAGS_IN, ControlSignal.CNT_CNT)
      .step(ControlSignal.A_EN, ControlSignal.A_IN, ControlSignal.ALU_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.JMP, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(ControlSignal.RAM_EN, ControlSignal.CNT_EN, ControlSignal.CNT_IN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.JC, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(cpu -> cpu.flagCarry.getValue().getAsInt() != 0, ControlSignal.RAM_EN, ControlSignal.CNT_EN, ControlSignal.CNT_IN)
      .step(cpu -> cpu.flagCarry.getValue().getAsInt() == 0, ControlSignal.CNT_CNT, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.JZ, op()
      .step(ControlSignal.ADD_EN, ControlSignal.ADD_IN, ControlSignal.CNT_EN)
      .step(cpu -> cpu.flagZero.getValue().getAsInt() != 0, ControlSignal.RAM_EN, ControlSignal.CNT_EN, ControlSignal.CNT_IN)
      .step(cpu -> cpu.flagZero.getValue().getAsInt() == 0, ControlSignal.CNT_CNT, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.OUTA, op()
      .step(ControlSignal.OUT_EN, ControlSignal.OUT_IN, ControlSignal.A_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.OUTB, op()
      .step(ControlSignal.OUT_EN, ControlSignal.OUT_IN, ControlSignal.B_EN, ControlSignal.STEP_RESET).build()
    );
    this.decoder.put(Instruction.HLT, op()
      .step(ControlSignal.HALT, ControlSignal.STEP_RESET).build()
    );
  }

  public MicroInstruction decode(final Instruction instruction) {
    final MicroInstruction decoded = this.decoder.get(instruction);

    if(decoded == null) {
      throw new InstructionNotImplementedException("Instruction " + instruction + " is not yet implemented");
    }

    return decoded;
  }

  private static MicroInstructionBuilder op() {
    return new MicroInstructionBuilder();
  }

  public static final class MicroInstruction {
    private final Predicate<CPU>[] conditions;
    private final ControlSignal[][] controls;

    private MicroInstruction(final MicroInstructionBuilder builder) {
      this.conditions = builder.conditions;
      this.controls = builder.controls;

      for(int i = 0; i < this.controls.length; i++) {
        if(this.controls[i] == null) {
          this.controls[i] = new ControlSignal[0];
        }
      }
    }

    public boolean activate(final CPU cpu, final int step) {
      boolean reset = false;

      if(this.conditions[step].test(cpu)) {
        for(final ControlSignal control : this.controls[step]) {
          control.setHigh(cpu);

          if(control == ControlSignal.STEP_RESET) {
            reset = true;
          }
        }
      }

      return reset;
    }
  }

  private static final class MicroInstructionBuilder {
    private final Predicate<CPU>[] conditions = new Predicate[8];
    private final ControlSignal[][] controls = new ControlSignal[8][];
    private int count;

    private MicroInstructionBuilder() {
      Arrays.fill(this.conditions, (Predicate<CPU>)cpu -> true);
      this.step(ControlSignal.CNT_EN, ControlSignal.ADD_EN, ControlSignal.ADD_IN);
      this.step(ControlSignal.BNK_DIS, ControlSignal.RAM_EN, ControlSignal.INST_EN, ControlSignal.INST_IN, ControlSignal.CNT_CNT);
    }

    private MicroInstructionBuilder step(final Predicate<CPU> condition, final ControlSignal... controls) {
      this.conditions[this.count] = condition;
      this.controls[this.count] = controls;
      this.count++;
      return this;
    }

    private MicroInstructionBuilder step(final ControlSignal... controls) {
      return this.step(cpu -> true, controls);
    }

    private MicroInstruction build() {
      return new MicroInstruction(this);
    }
  }
}
