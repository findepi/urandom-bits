package findepi.test.packages.packageprivate.packageb;

import java.lang.reflect.Method;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Sep 17, 2013
 */
public class derivedclass extends findepi.test.packages.packageprivate.packagea.publicclass {

	private void callf() throws Exception {
		foo();

		System.out.println("Looking");
		Method method = this.getClass().getSuperclass().getSuperclass()
				.getDeclaredMethod("foo");

		System.out.println("Calling");
//		method.setAccessible(true);
		Object gizmo = method.invoke(this);
		System.out.println(gizmo);
	}

	public static void main(String[] args) throws Exception {
		new derivedclass().callf();
	}
}
