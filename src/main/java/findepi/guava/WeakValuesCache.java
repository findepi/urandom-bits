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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class WeakValuesCache
{
    public static void main(String[] args)
    {
        // run me with little memory

        Cache<Integer, String> cache = CacheBuilder.newBuilder()
                .weakValues()
                .build();

        for (int i = 0; i < 10_000_000; i++) {
            cache.put(i, String.valueOf(i));
        }

        System.out.println(cache.asMap().hashCode());
    }
}
