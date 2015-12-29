import java.util.List;
import java.util.function.Predicate;

public class Inferrr {
	public List<? extends Predicate<? super String>> getStringAcceptingPredicates() {
		return java.util.Arrays.asList(
				eq("s"),
				eq(new Object())
		);
	}

	private <T> Predicate<T> eq(T compared) {
		return s -> compared.equals(s);
	}
}
