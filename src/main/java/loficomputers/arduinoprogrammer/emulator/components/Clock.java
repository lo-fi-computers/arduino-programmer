package loficomputers.arduinoprogrammer.emulator.components;

import loficomputers.arduinoprogrammer.emulator.connections.FloatingConnectionException;
import loficomputers.arduinoprogrammer.emulator.connections.InputConnection;
import loficomputers.arduinoprogrammer.emulator.connections.OutputConnection;

import java.util.concurrent.TimeUnit;

public class Clock {
  public final OutputConnection out = new OutputConnection(1).setValue(0);
  public final InputConnection halt = new InputConnection(1);
  public final int hz;
  private final long sleep;

  public Clock(final int hz) {
    this.hz = hz;
    this.sleep = 1000000000L / hz;
  }

  public void run() {
    while(this.halt.getValue().orElseThrow(() -> new FloatingConnectionException("Clock halt is floating")) == 0) {
      this.out.setValue(1);
      this.out.setValue(0);

      try {
        sleepNanos(this.sleep);
      } catch(final InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static final long SLEEP_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);
  private static final long SPIN_YIELD_PRECISION = TimeUnit.MILLISECONDS.toNanos(2);

  public static void sleepNanos(final long nanoDuration) throws InterruptedException {
    final long end = System.nanoTime() + nanoDuration;
    long timeLeft = nanoDuration;

    do {
      if(timeLeft > SLEEP_PRECISION) {
        Thread.sleep(1);
      } else if(timeLeft > SPIN_YIELD_PRECISION) {
        Thread.yield();
      }

      timeLeft = end - System.nanoTime();
    } while(timeLeft > 0);
  }
}
