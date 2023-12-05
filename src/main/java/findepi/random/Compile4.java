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
package findepi.random;

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
