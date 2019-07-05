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
package findepi.java.j8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import static java.lang.System.nanoTime;
import static java.util.Arrays.setAll;

public class StreamFirst
{
    private static final int MAX_ELEMENT_COUNT = 1024 * 1024 * 16;
    private static final int[] SOURCE_ELEMENTS;

    static {
        System.out.printf("Creating SOURCE_ELEMENTS as int[%s]\n", MAX_ELEMENT_COUNT);
        SOURCE_ELEMENTS = new int[MAX_ELEMENT_COUNT];
        System.out.println("Filling SOURCE_ELEMENTS");
        setAll(SOURCE_ELEMENTS, i -> (i << 7) ^ i);
    }

    public static void main(String[] args)
    {
        System.out.println("main()");
        doIt();
        doIt();
        doIt();
    }

    private static void doIt()
    {
        System.out.println();
        List<Object> sink = new ArrayList<>();

        for (int elementCount = 8; elementCount <= MAX_ELEMENT_COUNT; elementCount *= 2) {
            long intStreamTime = timeIntStream(elementCount, sink);
            long keyValueStreamTime = timeKeyValueStream(elementCount, sink);

            System.out.printf("%s\t%s\t%s\n", elementCount, intStreamTime, keyValueStreamTime);
        }

        System.out.println();
        System.out.println(sink.toString().hashCode());
    }

    private static long timeIntStream(int elementCount, List<Object> sink)
    {
        long start = nanoTime();
        OptionalInt first = IntStream.of(Arrays.copyOf(SOURCE_ELEMENTS, elementCount))
                .sorted()
                .findFirst();
        long stop = nanoTime();

        sink.add(first);
        return stop - start;
    }

    private static long timeKeyValueStream(int elementCount, List<Object> sink)
    {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < elementCount; i++) {
            map.put("" + i, SOURCE_ELEMENTS[i]);
        }

        long start = nanoTime();
        String first = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .findFirst()
                .get()
                .getKey();
        long stop = nanoTime();

        sink.add(first);
        return stop - start;
    }
}
