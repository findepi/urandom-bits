package findepi.random;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 16, 2012
 */
public class BridgeTest {

	public static class Super1 {
		public Object one() {
			return null;
		}
	}

	public static class Base1 extends Super1 {
		@Override
		public String one() {
			return null;
		}
	}

	public static class Super2<T> {
		public T two() {
			return null;
		}
	}

	public static class Base2 extends Super2<Integer> {
		@Override
		public Integer two() {
			return null;
		}
	}

	public static class Base3 extends Super2<Number> {
		@Override
		public Integer two() {
			return null;
		}
	}

	public static class MultiSuper3 {
		public Number getX1() {
			return null;
		}

		public Number getX2() {
			return null;
		}

		public Number getX3() {
			return null;
		}

	}

	public interface MultiSuper4<T, F> {
		public T getX4();

		public T getX5();

		public F getX6();
	}

	public interface MultiSuper5<T, F> {
		public T getX7();

		public T getX8();

		public F getX9();
	}

	public static class MultiBase extends MultiSuper3 implements MultiSuper4<String, Integer>,
			MultiSuper5<Date, Calendar> {

		public void setX7(Date newValue) {
		}

		@Override
		public Date getX7() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX8(Date newValue) {
		}

		@Override
		public Date getX8() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX9(Calendar newValue) {
		}

		@Override
		public Calendar getX9() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX4(String newValue) {
		}

		@Override
		public String getX4() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX5(String newValue) {
		}

		@Override
		public String getX5() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX6(Integer newValue) {
		}

		@Override
		public Integer getX6() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX1(Long newValue) {
		}

		@Override
		public Long getX1() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX2(BigDecimal newValue) {
		}

		@Override
		public BigDecimal getX2() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setX3(BigInteger newValue) {
		}

		@Override
		public BigInteger getX3() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static void main(String[] args) throws Exception {
		Class<?>[] cs = { Base1.class, Base2.class, Base3.class, MultiBase.class };

		for (Class<?> c : cs) {
			System.out.println("*** " + c);
			for (Method m : c.getDeclaredMethods()) {
				System.out.printf("%s : %s\n", m.getName(),
						m.getReturnType() == void.class ? m.getParameterTypes()[0] : m.getReturnType()
						);
			}
			System.out.println();

			System.out.println("*** BeanInfo for " + MultiBase.class);
			BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(MultiBase.class);
			for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
				System.out.printf("%s : %s, %s\n", pd.getName(), pd.getPropertyType(),
						(pd.getWriteMethod() == null ? "RO"
								: (pd.getReadMethod() == null ? "WR" : "RW"))
						);
			}
		}
	}
}
