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

import org.joda.time.DateTimeZone;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.repeat;
import static java.lang.String.format;

public class JodaJdkZoneDrift
{
    public static void main(String[] args)
    {
        for (int year : new int[] {1884, 1883}) {
            long nearYearStart = LocalDateTime.of(year, 1, 3, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000;
            System.out.printf("year %s\n", year);
            System.out.printf("  %s\n", java.util.TimeZone.getTimeZone("America/New_York").getOffset(nearYearStart) / 1000);
            System.out.printf("  %s\n", java.time.ZoneId.of("America/New_York").getRules().getOffset(Instant.ofEpochMilli(nearYearStart)).getTotalSeconds());
            System.out.println();
        }

        int step = 10;
        int startYear = LocalDate.now().getYear() / step * step;
        int endYear = 1700;
        String[] zones = {"Europe/Warsaw", "America/New_York", "Asia/Kathmandu"};
        String fmt = "%7s" + repeat(" %24s", zones.length) + "\n";
        System.out.printf(
                fmt,
                Stream.concat(
                        Stream.of(""),
                        Stream.of(zones))
                        .toArray());

        for (int year = startYear; year >= endYear; year -= step) {
            // Using timestamp for simplicity. Using January 3rd to avoid wrong answers if there was a TZ change at/near year start
            long startOfYearUtcTimestamp = LocalDateTime.of(year, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC) * 1000;

            System.out.printf(
                    fmt,
                    Stream.concat(
                            Stream.of("" + year),
                            Stream.of(zones)
                                    .map(id -> {
                                        int javaTimeOffset = ZoneId.of(id)
                                                .getRules()
                                                .getOffset(Instant.ofEpochMilli(startOfYearUtcTimestamp))
                                                .getTotalSeconds();

                                        int java7Offset = TimeZone.getTimeZone(id).getOffset(startOfYearUtcTimestamp) / 1000;

                                        int jodaOffset = DateTimeZone.forID(id)
                                                .getOffset(startOfYearUtcTimestamp) / 1000;

                                        checkState(jodaOffset == javaTimeOffset, "Joda and java.time always agree, right?");

                                        return format("%6s %6s  [%4s]", java7Offset, jodaOffset, jodaOffset - java7Offset);
                                    }))
                            .toArray());
        }
    }
}
