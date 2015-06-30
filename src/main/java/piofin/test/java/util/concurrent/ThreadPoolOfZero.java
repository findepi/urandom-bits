package piofin.test.java.util.concurrent;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Jun 30, 2015
 */
public class ThreadPoolOfZero {
	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 2, 1, SECONDS,
				new LinkedBlockingQueue<Runnable>());

		Future<?> future = threadPoolExecutor.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("ThreadPoolOfZero.main(...).new Runnable() {...}.run()");
			}
		});

		Object object = future.get(5, SECONDS);
		System.out.println("got: " + object);
	}
}
