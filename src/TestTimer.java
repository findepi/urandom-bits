import java.util.Timer;
import java.util.TimerTask;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Oct 19, 2012
 */
public class TestTimer {
	public static void main(String[] args) throws Exception {
		Timer timer = new Timer(true);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("TestTimer.main(...).new TimerTask() {...}.run()");
				 throw new RuntimeException("dedewfdwe");
			}
		}, 0, 5);

		Thread.sleep(10 * 1000);
	}
}
