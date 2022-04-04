package findepi.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class WeakValuesCache
{
    public static void main(String[] args)
    {
        // run me with little memory

        Cache<Integer, String> cache = CacheBuilder.newBuilder()
                .weakValues()
                .build();

        for (int i = 0; i < 10_000_000; i++) {
            cache.put(i, String.valueOf(i));
        }

        System.out.println(cache.asMap().hashCode());
    }
}
