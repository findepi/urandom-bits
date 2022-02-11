package findepi.time;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Verify.verify;

public class NanoCores
{
    private static volatile long someNanoTime;

    public static void main(String[] args)
            throws Exception
    {
        int threads = Runtime.getRuntime().availableProcessors() / 2 + 1;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        try {
            CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);

            someNanoTime = System.nanoTime();
            for (int i = 0; i < threads; i++) {
                completionService.submit(() -> {
                    for (int j = 0; j < 100_000_000; j++) {
                        long received = someNanoTime;
                        long now = System.nanoTime();
                        verify(now >= received, "not monotonic");
                        someNanoTime = now;
                    }
                    return null;
                });
            }

            for (int i = 0; i < threads; i++) {
                completionService.take().get();
                System.out.println("One is done");
            }
            System.out.println("All good");
        }
        finally {
            executor.shutdownNow();
        }
    }
}
