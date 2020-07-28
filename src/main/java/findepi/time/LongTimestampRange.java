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
