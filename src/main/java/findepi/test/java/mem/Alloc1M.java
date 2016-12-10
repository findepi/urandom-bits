package findepi.test.java.mem;

import java.util.ArrayList;
import java.util.List;

/**
 * Run with e.g.
 *
 * <pre>
 * -XX:-CITime  -XX:+PrintCompilation -Xint
 * </pre>
 *
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Mar 16, 2016
 */
public class Alloc1M {
	public static void main(String[] args) {
		final int count = 1_000_000;
		long start = System.currentTimeMillis();
		List<Object> list = new ArrayList<>(/* count */);
		for (int i = 0; i < count; ++i) {
			list.add(new Object());
		}
		long end = System.currentTimeMillis();
		System.out.printf("took %s ms\n", end - start);
	}
}
