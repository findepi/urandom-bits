package findepi.test.java.j8;

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
