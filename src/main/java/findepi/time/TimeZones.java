package findepi.time;

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
        String format = "%15s  | %-22s %-27s | %-22s %-18s | %-22s %-25s\n";
        System.out.printf(format, "", "j.u.TimeZone", "j.u.TimeZone::getTimeZone",  "j.t.ZoneId", "j.t.ZoneId::of", "joda.DateTimeZone", "joda.DateTimeZone::forID");
        for (String tzId : asList("UTC", "GMT", "+01:00", "UTC+01:00", "GMT+01:00", "Europe/Warsaw", "CET", "MST", "America/Nuuk")) {
            System.out.printf(format, tzId
                    , canonicalId(tzId, TimeZone::getTimeZone, TimeZone::getID)
                    , offset(tzId, TimeZone::getTimeZone, (tz, n) -> tz.getOffset(n) / 1000 / 3600.)
                    , canonicalId(tzId, ZoneId::of, ZoneId::getId)
                    , offset(tzId, ZoneId::of, (z, n) -> z.getRules().getOffset(Instant.ofEpochMilli(n)).getTotalSeconds() / 3600.)
                    , canonicalId(tzId, DateTimeZone::forID, DateTimeZone::getID)
                    , offset(tzId, DateTimeZone::forID, (z, n) -> z.getOffset(n) / 1000 / 3600.)
            );
        }
    }

    private static <T> String canonicalId(String zoneId, Function<String, T> ctor, Function<T, String> getId)
    {
        T zone = null;
        try {
            zone = ctor.apply(zoneId);
        }
        catch (RuntimeException e) {
            return "-- fail --";
        }
        String canonicalId = getId.apply(zone);
        return canonicalId;
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
