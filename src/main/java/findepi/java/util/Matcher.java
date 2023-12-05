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
package findepi.java.util;

import java.util.regex.Pattern;

/**
 * 
 * @author findepi <piotr.findeisen@syncron.com>
 * @since dFeb 17, 2014
 */
public class Matcher {

	public static void main(String[] args) {
		String input = "a b c";

		java.util.regex.Matcher matcher = Pattern.compile("\\w").matcher(input);

		StringBuffer out = new StringBuffer();
		while (matcher.find()) {
			String replacement = replacement(matcher.group());
			if (replacement != null) {
				matcher.appendReplacement(out, replacement);
			}
		}
		matcher.appendTail(out);

		System.out.println(out);
	}

	private static String replacement(String group) {
		switch (group) {
		case "a":
			return "1";
		case "c":
			return "3";
		}
		return null;
	}
}
