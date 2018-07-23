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

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static com.google.common.base.Verify.verify;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MapRecursiveComputeIfAbsent
{
    private static final ExecutorService executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .build());

    public static void main(String[] args)
    {
        // results given for jdk1.8.0_121

        test(HashMap::new, 1); // OK
        test(HashMap::new, 2); // OK
        test(HashMap::new, 5); // wrong size of values' copy
        test(HashMap::new, 50); // wrong size of values' copy
        test(HashMap::new, 200); // wrong size of values' copy

        test(ConcurrentHashMap::new, 1); // OK
        test(ConcurrentHashMap::new, 2); // OK
        test(ConcurrentHashMap::new, 3); // timeout
    }

    private static void test(Supplier<Map<Key, Integer>> mapSupplier, int depth)
    {
        Map<Key, Integer> map = mapSupplier.get();
        Future<?> result = executor.submit(() -> {
            compute(map, 1, depth);
            verify(depth == map.size(), "wrong size");
            verify(depth == map.values().size(), "wrong size of values");
            verify(depth == new ArrayList<>(map.values()).size(), "wrong size of values' copy");
            verify(depth == new ArrayList<>(map.entrySet()).size(), "wrong size of entry set's copy");
        });
        try {
            result.get(4, SECONDS);
            System.out.printf("%s, %s -> OK\n", map.getClass(), depth);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        catch (ExecutionException e) {
            System.out.printf("%s, %s -> failure: %s\n", map.getClass(), depth, e.getCause());
        }
        catch (TimeoutException e) {
            System.out.printf("%s, %s -> timeout\n", map.getClass(), depth);
            result.cancel(true);
        }
    }

    private static void compute(Map<Key, Integer> map, int level, int depth)
    {
        map.computeIfAbsent(new Key(level), key -> {
            if (level < depth) {
                compute(map, level + 1, depth);
            }
            return level;
        });
    }

    private static class Key
    {
        private final int i;

        Key(int i)
        {
            this.i = i;
        }

        @Override
        public int hashCode()
        {
            // This is, of course, correct hashCode.
            // If you think it's theoretical only, think about hash collision (intentional or not).
            // return 0;

            return i % 2;
        }

        @Override
        public boolean equals(Object obj)
        {
            return obj instanceof Key
                    && this.i == ((Key) obj).i;
        }
    }
}
