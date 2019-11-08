package loficomputers.arduinoprogrammer.programmers;

import loficomputers.arduinoprogrammer.compiler.Program;

public interface Programmer extends AutoCloseable {
  void program(final Program program) throws Exception;
}
