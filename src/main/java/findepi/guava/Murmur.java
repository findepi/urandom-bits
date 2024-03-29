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

import com.google.common.hash.Hashing;
import io.airlift.slice.Murmur3Hash32;
import io.airlift.slice.Slices;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Murmur
{
    public static void main(String[] args)
    {
        for (String string : List.of("plan ascii", "BMP Piękna łąka w 東京都",  "surrogate pair 💰")) {
            for (Charset charset : List.of(StandardCharsets.UTF_16, StandardCharsets.UTF_8)) {
                int airliftValue = Murmur3Hash32.hash(Slices.wrappedBuffer(string.getBytes(charset)));
                int guavaValue1 = Hashing.murmur3_32().hashBytes(string.getBytes(charset)).asInt();
                int guavaValue2 = Hashing.murmur3_32().hashString(string, charset).asInt();
                System.out.printf("string: %s, charset: %s\n", string, charset);
                System.out.println("airliftValue = " + airliftValue);
                System.out.println("guavaValue1  = " + guavaValue1);
                System.out.println("guavaValue2  = " + guavaValue2);
                System.out.println();
            }
        }
    }
}
