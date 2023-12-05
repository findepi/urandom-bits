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
package findepi.java.beans;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BridgeMethodsTest2 {
	/* @formatter:off */
	public static abstract class Propertiful<T extends Number> {
		public  Number getX1() { return null; }
		public  Number getX2() { return null; }
		public  Number getX3() { return null; }
		public  Number getX4() { return null; }
		public void setX6(T newValue) {} public T getX6() { return null; }
		public void setX7(T newValue) {} public T getX7() { return null; }
		public void setX8(T newValue) {} public T getX8() { return null; }
		public void setX9(T newValue) {} public T getX9() { return null; }
	}

	public static class Derived extends Propertiful<Long> {
		public void setX1(Long newValue) {} @Override public Long getX1() { return null; }
		public void setX2(Long newValue) {} @Override public Long getX2() { return null; }
		public void setX3(Long newValue) {} @Override public Long getX3() { return null; }
		public void setX4(Long newValue) {} @Override public Long getX4() { return null; }
	}
	/* @formatter:on */

	public static void main(String[] args) throws Exception {

		// Get PropertyDescriptor except the getClass()
		List<PropertyDescriptor> pds = new ArrayList<>();
		Collections.addAll(pds, Introspector.getBeanInfo(Derived.class).getPropertyDescriptors());
		for (Iterator<PropertyDescriptor> pi = pds.iterator(); pi.hasNext();) {
			if (pi.next().getName().equals("class")) {
				pi.remove();
			}
		}

		System.out.println("BEFORE GC");
		for (PropertyDescriptor pd : pds) {
			System.out.printf("%s type         : %s\n", pd.getName(), pd.getPropertyType());
			System.out.printf("%s read method  : %s\n", pd.getName(), pd.getReadMethod());
			System.out.printf("%s write method : %s\n", pd.getName(), pd.getWriteMethod());
			System.out.printf("%s type         : %s\n", pd.getName(), pd.getPropertyType());
		}

		System.out.println("SIMULATE GC");
		for (PropertyDescriptor pd : pds) {
			doWhatGCWouldDoAnywaySoonerOrLater(pd);
		}

		System.out.println("AFTER GC");
		for (PropertyDescriptor pd : pds) {
			System.out.printf("%s type         : %s\n", pd.getName(), pd.getPropertyType());
			System.out.printf("%s read method  : %s\n", pd.getName(), pd.getReadMethod());
			System.out.printf("%s write method : %s\n", pd.getName(), pd.getWriteMethod());
			System.out.printf("%s type         : %s\n", pd.getName(), pd.getPropertyType());
		}
	}

	/*
	 * private static void doWhatGCWouldDoAnywaySoonerOrLaterx(PropertyDescriptor pd) throws NoSuchFieldException,
	 * IllegalAccessException {
	 * 
	 * 
	 * Be merciful. If I (or GC) cleared propertyTypeRef and readMethodRef but not writeMethodRef, we would just get
	 * null (yes, null!) from pd.getPropertyType()
	 * 
	 * for (String fieldName : Arrays.asList("readMethodRef", "writeMethodRef")) { Field fd =
	 * PropertyDescriptor.class.getDeclaredField(fieldName); fd.setAccessible(true); Object ref = fd.get(pd);
	 * clearIfReference(ref); if (ref != null && ref.getClass().getSimpleName().equals("MethodRef")) { // Java 8 for
	 * (String mrfn : Arrays.asList("methodRef")) { Field mrf = ref.getClass().getDeclaredField(mrfn);
	 * mrf.setAccessible(true); Object v = mrf.get(ref); clearIfReference(v); } } } }
	 */

	private static void doWhatGCWouldDoAnywaySoonerOrLater(PropertyDescriptor pd) throws Exception {
		for (String fieldName : Arrays.asList("readMethodRef", "writeMethodRef")) {
			Field field = PropertyDescriptor.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			SoftReference<?> ref = (SoftReference<?>) field.get(pd);
			if (ref != null) {
				// Feel like the GC and clear the ref.
				ref.clear();
			}
		}
	}
}
