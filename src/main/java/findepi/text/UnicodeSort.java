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
package findepi.text;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import io.airlift.slice.Slices;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class UnicodeSort
{
    public static void main(String[] args)
    {
        testSort("abc", "abd");
        testSort("ab⌘", "abc");
        testSort("ab\uFAD8", "ab\uD83D\uDD74");
    }

    private static void testSort(String left, String right)
    {
        System.out.println();

        compare(left, right, "String::compareTo", String::compareTo);

        compare(left, right, "UTF-8 bytes unsigned", Ordering.<Integer>natural()
                .lexicographical()
                .onResultOf(UnicodeSort::getUtf8UnsignedBytes));

        compare(left, right, "UTF-8 slice", Comparator.comparing(Slices::utf8Slice));
    }

    private static void compare(String left, String right, String comparatorName, Comparator<String> comparator)
    {
        requireNonNull(left, "left is null");
        requireNonNull(right, "right is null");

        int comparison = comparator.compare(left, right);
        String result;
        if (comparison < 0) {
            result = "less than";
        }
        else if (comparison == 0) {
            result = "equal to";
        }
        else {
            result = "greater than";
        }

        System.out.printf("'%s' is %s '%s' according to %s\n", left, result, right, comparatorName);
    }

    private static List<Integer> getUtf8UnsignedBytes(String s)
    {
        requireNonNull(s, "s is null");
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        int[] unsignedBytes = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            unsignedBytes[i] = 0xFF & bytes[i];
        }
        return Ints.asList(unsignedBytes);
    }
}
