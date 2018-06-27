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
import java.time.LocalDate;
import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;

public class FunkyZones
{
    public static void main(String[] args)
    {
        String[] zoneIds = {
                "Pacific/Apia",
                "Pacific/Chatham",
                "Asia/Kathmandu",
                "Europe/Vilnius",
        };

        for (String zoneId : zoneIds) {
            ZoneId zone = ZoneId.of(zoneId);

            Instant zero = Instant.ofEpochMilli(0);
            // Using dates in the past makes the results immune to future policy changes
            Instant january = LocalDate.of(2017, 1, 5).atStartOfDay().atZone(UTC).toInstant();
            Instant june = LocalDate.of(2017, 6, 5).atStartOfDay().atZone(UTC).toInstant();

            System.out.printf(
                    "%20s offset at 0: %s [DST %s]. offset Jan: %s [DST %s], offset Jun: %s [DST %s]\n",
                    zoneId,
                    zone.getRules().getOffset(zero),
                    zone.getRules().isDaylightSavings(zero),
                    zone.getRules().getOffset(january),
                    zone.getRules().isDaylightSavings(january),
                    zone.getRules().getOffset(june),
                    zone.getRules().isDaylightSavings(june)
            );
        }
    }
}
