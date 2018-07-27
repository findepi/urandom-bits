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
package findepi.guava;

import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;

public class Collider
{
    public static void main(String[] args)
    {
        // depends on Java 8's String#hashCode

        ImmutableSet.Builder<String> set = ImmutableSet.builder();
        List<String> list = new ArrayList<>();
        for (int a = 0, b = Character.MAX_VALUE; b >= 0; a += 1, b -= 31) {
            String s = "" + ((char) a) + ((char) b);
            System.out.println(s.hashCode());
            set.add(s);
            list.add(s);
        }

        System.out.println(new ArrayList<>(set.build()).equals(list));
        System.out.println(new ArrayList<>(ImmutableSet.copyOf(list)).equals(list));
    }
}
