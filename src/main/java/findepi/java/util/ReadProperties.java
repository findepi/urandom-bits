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
package findepi.java.util;

import java.io.IOException;
import java.util.Properties;

/**
 * @author findepi <piotr.findeisen@syncron.com>
 * @since Sep 4, 2013
 */
public class ReadProperties {
	public static void main(String[] args) throws IOException {
		Properties p = new Properties();
		p.load(ReadProperties.class.getResourceAsStream("prop1.properties"));

		System.out.println("" + p);
	}
}
