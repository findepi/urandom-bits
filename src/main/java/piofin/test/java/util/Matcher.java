package piofin.test.java.util;

import java.util.regex.Pattern;

/**
 * 
 * @author piofin <piotr.findeisen@syncron.com>
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
