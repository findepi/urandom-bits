package findepi.java.j8;

import java.util.Arrays;

/**
 * @author findepi
 * @since Oct 27, 2016
 */
public class LambdiedMethod {
	public static void main(String[] args) {
		Arrays.stream(LambdiedMethod.class.getDeclaredMethods())
				.forEach(System.out::println);
	}
}
