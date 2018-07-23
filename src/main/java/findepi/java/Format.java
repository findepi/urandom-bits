package findepi.java;

import static java.lang.String.format;

public class Format {
	public static void main(String[] args) {
		System.out.println(format("[%.10s]", "aaa"));
		System.out.println(format("[%.10s]", "aaa aaa aaa aaa aaa aaa"));
	}
}
