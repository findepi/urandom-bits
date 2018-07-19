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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;
import static java.lang.String.format;

public class PrestoHivePopeGregoryAndJuliusCaesar
{
    private static final MethodHandle epochDayJavaTime;
    private static final MethodHandle epochDayJoda;
    private static final MethodHandle epochDayPresto;
    private static final MethodHandle epochDayHive;

    static {
        try {
            epochDayJavaTime = MethodHandles.lookup().unreflect(PrestoHivePopeGregoryAndJuliusCaesar.class.getDeclaredMethod("epochDayJavaTime", String.class));
            epochDayJoda = MethodHandles.lookup().unreflect(PrestoHivePopeGregoryAndJuliusCaesar.class.getDeclaredMethod("epochDayJoda", String.class));
            epochDayPresto = MethodHandles.lookup().unreflect(PrestoHivePopeGregoryAndJuliusCaesar.class.getDeclaredMethod("epochDayPresto", String.class));
            epochDayHive = MethodHandles.lookup().unreflect(PrestoHivePopeGregoryAndJuliusCaesar.class.getDeclaredMethod("epochDayHive", String.class));
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        String[] inputDates = {
                "2018-01-01",
                "1980-01-01",
                "1970-01-01",
                "1950-01-01",
                "1900-01-01",
                "1700-01-01",
                "1600-01-01",
                "1500-01-01",
                "1300-01-01",
                "1000-01-01",
                "0900-01-01",
                "0700-01-01",
                "0300-01-01",
                "0100-01-01",
                "0001-01-01",
                "0000-12-31",
                "0000-01-01",
                "-0001-01-01",
        };

        for (String date : inputDates) {

            List<Object> fmtArgs = new ArrayList<>();
            fmtArgs.add(date);

            String dateJavaTime = call(epochDayJavaTime, date);
            String dateJoda = call(epochDayJoda, date);
            String datePresto = call(epochDayPresto, date);
            String dateHive = call(epochDayHive, date);
            List<String> reprs = Arrays.asList(dateJavaTime, dateJoda, datePresto, dateHive);
            fmtArgs.addAll(reprs);
            fmtArgs.add(reprs.stream().distinct().count() == 1);

            long prestoHiveDifference = Long.valueOf(dateHive) - Long.valueOf(datePresto);
            if (prestoHiveDifference != 0) {
                fmtArgs.add(format("-- p-h difference %+d", + prestoHiveDifference));
            } else {
                fmtArgs.add("");
            }

            System.out.printf("%11s: java.time: %7s, joda: %7s, presto: %7s, hive: %7s, allMatch: %5s %s\n", fmtArgs.toArray());
        }
    }

    private static String call(MethodHandle method, Object... args)
    {
        try {
            return method.invokeWithArguments(args).toString();
        }
        catch (Throwable throwable) {
            return "err :(";
        }
    }

    private static long epochDayJavaTime(String date)
    {
        return java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(date, java.time.LocalDate::from).toEpochDay();
    }

    private static long epochDayJoda(String date)
    {
        org.joda.time.LocalDate localDate = new DateTimeFormatter(
                null,
                DateTimeFormat.forPattern("yyyy-M-d").getParser())
                .parseLocalDate(date);
        long epochDay = TimeUnit.MILLISECONDS.toDays(localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis());
        return epochDay;
    }

    // com.facebook.presto.util.DateTimeUtils#parseDate
    private static int epochDayPresto(String date)
    {
        return toIntExact(TimeUnit.MILLISECONDS.toDays(ISODateTimeFormat.date().withZoneUTC().parseMillis(date)));
    }

    private static long epochDayHive(String date)
    {
        try {
            return Hive.castStringToDate(date);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Bits copied from org.apache.hadoop.hive.serde2.io.DateWritable, Hive 1.2.2
    @SuppressWarnings("AnonymousHasLambdaAlternative")
    private static class Hive
    {
        // extracted from org.apache.hadoop.hive.ql.exec.vector.expressions.CastStringToDate
        static long castStringToDate(String text)
                throws ParseException
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = formatter.parse(text);
            java.sql.Date sqlDate = new java.sql.Date(0);
            sqlDate.setTime(utilDate.getTime());
            long days = DateWritable.dateToDays(sqlDate);
            return days;
        }

        // extracted from org.apache.hadoop.hive.serde2.io.DateWritable
        static class DateWritable
        {
            private static final long MILLIS_PER_DAY = TimeUnit.DAYS.toMillis(1);

            // Local time zone. Store separately because Calendar would clone it.
            // Java TimeZone has no mention of thread safety. Use thread local instance to be safe.
            private static final ThreadLocal<TimeZone> LOCAL_TIMEZONE = new ThreadLocal<TimeZone>()
            {
                @Override
                protected TimeZone initialValue()
                {
                    return Calendar.getInstance().getTimeZone();
                }
            };

            public static int millisToDays(long millisLocal)
            {
                // We assume millisLocal is midnight of some date. What we are basically trying to do
                // here is go from local-midnight to UTC-midnight (or whatever time that happens to be).
                long millisUtc = millisLocal + LOCAL_TIMEZONE.get().getOffset(millisLocal);
                int days;
                if (millisUtc >= 0L) {
                    days = (int) (millisUtc / MILLIS_PER_DAY);
                }
                else {
                    days = (int) ((millisUtc - 86399999 /*(MILLIS_PER_DAY - 1)*/) / MILLIS_PER_DAY);
                }
                return days;
            }

            public static int dateToDays(java.sql.Date d)
            {
                // convert to equivalent time in UTC, then get day offset
                long millisLocal = d.getTime();
                return millisToDays(millisLocal);
            }
        }
    }
}
