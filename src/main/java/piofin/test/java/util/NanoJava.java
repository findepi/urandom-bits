package piofin.test.java.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;

/**
 * @author findepi
 * @since May 18, 2016
 */
public class NanoJava {

	public static void main(String[] args) throws Exception {

		Stopwatch nanoBasedWatch = Stopwatch.createUnstarted();
		Stopwatch millisBasedWatch = Stopwatch.createUnstarted(new Ticker() {
			@Override
			public long read() {
				return MILLISECONDS.toNanos(System.currentTimeMillis());
			}
		});
		nanoBasedWatch.start();
		millisBasedWatch.start();

		for (int i = 0; i < 1_000_000; ++i) {
			Thread.sleep(ThreadLocalRandom.current().nextLong(11));

			long diff = nanoBasedWatch.elapsed(MILLISECONDS) - millisBasedWatch.elapsed(MILLISECONDS);
			if (diff <= 2) {
				// be forgiveful, time not quite standing still and rounding might have caused this
			} else {
				System.out.println(diff);
			}
		}
	}
}
