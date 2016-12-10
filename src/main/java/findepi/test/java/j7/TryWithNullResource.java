package findepi.test.java.j7;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Dec 17, 2013
 */
public class TryWithNullResource {

	public static void main(String[] args) throws Exception {
		try (AutoCloseable resource = getResource()) {
			System.out.println(resource);
		}
	}

	private static AutoCloseable getResource() {
		return null;
	}
}
