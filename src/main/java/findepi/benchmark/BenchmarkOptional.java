package findepi.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static findepi.benchmark.Benchmarks.benchmark;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(2)
@Warmup(iterations = 20, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time = 500, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class BenchmarkOptional
{
    private static final int OPERATIONS_PER_INVOCATION = 1_000_000;

    @State(Scope.Thread)
    public static class BenchmarkData
    {
        private List<Optional<Duration>> optionals;

        @Setup
        public void setup()
        {
            optionals = new ArrayList<>();

            for (int i = 0; i < OPERATIONS_PER_INVOCATION; i++) {
                optionals.add(
                        ThreadLocalRandom.current().nextDouble() < 0.9
                                ? Optional.of(Duration.ofMillis(i))
                                : Optional.empty());
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(OPERATIONS_PER_INVOCATION)
    public long orElseDurationConstructor(BenchmarkData data)
    {
        long sum = 0;
        for (Optional<Duration> duration : data.optionals) {
            sum += millisWithOrElse(duration);
        }
        return sum;
    }

    private long millisWithOrElse(Optional<Duration> duration)
    {
        return duration
                .orElse(Duration.ofSeconds(13))
                .toMillis();
    }

    @Benchmark
    @OperationsPerInvocation(OPERATIONS_PER_INVOCATION)
    public long orElseGetDurationConstructor(BenchmarkData data)
    {
        long sum = 0;
        for (Optional<Duration> duration : data.optionals) {
            sum += millisWithOrElseGet(duration);
        }
        return sum;
    }

    private long millisWithOrElseGet(Optional<Duration> duration)
    {
        return duration
                .orElseGet(() -> Duration.ofSeconds(13))
                .toMillis();
    }

    public static void main(String[] args)
            throws Exception
    {
        // assure the benchmarks are valid before running
        BenchmarkData data = new BenchmarkData();
        data.setup();
        new BenchmarkOptional().orElseDurationConstructor(data);
        new BenchmarkOptional().orElseGetDurationConstructor(data);

        benchmark(BenchmarkOptional.class).run();
    }
}
