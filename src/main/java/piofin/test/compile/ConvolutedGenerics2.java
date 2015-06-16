package piofin.test.compile;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Jun 16, 2015
 */
public class ConvolutedGenerics2 {

	public static void main(String[] args) {

		/*
		 * This does not compile before Java 8
		 */

		Function<? super Long, ? extends Predicate<? super Thread>> f1 = null;
		List<Function<? super Long, ? extends Predicate<? super Thread>>> list = ImmutableList.of(f1, f1);

	}
}
