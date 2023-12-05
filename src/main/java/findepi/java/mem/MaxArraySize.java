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
package findepi.java.mem;

import java.util.function.Function;

/**
 * @author findepi <piotr.findeisen@syncron.com>
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
