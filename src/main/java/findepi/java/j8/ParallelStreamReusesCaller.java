package findepi.java.j8;

import java.util.Collections;

/**
 * @author findepi <piotr.findeisen@gmail.com>
 * @since Mar 4, 2016
 */
public class ParallelStreamReusesCaller {
	public static void main(String[] args) {
		Thread main = Thread.currentThread();
		Collections.nCopies(1000, 0)
				.parallelStream()
				.forEach(i -> {
					System.out.println("Current thread = " + Thread.currentThread());
					if (Thread.currentThread() == main) {
						System.exit(0);
					}
				});
		System.out.println("Did not terminate?");
	}
}
