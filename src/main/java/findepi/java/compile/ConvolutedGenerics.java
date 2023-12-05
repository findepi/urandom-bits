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

import java.io.Serializable;
import java.util.List;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Sep 20, 2013
 */
public class ConvolutedGenerics {

	// listClass is a class implementing a List of some Serializable type
	public void doSomethingWithListOfSerializables(
			Class<? extends List<? extends Serializable>> listClass) {

		// Capture '? extends Serializable' as 'T extends Serializable'
		// The line does not compile with javac 7
//		captureTheWildcard(listClass);
	}

	// listClass is a class implementing a List of some Serializable type
	private <T extends Serializable>
			void captureTheWildcard(
					Class<? extends List</* ? extends */T>> listClass) {

		// Do something
	}
}
