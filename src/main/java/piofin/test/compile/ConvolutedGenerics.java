package piofin.test.compile;

import java.io.Serializable;
import java.util.List;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Sep 20, 2013
 */
public class ConvolutedGenerics {

	// listClass is a class implementing a List of some Serializable type
	public void doSomethingWithListOfSerializables(
			Class<? extends List<? extends Serializable>> listClass) {

		// Capture '? extends Serializable' as 'T extends Serializable'
		// The line does not compile with javac 7
//		captureTheWildcard(listClass);
	}

	// listClass is a class implementing a List of some Serializable type
	private <T extends Serializable>
			void captureTheWildcard(
					Class<? extends List</* ? extends */T>> listClass) {

		// Do something
	}
}
