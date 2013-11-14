package piofin.test.compile;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Nov 14, 2013
 */
public class UnnecessaryCast {

	public java.util.concurrent.locks.ReentrantLock instance;

	public void foo() {
		/*
		 * instanceof does trigger the warning: The expression of type UnnecessaryCast.MyClass is already an instance of
		 * type UnnecessaryCast.MyInterface
		 */
		if (instance instanceof java.util.concurrent.locks.Lock) {
			System.out.println("this is MyInterface");
		}
	}

	public void bar() {
		/*
		 * EXPECTED: cast should also trigger a warning but it does not
		 */
		((java.util.concurrent.locks.Lock) instance).lock();

		/*
		 * this cast correctly produces a warning
		 */
		((java.util.concurrent.locks.ReentrantLock) instance).lock();
	}
}
