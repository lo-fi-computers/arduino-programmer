package loficomputers.arduinoprogrammer.programmers;

import loficomputers.arduinoprogrammer.compiler.Program;
import org.ardulink.core.Link;
import org.ardulink.core.Pin;
import org.ardulink.core.convenience.Links;
import org.ardulink.util.URIs;

import java.io.IOException;
import java.util.Map;
import java.util.function.IntSupplier;

public class ArduinoProgrammer implements Programmer {
  private final Link link;
  private final Pin.DigitalPin shiftData = Pin.digitalPin(2);
  private final Pin.DigitalPin shiftClock = Pin.digitalPin(3);
  private final Pin.DigitalPin shiftLatch = Pin.digitalPin(4);
  private final Pin.DigitalPin busOut = Pin.digitalPin(5);
  private final Pin.DigitalPin addressWrite = Pin.digitalPin(6);
  private final Pin.DigitalPin ramWrite = Pin.digitalPin(7);
  private final Pin.DigitalPin clock = Pin.digitalPin(8);
  private final Pin.DigitalPin disable = Pin.digitalPin(9);

  public ArduinoProgrammer(final String port) throws IOException {
    this.link = Links.getLink(URIs.newURI("ardulink://serial-jssc?port=" + port + "&baudrate=115200&pingprobe=false&waitsecs=2"));
    this.disableOutput();
    this.enableControlLogic();
  }

  @Override
  public void program(final Program program) throws IOException {
    this.disableControlLogic();

    for(final Map.Entry<Integer, IntSupplier> entry : program.instructions.entrySet()) {
      final int address = entry.getKey();
      final IntSupplier instruction = entry.getValue();

      this.setAddress(address);
      this.writeToRam(instruction.getAsInt());
    }

    this.enableControlLogic();
  }

  private void setAddress(final int address) throws IOException {
    this.outputData(address);
    this.link.switchDigitalPin(this.addressWrite, true);
    this.pulseClock();
    this.link.switchDigitalPin(this.addressWrite, false);
    this.disableOutput();
  }

  private void writeToRam(final int value) throws IOException {
    this.outputData(value);
    this.pulsePin(this.ramWrite);
    this.disableOutput();
  }

  private void outputData(final int data) throws IOException {
    this.shiftOut(data);
    this.enableOutput();
  }

  private void enableOutput() throws IOException {
    this.link.switchDigitalPin(this.busOut, false);
    this.pause();
  }

  private void disableOutput() throws IOException {
    this.link.switchDigitalPin(this.busOut, true);
    this.pause();
  }

  private void pulseClock() throws IOException {
    this.pulsePin(this.clock);
  }

  private void enableControlLogic() throws IOException {
    this.link.switchDigitalPin(this.disable, false);
    this.pause();
  }

  private void disableControlLogic() throws IOException {
    this.link.switchDigitalPin(this.disable, true);
    this.pause();
  }

  private void pulsePin(final Pin.DigitalPin pin) throws IOException {
    this.link.switchDigitalPin(pin, true);
    this.pause();
    this.link.switchDigitalPin(pin, false);
    this.pause();
  }

  private void shiftOut(final int data) throws IOException {
    if(data < 0 || data > 255) {
      throw new ValueOutOfRangeException("Shift data must be between 0 and 255 inclusive (got " + data + ')');
    }

    for(int i = 0; i < 8; i++) {
      this.link.switchDigitalPin(this.shiftData, (data >> i & 1) == 1);
      this.link.switchDigitalPin(this.shiftClock, true);
      this.link.switchDigitalPin(this.shiftClock, false);
    }

    this.pulsePin(this.shiftLatch);
  }

  private void pause() {
    try {
      Thread.sleep(100);
    } catch(final InterruptedException ignored) { }
  }

  @Override
  public void close() throws IOException {
    this.link.close();
  }
}
