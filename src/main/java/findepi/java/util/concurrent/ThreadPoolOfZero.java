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
package findepi.java.util.concurrent;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Jun 30, 2015
 */
public class ThreadPoolOfZero {
	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 2, 1, SECONDS,
				new LinkedBlockingQueue<Runnable>());

		Future<?> future = threadPoolExecutor.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("ThreadPoolOfZero.main(...).new Runnable() {...}.run()");
			}
		});

		Object object = future.get(5, SECONDS);
		System.out.println("got: " + object);
	}
}
