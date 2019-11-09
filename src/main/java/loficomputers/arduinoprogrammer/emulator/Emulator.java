package loficomputers.arduinoprogrammer.emulator;

import loficomputers.arduinoprogrammer.emulator.components.Clock;
import loficomputers.arduinoprogrammer.emulator.components.Register;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;
import loficomputers.arduinoprogrammer.emulator.cpu.CPU;
import loficomputers.arduinoprogrammer.emulator.modules.ALUModule;
import loficomputers.arduinoprogrammer.emulator.modules.Bus;
import loficomputers.arduinoprogrammer.emulator.modules.CounterModule;
import loficomputers.arduinoprogrammer.emulator.modules.DisableableRegisterModule;
import loficomputers.arduinoprogrammer.emulator.modules.OutputModule;
import loficomputers.arduinoprogrammer.emulator.modules.RAMModule;
import loficomputers.arduinoprogrammer.emulator.modules.RegisterModule;

public class Emulator {
  private final Clock clock;
  public final RAMModule ram;

  public Emulator(final int clockRate) {
    OutputConnection.disableStateCallbacks();

    final Bus bus = Bus.eightBit();

    // CLOCK SETUP
    this.clock = new Clock(clockRate);

    final CPU cpu = new CPU(8);
    cpu.clock.connectTo(this.clock.out);

    // A REGISTER SETUP
    final RegisterModule registerA = new RegisterModule("A register", 8);
    registerA.clock.connectTo(this.clock.out);

    bus.connect(registerA);

    // B REGISTER SETUP
    final RegisterModule registerB = new RegisterModule("B register", 8);
    registerB.clock.connectTo(this.clock.out);

    bus.connect(registerB);

    // ALU SETUP
    final ALUModule alu = new ALUModule("ALU", 8);

    alu.a.connectTo(registerA.out);
    alu.b.connectTo(registerB.out);

    bus.connect(alu);

    // OUTPUT SETUP
    final OutputModule output = new OutputModule("Output register", 8);
    output.clock.connectTo(this.clock.out);
    bus.connect(output);

    // ADDRESS REGISTER
    final RegisterModule address = new RegisterModule("Address register", 8);
    address.clock.connectTo(this.clock.out);
    bus.connect(address);

    // BANK REGISTER
    final DisableableRegisterModule bank = new DisableableRegisterModule("Bank register", 8);
    bank.clock.connectTo(this.clock.out);
    bus.connect(bank);

    // RAM SETUP
    this.ram = new RAMModule("RAM", 8);
    this.ram.clock.connectTo(this.clock.out);
    bus.connect(this.ram);

    this.ram.bank.connectTo(bank.out);
    this.ram.address.connectTo(address.out);

    // PROGRAM COUNTER
    final CounterModule counter = new CounterModule("Program counter", 8);
    counter.clock.connectTo(this.clock.out);
    bus.connect(counter);

    // INSTRUCTION REGISTER
    final RegisterModule instruction = new RegisterModule("Instruction register", 8);
    instruction.clock.connectTo(this.clock.out);
    bus.connect(instruction);

    // FLAGS REGISTER
    final Register flags = new Register(2);
    flags.clock.connectTo(this.clock.out);
    flags.in.connectTo(OutputConnection.combine(alu.zero, alu.carry));

    // CPU SETUP
    this.clock.halt.connectTo(cpu.halt);
    registerA.input.connectTo(cpu.aIn);
    registerA.enable.connectTo(cpu.aEnable);
    registerB.input.connectTo(cpu.bIn);
    registerB.enable.connectTo(cpu.bEnable);
    alu.enable.connectTo(cpu.aluEnable);
    alu.sub.connectTo(cpu.aluSubtract);
    address.input.connectTo(cpu.addressIn);
    address.enable.connectTo(cpu.addressEnable);
    bank.input.connectTo(cpu.bankIn);
    bank.enable.connectTo(cpu.bankEnable);
    bank.disable.connectTo(cpu.bankDisable);
    this.ram.input.connectTo(cpu.ramIn);
    this.ram.enable.connectTo(cpu.ramEnable);
    counter.input.connectTo(cpu.countIn);
    counter.enable.connectTo(cpu.countEnable);
    counter.count.connectTo(cpu.countCount);
    instruction.input.connectTo(cpu.instructionIn);
    instruction.enable.connectTo(cpu.instructionEnable);
    output.input.connectTo(cpu.outIn);
    output.enable.connectTo(cpu.outEnable);
    flags.load.connectTo(cpu.flagsIn);

    cpu.flagZero.connectTo(new OutputConnection.OutputSplitterConnection(flags.out, 0));
    cpu.flagCarry.connectTo(new OutputConnection.OutputSplitterConnection(flags.out, 1));

    cpu.instruction.connectTo(instruction.out);

    OutputConnection.enableStateCallbacks();
  }

  public void run() {
    this.clock.run();
  }
}
