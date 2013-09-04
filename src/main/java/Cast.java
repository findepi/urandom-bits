import java.io.Serializable;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Jul 2, 2012
 */
public class Cast {
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			Serializable x = (Serializable) new Object();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		System.err.println();
		try {
			Serializable.class.cast(new Object());
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}
}
