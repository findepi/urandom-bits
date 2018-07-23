package findepi.reflection.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.RandomAccess;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 26, 2015
 */
public class JLRProxyTest {

	public interface Finalizable {
		void finalize();
	}

	public static void mainproxy(String[] args) {

		for (int i = 0; i < 100_000_000; ++i) {

			Object proxy = Proxy.newProxyInstance(JLRProxyTest.class.getClassLoader(),
					new Class[] {
							RandomAccess.class,
							Serializable.class,
							Finalizable.class,
					}, new InvocationHandler() {

						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							if (method.getName().equals("finalize") && args == null) {
								return null;
							}

							if (method.getName().equals("toString") && args == null) {
								return "toString";
							}

							return null;
						}
					});

			proxy.toString().hashCode();

		}
	}

	public static void mainjavassist(String[] args) throws Exception {

		for (int i = 0; i < 1_000_000; ++i) {
			ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.setSuperclass(Object.class);

			MethodHandler handler = new MethodHandler() {
				@Override
				public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
					if (args.length == 0 && "finalize".equals(thisMethod.getName())) {
						return null;
					}

					if (args.length == 0 && thisMethod.getName().equals("toString")) {
						return "toString";
					}

					return null;
				}
			};
			Object proxy = proxyFactory.create(new Class[0], new Object[0],
					handler);

			proxy.toString().hashCode();
		}
	}

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 10_000_000; ++i) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(Object.class);
			enhancer.setCallback(new MethodInterceptor() {

				@Override
				public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
					if (args.length == 0 && "finalize".equals(method.getName())) {
						return null;
					}

					if (args.length == 0 && method.getName().equals("toString")) {
						return "toString";
					}

					return null;
				}
			});

			Object proxy = enhancer.create();

			proxy.toString().hashCode();
		}
	}

}
