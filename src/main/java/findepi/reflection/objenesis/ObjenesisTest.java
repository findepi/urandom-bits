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
package findepi.reflection.objenesis;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

/**
 *
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Nov 15, 2015
 */
public class ObjenesisTest {

	public static void main(String[] args) {
		Objenesis objenesis = new ObjenesisStd(false);
		ObjectInstantiator<Uninstantiable> instantiator = objenesis.getInstantiatorOf(Uninstantiable.class);
		Uninstantiable instance = instantiator.newInstance();
		instance.witness();
	}
}

class Uninstantiable {
	
	private Uninstantiable() {
		System.out.println("Uninstantiable.Uninstantiable()");
		throw new UnsupportedOperationException();
	}
	
	public void witness() {
		System.out.println("Uninstantiable.witness()");
	}
}
