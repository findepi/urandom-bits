package piofin.test.java.bool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Nov 22, 2015
 */
public class BooleanArrays {
	static Random r = new Random();

	public static void main(String[] args) throws Exception {

		for (int repeatition = 0; repeatition < 1000; ++repeatition) {
			dotest(repeatition);
		}
	}

	private static void dotest(int repeatition) throws Exception {
		final int count = 1 << 24;
		final boolean[] booleans = new boolean[count];

		final int threads = 2 + r.nextInt(2);
		System.out.println("using " + threads + " threads");

		final CyclicBarrier barrier = new CyclicBarrier(threads);
		List<Thread> createdThreads = new ArrayList<>();
		for (int i = 0; i < threads; ++i) {
			final int thid = i;

			Thread th = new Thread(new Runnable() {
				public void run() {
					try {
						barrier.await();

						for (int i = thid; i < count; i += threads) {
							booleans[i] = true;
						}

					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
			th.setDaemon(true);
			createdThreads.add(th);
			th.start();
		}
		
		for (Thread thread : createdThreads) {
			thread.join();
		}

		// check booleans
		for (int i = 0; i < booleans.length; i++) {
			if (!booleans[i]) {
				System.out.println("false at " + i);
				System.exit(1);
			}
		}
		System.out.println("repeatition " + repeatition + " OK");
	}
}
