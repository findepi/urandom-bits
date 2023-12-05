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
package findepi.random;

public class ShouldNotCompile {

	public static void main(String[] args) {
//		foo(rt -> true); // javac 1.8.0_66: error: reference to foo is ambiguous
	}

	public static <T extends java.io.Serializable> void foo(T serialiable) {
	}

	public static void foo(java.util.function.Predicate<?> predicate) {
	}
}
