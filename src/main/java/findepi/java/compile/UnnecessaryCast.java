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
package findepi.java.compile;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 14, 2013
 */
public class UnnecessaryCast {

	public java.util.concurrent.locks.ReentrantLock instance;

	public void foo() {
		/*
		 * instanceof does trigger the warning: The expression of type UnnecessaryCast.MyClass is already an instance of
		 * type UnnecessaryCast.MyInterface
		 */
		if (instance instanceof java.util.concurrent.locks.Lock) {
			System.out.println("this is MyInterface");
		}
	}

	public void bar() {
		/*
		 * EXPECTED: cast should also trigger a warning but it does not
		 */
		((java.util.concurrent.locks.Lock) instance).lock();

		/*
		 * this cast correctly produces a warning
		 */
		((java.util.concurrent.locks.ReentrantLock) instance).lock();
	}
}
