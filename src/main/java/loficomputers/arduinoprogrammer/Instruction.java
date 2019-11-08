package loficomputers.arduinoprogrammer;

public enum Instruction {
  /** No-op */
  NOOP(0b00000000),
  /** Load a value from RAM into the A register */
  LDA (0b00000001),
  /** Load a value from RAM into the B register */
  LDB (0b00000010),
  /** Load a constant into the A register */
  LDAC(0b00000011),
  /** Load a constant into the B register */
  LDBC(0b00000100),
  /** Load a value from RAM into the bank register */
  BNK (0b00000101),
  /** Load a constant into the bank register */
  BNKC(0b00000110),
  /** Store the value of the A register into RAM */
  STA (0b00000111),
  /** Store the value of the B register into RAM */
  STB (0b00001000),
  /** Load a value from RAM into the B register, sum it with the A register, and load it into the A register */
  ADD (0b00001001),
  /** Load a constant into the B register, sum it with the A register, and load it into the A register */
  ADDC(0b00001010),
  /** Load a constant into the A register, another constant into the B register, sum them, and load it into the A register */
  AD2C(0b00001011),
  /** Sum the values of the A and B registers, and load it into the A register */
  ADDI(0b00001100),
  /** Load a value from RAM into the B register, subtract it from the A register, and load it into the A register */
  SUB (0b00001101),
  /** Load a constant into the B register, subtract it from the A register, and load it into the A register */
  SUBC(0b00001110),
  /** Load a constant into the A register, another constant into the B register, subtract B from A, and load it into the A register */
  SB2C(0b00001111),
  /** Subtract the value of the B register from the A register, and load it into the A register */
  SUBI(0b00010000),
  /** Jump to a different address */
  JMP (0b00010001),
  /** Jump to a different address if the carry flag is set */
  JC  (0b00010010),
  /** Jump to a different address if the zero flag is set */
  JZ  (0b00010011),
  /** Put the computer in signed mode */
  SGN (0b00010100),
  /** Put the computer in unsigned mode */
  USGN(0b00010101),
  /** Output the value of the A register */
  OUTA(0b00010110),
  /** Output the value of the B register */
  OUTB(0b00010111),
  /** Halt execution */
  HLT (0b00011000),
  ;

  public final int opcode;

  Instruction(final int opcode) {
    this.opcode = opcode;
  }
}
