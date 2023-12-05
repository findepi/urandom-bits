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
package findepi.java.bool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author findepi <piotr.findeisen@syncron.com>
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
