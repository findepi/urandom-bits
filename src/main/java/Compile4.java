import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Compile4 {

	public static <Token extends Serializable,
			Col extends Iterable<Token>,
			ReturnType extends List<Token>,
			ReturnType2 extends ReturnType>
			ReturnType2 flatten(Iterable<Col> elements) {

		List<Token> list = new ArrayList<Token>();
		for (Col els : elements) {
			for (Token e : els) {
				list.add(e);
			}
		}

		@SuppressWarnings("unchecked")
		ReturnType2 result = (ReturnType2) list;
		return result;
	}

	public static void main(String[] args) {
		Set<Set<String>> someStrings = new HashSet<Set<String>>();

		// assignment works (but the tooltip is incorrect)
		List<String> assignment = flatten(someStrings);

		// invocation chaining does not compile in Eclipse
		flatten(someStrings)
				.add("");
	}
}
