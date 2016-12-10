package findepi.test.java.j8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

/**
 * @author findepi <piotr.findeisen@gmail.com>
 * @since Mar 4, 2016
 */
public class StreamHog {

	// can we trust the availableProcessors() ?
	private static final int HOGS = Runtime.getRuntime().availableProcessors() * 4 + 10;

	public static void main(String[] args) {
		processElementsSequential();
		processElementsStream();
		processElementsParallelStream();
	}

	private static void processElementsSequential() {
		String action = "processElementsSequential";
		try (ForkJoinHog hog = hog()) {
			List<Integer> elements = elements();
			Consumer<Integer> consumer = consumer(action);
			try (TimingPriner timingPrinter = new TimingPriner(action)) {
				for (Integer i : elements) {
					consumer.accept(i);
				}
			}
		}
	}

	private static void processElementsStream() {
		String action = "processElementsStream";
		try (ForkJoinHog hog = hog()) {
			List<Integer> elements = elements();
			Consumer<Integer> consumer = consumer(action);
			try (TimingPriner timingPrinter = new TimingPriner(action)) {
				elements
						.stream()
						.forEach(consumer);
			}
		}
	}

	private static void processElementsParallelStream() {
		String action = "processElementsParallelStream";
		try (ForkJoinHog hog = hog()) {
			List<Integer> elements = elements();
			Consumer<Integer> consumer = consumer(action);
			try (TimingPriner timingPrinter = new TimingPriner(action)) {
				elements
						.parallelStream()
						.forEach(consumer);
			}
		}
	}

	private static List<Integer> elements() {
		Random r = new Random(12798342L);
		List<Integer> l = new ArrayList<>();
		for (int i = 0; i < 20; ++i) {
			l.add(r.nextInt());
		}
		return l;
	}

	private static Consumer<Integer> consumer(String action) {
		return i -> {
			System.out.printf("%s: consuming %d (btw, common FJ size = %s) @ %s\n", action, i,
					ForkJoinPool.commonPool().getPoolSize(), Thread.currentThread());
			try {
				Thread.sleep(50); // simulate busy server
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		};
	}

	private static ForkJoinHog hog() {
		ForkJoinHog hog = new ForkJoinHog();
		hog.start(); // bad, possible thread leak if start() throws
		return hog;
	}

	private static class ForkJoinHog implements AutoCloseable {

		private final Object sleepLock = new Object();
		// @GuardedBy("sleepLock")
		private boolean closed = false;

		private final Thread th = new Thread(new Runnable() {
			public void run() {

				Collections.nCopies(HOGS, 1)
						.parallelStream()
						.forEach(i -> {
							System.out.printf("Thread %s hogging some time....\n",
									Thread.currentThread(), i);
							try {
								synchronized (sleepLock) {
									if (!closed) {
										sleepLock.wait(1000);// not pretty, but allows "wake-able" sleep
									}
								}
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								throw new RuntimeException(e);
							}
						});
			}
		});

		void start() {
			th.start();
			try {
				Thread.sleep(200); // let them start
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
		}

		@Override
		public void close() {
			synchronized (sleepLock) {
				closed = true;
				sleepLock.notifyAll();
			}
			try {
				th.join(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}
			if (th.isAlive()) {
				throw new RuntimeException("thread still alive");
			}
			System.out.println("hog closed");
		}
	}

	private static class TimingPriner implements AutoCloseable {
		private String action;
		private long startMillis;

		public TimingPriner(String action) {
			this.action = action;
			System.out.printf("****************************** starting %s\n", action);
			this.startMillis = System.currentTimeMillis();
		}

		@Override
		public void close() {
			System.out.printf("****************************** %s took about %d ******************************** \n", action,
					(System.currentTimeMillis() - startMillis));
		}
	}
}
