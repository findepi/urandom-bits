package findepi.test.os;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.stream.IntStream;

import com.google.common.collect.Sets;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 28, 2016
 */
public class ClockTheClock {
	public static void main(String[] args) {

		int currentMilli;
		long currentSecond;
		do {
			long current = System.currentTimeMillis();
			currentMilli = (int) (current % 1000);
			currentSecond = current / 1000;
		} while (currentMilli != 0);

		boolean[] hits = new boolean[1000];
		hits[0] = true;

		final long second = currentSecond;

		do {
			long current = System.currentTimeMillis();
			currentMilli = (int) (current % 1000);
			currentSecond = current / 1000;

			hits[currentMilli] = true;

		} while (second == currentSecond);

		Set<Integer> expected = IntStream.range(0, 1000).boxed().collect(toSet());
		Set<Integer> hit = expected.stream()
				.filter(milli -> hits[milli])
				.collect(toSet());

		System.out.println("equal: " + expected.equals(hit));
		System.out.println("missed: "  + Sets.difference(expected, hit));
	}
}
