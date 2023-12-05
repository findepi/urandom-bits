/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
