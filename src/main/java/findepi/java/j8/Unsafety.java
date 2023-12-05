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
package findepi.java.j8;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Feb 21, 2016
 */
public class Unsafety {
	public static void main(String[] args) throws Exception {

		Unsafe unsafe;
		// unsafe = Unsafe.getUnsafe();

		// unsafe = (Unsafe) Unsafe.class.getDeclaredMethod("getUnsafe").invoke(null);
		Field field = Unsafe.class.getDeclaredField("theUnsafe");
		field.setAccessible(true);
		unsafe = (Unsafe) field.get(null);

		System.out.println(unsafe);

		Thread th = new Thread() {
			public void run() {
				System.out.println("Thread.run() park");
				unsafe.park(true, System.currentTimeMillis() + 1500);
				System.out.println("Thread.run() exit");
			};
		};

		System.out.println("Unsafety.main() th.start");
		th.start();

		SECONDS.sleep(5);

		System.out.println("Unsafety.main() unpark");
		unsafe.unpark(th);

	}
}
