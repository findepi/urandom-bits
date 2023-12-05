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
package findepi.java.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ThreadLocalRandom;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;

/**
 * @author findepi
 * @since May 18, 2016
 */
public class NanoJava {

	public static void main(String[] args) throws Exception {

		Stopwatch nanoBasedWatch = Stopwatch.createUnstarted();
		Stopwatch millisBasedWatch = Stopwatch.createUnstarted(new Ticker() {
			@Override
			public long read() {
				return MILLISECONDS.toNanos(System.currentTimeMillis());
			}
		});
		nanoBasedWatch.start();
		millisBasedWatch.start();

		for (int i = 0; i < 1_000_000; ++i) {
			Thread.sleep(ThreadLocalRandom.current().nextLong(11));

			long diff = nanoBasedWatch.elapsed(MILLISECONDS) - millisBasedWatch.elapsed(MILLISECONDS);
			if (diff <= 2) {
				// be forgiveful, time not quite standing still and rounding might have caused this
			} else {
				System.out.println(diff);
			}
		}
	}
}
