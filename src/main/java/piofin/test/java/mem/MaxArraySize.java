package piofin.test.java.mem;

import java.util.function.Function;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Oct 12, 2016
 */
public class MaxArraySize {

	public static void main(String[] args) throws Exception {
		Thread.sleep(5000);
		printMaxArraySize(boolean[]::new);
		printMaxArraySize(int[]::new);
		printMaxArraySize(long[]::new);
	}

	private static void printMaxArraySize(Function<Integer, ? extends Object> factory) {
		String description = factory.apply(0).getClass().getCanonicalName();

		int lo = 0; // already tested as possible
		int hi = Integer.MAX_VALUE; // assume this is impossible

		while (lo + 1 < hi) {
			int mi = (lo + hi) >>> 1;
			boolean allocPossible = allocPossible(factory, mi);
			if (allocPossible) {
				lo = mi;
			} else {
				hi = mi;
			}

//			System.out.printf("%s %s %s\n", lo, hi, mi);
		}

		System.out.printf("Max array size for %s is %s, i.e. Integer.MAX_VALUE - %s\n", description, lo,
				Integer.MAX_VALUE - lo);
	}

	private static boolean allocPossible(Function<Integer, ? extends Object> factory, int size) {
		try {
			factory.apply(size);
			return true;
		} catch (OutOfMemoryError e) {
			return false;
		}
	}
}
