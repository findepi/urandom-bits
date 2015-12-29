import java.util.ArrayList;
import java.util.List;

/*
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=417412
 */
public class Compile3 {
	public static <T,
			Y extends List<T>,
			ReturnType extends List<T>>
			ReturnType nullCopy(Y list) {
		return null;
	}

	public static void main(String[] args) {
		List<String> nullList = null;
		ArrayList<String> list = nullCopy(nullList);
	}
}
