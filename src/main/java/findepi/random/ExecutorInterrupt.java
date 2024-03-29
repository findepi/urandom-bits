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
package findepi.random;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.Lists;

public class ExecutorInterrupt {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException, TimeoutException,
			ExecutionException {
		final AtomicReference<Thread> currentThread = new AtomicReference<Thread>();
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

		ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

		List<Future<?>> futures = Lists.newArrayList();

		for (int i = 0; i < 5; ++i) {
			Future<?> future = newSingleThreadExecutor.submit(new Runnable() {
				public void run() {
					try {
						currentThread.set(Thread.currentThread());
						cyclicBarrier.await();
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			futures.add(future);
		}

		Thread thread = null;

		for (Future<?> future : futures) {
			cyclicBarrier.await(1, TimeUnit.SECONDS);
			thread = currentThread.get();
			System.out.println(thread);
			thread.interrupt();
			thread.interrupt();
			thread.interrupt();
			thread.interrupt();
			thread.interrupt();
			thread.interrupt();
			thread.interrupt();
			try {
				future.get();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		thread.interrupt();

		newSingleThreadExecutor.submit(new Runnable() {
			public void run() {

			}
		}).get();
	}
}
