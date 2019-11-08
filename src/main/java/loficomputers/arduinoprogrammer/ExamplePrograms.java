package loficomputers.arduinoprogrammer;

import loficomputers.arduinoprogrammer.compiler.Compiler;
import loficomputers.arduinoprogrammer.compiler.Program;
import loficomputers.arduinoprogrammer.programmers.ArduinoProgrammer;
import loficomputers.arduinoprogrammer.programmers.Programmer;

public final class ExamplePrograms {
  private ExamplePrograms() { }

  public static void main(final String[] args) {
    final Program count = Compiler.compile(ExamplePrograms::count);

    try(final Programmer programmer = new ArduinoProgrammer("COM3")) {
      programmer.program(count);
    } catch(final Exception e) {
      e.printStackTrace();
    }
  }

  private static void count(final Compiler programmer) {
    programmer
      .mark("loop")                   // Mark the start of the loop
      .set(Instruction.LDA, "varA")   // Load variable "varA" into register A
      .set(Instruction.OUTA)          // Output the value
      .set(Instruction.ADDC, 1)       // Add 1 to register A
      .set(Instruction.STA, "varA")   // Store the value of register A into variable "varA"
      .set(Instruction.JC, "exit")    // Jump to the "exit" mark if the value rolls over from 255 to 0
      .set(Instruction.JMP, "loop")   // Jump back up to the "loop" mark

      .mark("exit")                   // Mark the exit point
      .set(Instruction.HLT)           // Halt execution

      .mark("varA")                   // Mark variable "varA"
      .set(0)                         // Initialize "varA" to 0
    ;
  }

  private static void fibonacci(final Compiler programmer) {
    programmer
      .set(Instruction.LDA, "varA")       // Load and display the first two numbers
      .set(Instruction.OUTA)              //
      .set(Instruction.LDA, "varB")       //
      .set(Instruction.OUTA)              //

      .mark("loop")
      .set(Instruction.LDA, "varA")       // Load "varA" into register A
      .set(Instruction.ADD, "varB")       // Load "varB" into register B, load sum (regA+regB) into register A
      .set(Instruction.JC, "overflow")    // Jump out of the loop if we overflow
      .set(Instruction.OUTA)              // Display the value in register A
      .set(Instruction.STB, "varA")       // Store register B in "varA"
      .set(Instruction.STA, "varB")       // Store register A in "varB"
      .set(Instruction.JMP, "loop")       // Jump back up to the "loop" mark

      .mark("overflow")
      .set(Instruction.HLT)               // Stop execution

      .mark("varA")
      .set(0)                             // Initialize "varA"

      .mark("varB")
      .set(1)                             // Initialize "varB"
    ;
  }
}
