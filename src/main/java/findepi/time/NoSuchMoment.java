package findepi.time;

import org.joda.time.DateTimeZone;

public class NoSuchMoment
{
    public static void main(String[] args)
    {
        // "2018-03-25T02:17:17.000+02:00"
        System.out.println(org.joda.time.DateTime.parse("2018-03-25T02:17:17+00:00")
                .withZoneRetainFields(DateTimeZone.forID("Europe/Vilnius")));

        // "2018-03-25T04:17:17.000+03:00"
        System.out.println(org.joda.time.DateTime.parse("2018-03-25T03:17:17+00:00")
                .withZoneRetainFields(DateTimeZone.forID("Europe/Vilnius")));
    }
}
