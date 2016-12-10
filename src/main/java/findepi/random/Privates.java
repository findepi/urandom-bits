package findepi.random;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since May 7, 2013
 */
public class Privates {

	private static void thisIsPrivate() {
	}

	private static class Inner extends Privates {
		public Inner() {
			thisIsPrivate();
		}
	}

}
