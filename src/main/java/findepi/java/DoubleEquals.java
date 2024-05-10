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
package findepi.java;

import java.util.Objects;

public class DoubleEquals
{
    public static void main(String[] args)
    {
        double a = Double.NaN;
        double b = Double.NaN;

        System.out.println(a == b); // false
        System.out.println(Objects.equals(a, b)); // true
        System.out.println(new DoubleRecord(a).equals(new DoubleRecord(b))); // true
    }

    public record DoubleRecord(double value) {}
}
