package findepi.reflection.objenesis;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 *
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 15, 2015
 */
public class ObjenesisTest {

	public static void main(String[] args) {
		Objenesis objenesis = new ObjenesisStd(false);
		ObjectInstantiator<Uninstantiable> instantiator = objenesis.getInstantiatorOf(Uninstantiable.class);
		Uninstantiable instance = instantiator.newInstance();
		instance.witness();
	}
}

class Uninstantiable {
	
	private Uninstantiable() {
		System.out.println("Uninstantiable.Uninstantiable()");
		throw new UnsupportedOperationException();
	}
	
	public void witness() {
		System.out.println("Uninstantiable.witness()");
	}
}
