package findepi.joda;

import org.joda.time.DateTimeZone;

import java.time.Instant;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Arrays.asList;

/**
 * Created by findepi on 2017-03-05.
 */
public class TimeZones
{
    public static void main(String[] args)
    {
        String format = "%15s %27s %18s %25s\n";
        System.out.printf(format, "", "j.u.TimeZone::getTimeZone", "j.t.ZoneId::of", "joda.DateTimeZone::forID");
        for (String tzId : asList("UTC", "GMT", "+01:00", "UTC+01:00", "GMT+01:00", "Europe/Warsaw", "CET", "MST")) {
            System.out.printf(format, tzId
                    , offset(tzId, TimeZone::getTimeZone, (tz, n) -> tz.getOffset(n) / 1000 / 3600.)
                    , offset(tzId, ZoneId::of, (z, n) -> z.getRules().getOffset(Instant.ofEpochMilli(n)).getTotalSeconds() / 3600.)
                    , offset(tzId, DateTimeZone::forID, (z, n) -> z.getOffset(n) / 1000 / 3600.)
            );
        }
    }

    private static <T> String offset(String zoneId, Function<String, T> ctor, BiFunction<T, Long, Double> getOffset)
    {
        T zone = null;
        try {
            zone = ctor.apply(zoneId);
        }
        catch (RuntimeException e) {
            return "-- fail --";
        }
        double offsetHours = getOffset.apply(zone, System.currentTimeMillis());
        return "" + offsetHours;
    }
}
