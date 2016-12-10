package findepi.random;

import java.util.ArrayList;
import java.util.List;

public class Compile {

	public <T, Exp extends List<T>> Exp typedNull() {
		return null;
	}

	public void call() {
		ArrayList<String> list = typedNull();
	}
}
