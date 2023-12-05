/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package findepi.os;

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
