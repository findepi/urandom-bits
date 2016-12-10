package findepi.test.java.beans;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BridgeMethodsTest {
	/* @formatter:off */
	public interface Propertiful {
		Number getX1();
		Number getX2();
		Number getX3();
		Number getX4();
		Number getX5();
	}
	public static class Derived implements Propertiful {
		public void setX1(Long newValue) {} @Override public Long getX1() { return null; }
		public void setX2(Long newValue) {} @Override public Long getX2() { return null; }
		public void setX3(Long newValue) {} @Override public Long getX3() { return null; }
		public void setX4(Long newValue) {} @Override public Long getX4() { return null; }
		public void setX5(Long newValue) {} @Override public Long getX5() { return null; }
	}
	/* @formatter:on */

	public static void main(String[] args) throws Exception {
		Map<String, Class<?>> propertyNameToExpectedType = new HashMap<>();
		propertyNameToExpectedType.put("class", Class.class);
		propertyNameToExpectedType.put("x1", Long.class);
		propertyNameToExpectedType.put("x2", Long.class);
		propertyNameToExpectedType.put("x3", Long.class);
		propertyNameToExpectedType.put("x4", Long.class);
		propertyNameToExpectedType.put("x5", Long.class);

		PropertyDescriptor[] pds = Introspector.getBeanInfo(Derived.class).getPropertyDescriptors();
		check(pds.length == 6, "Should find 6 PropertyDescriptors, got: " + pds.length);
		for (PropertyDescriptor pd : pds) {
			check(propertyNameToExpectedType.containsKey(pd.getName()), "unexepcted: " + pd.getName());
			Class<?> expectedType = propertyNameToExpectedType.get(pd.getName());

			checkPropertyType("before GC", pd, expectedType);

			doWhatGCWouldDoAnywaySoonerOrLater(pd);

			checkPropertyType("after GC", pd, expectedType);
		}
	}

	private static void checkPropertyType(String when, PropertyDescriptor pd, Class<?> expectedType) {
		Class<?> propertyType;
		propertyType = pd.getPropertyType();
		check(propertyType == expectedType, "wrong type " + when + ": " + pd.getName() + ", got " + propertyType
				+ ", expected " + expectedType);
	}

	private static void doWhatGCWouldDoAnywaySoonerOrLater(PropertyDescriptor pd) throws NoSuchFieldException,
			IllegalAccessException {

		/*
		 * Be merciful. If I (or GC) cleared propertyTypeRef and readMethodRef but not writeMethodRef, we would just get
		 * null (yes, null!) from pd.getPropertyType()
		 */
		for (String fieldName : Arrays.asList("propertyTypeRef", "readMethodRef", "writeMethodRef")) {
			Field fd = PropertyDescriptor.class.getDeclaredField(fieldName);
			fd.setAccessible(true);
			Object ref = fd.get(pd);
			clearIfReference(ref);
			if (ref != null && ref.getClass().getSimpleName().equals("MethodRef")) {
				// Java 8
				for (String mrfn : Arrays.asList("methodRef")) {
					Field mrf = ref.getClass().getDeclaredField(mrfn);
					mrf.setAccessible(true);
					Object v = mrf.get(ref);
					clearIfReference(v);
				}
			}
		}
	}

	private static void clearIfReference(Object ref) {
		if (ref instanceof WeakReference || ref instanceof SoftReference) {
			((Reference<?>) ref).clear();
		}
	}

	private static void check(boolean test, String message) {
		if (!test) {
			throw new AssertionError(message);
		}
	}
}
