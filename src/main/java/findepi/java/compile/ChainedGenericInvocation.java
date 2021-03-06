package findepi.java.compile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Dec 21, 2015
 */
public class ChainedGenericInvocation {

	public static <Token extends Serializable, //
	Col extends Iterable<Token>, //
	ReturnType extends List<Token>, //
	ReturnType2 extends ReturnType> //
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

		// assignment works
		List<String> assignment = flatten(someStrings);

		// invocation chaining does not
		flatten(someStrings)
				.add(""); // Error: The method add(Token) in the type List<Token> is not applicable for the arguments
							// (String)
	}
}
