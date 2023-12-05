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
package findepi.time;

import java.time.Instant;

public class LongTimestampRange
{
    public static void main(String[] args)
    {
        System.out.println("64-bit max with contiguous encoding, p=9:       " + Instant.ofEpochMilli(Long.MAX_VALUE / 1_000_000));
        System.out.println("64-bit max with contiguous encoding, p=7:     " + Instant.ofEpochMilli(Long.MAX_VALUE / 10_000));
        System.out.println("64-bit max with contiguous encoding, p=6:    " + Instant.ofEpochMilli(Long.MAX_VALUE / 1_000));
        System.out.println("64-bit max with contiguous encoding, p=3: " + Instant.ofEpochMilli(Long.MAX_VALUE));
        System.out.println("64-bit max with fast millis             :    " + Instant.ofEpochMilli(Long.MAX_VALUE >> 10));
    }
}
