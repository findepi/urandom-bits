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
