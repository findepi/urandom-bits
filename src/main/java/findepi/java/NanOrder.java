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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.DoublePredicate;

import static com.google.common.base.Verify.verify;
import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.isNaN;
import static java.util.Map.entry;

public class NanOrder
{
    public static void main(String[] args)
    {
        List<Entry<String, Double>> namedValues = new ArrayList<>();
        add(namedValues, "NaN", Double.NaN, NanOrder::isOrdinaryNan);
        add(namedValues, "Nan (the NaN, 0x7ff8000000000000L)", Double.longBitsToDouble(0x7ff8000000000000L), NanOrder::isOrdinaryNan);
        add(namedValues, "Nan (0xfff8000000000000L)", Double.longBitsToDouble(0xfff8000000000000L), NanOrder::isExtraordinaryNan);
        add(namedValues, "-NaN", -Double.NaN, NanOrder::isOrdinaryNan);

        add(namedValues, "NEGATIVE_INFINITY", Double.NEGATIVE_INFINITY, Double::isInfinite);
        add(namedValues, "POSITIVE_INFINITY", Double.POSITIVE_INFINITY, Double::isInfinite);
        add(namedValues, "0", 0d, d -> d == 0);
        add(namedValues, "-0", -0d, d -> d == 0);
        add(namedValues, "5", 5d, d -> true);
        add(namedValues, "-5", -5d, d -> true);

        namedValues.sort(Entry.comparingByValue());

        System.out.println("peer group");
        for (int i = 0; i < namedValues.size(); i++) {
            Entry<String, Double> current = namedValues.get(i);
            if (i != 0) {
                Entry<String, Double> previous = namedValues.get(i - 1);
                int compare = previous.getValue().compareTo(current.getValue());
                verify(compare <= 0, "not sorted");
                if (compare != 0) {
                    System.out.println("peer group");
                }
            }
            System.out.printf("%40s: %s\n", current.getKey(), current.getValue());
        }
    }

    private static void add(List<Entry<String, Double>> namedValues, String name, double value, DoublePredicate check)
    {
        verify(check.test(value), "Value %s named %s does not satisfy the predicate", value, name);

        namedValues.add(entry(name, value));
    }

    private static boolean isOrdinaryNan(double d)
    {
        return isNaN(d) && doubleToRawLongBits(d) == doubleToRawLongBits(Double.NaN);
    }

    private static boolean isExtraordinaryNan(double d)
    {
        return isNaN(d) && doubleToRawLongBits(d) != doubleToRawLongBits(Double.NaN);
    }
}
