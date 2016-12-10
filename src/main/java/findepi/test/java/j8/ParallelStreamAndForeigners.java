package findepi.test.java.j8;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author findepi <piotr.findeisen@gmail.com>
 * @since Mar 4, 2016
 */
public class ParallelStreamAndForeigners {
	public static void main(String[] args) throws Exception {
		Set<Thread> callers = Collections
				.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<Thread, Boolean>()));

		List<Integer> input = Collections.nCopies(100000, 0);

		for (int i = 0; i < 40; ++i) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					Thread calling = Thread.currentThread();
					while (true) {
						input.parallelStream()
								.forEach(i -> {
									Thread current = Thread.currentThread();
									if (current != calling && callers.contains(current)) {
										System.out.printf("%s is stealing jobs submitted by %s\n", current, calling);
										System.exit(0);
									}
								});
					}
				}
			});
			callers.add(th);
			th.start();
		}

		Thread.sleep(10000);
		System.out.println("no stealing");
		System.exit(0);
	}
}
