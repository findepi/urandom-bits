package piofin.test.java.j8;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Jun 28, 2015
 */
public class SetStream {
	public static <E> Set<E> reduceSet1(List<? extends Set<E>> sets) {
		return sets.stream().<Set<E>> reduce(
				ImmutableSet.<E> of(),
				Sets::union,
				Sets::union);
	}
}
