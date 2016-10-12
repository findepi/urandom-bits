public class ShouldNotCompile {

	public static void main(String[] args) {
//		foo(rt -> true); // javac 1.8.0_66: error: reference to foo is ambiguous
	}

	public static <T extends java.io.Serializable> void foo(T serialiable) {
	}

	public static void foo(java.util.function.Predicate<?> predicate) {
	}
}
