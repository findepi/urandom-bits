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

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Jun 16, 2015
 */
public class ConvolutedGenerics2 {

	public static void main(String[] args) {

		/*
		 * This does not compile before Java 8
		 */

		Function<? super Long, ? extends Predicate<? super Thread>> f1 = null;
		List<Function<? super Long, ? extends Predicate<? super Thread>>> list = ImmutableList.of(f1, f1);

	}
}
