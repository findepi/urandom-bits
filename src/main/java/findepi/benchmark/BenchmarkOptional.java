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
import java.util.TimeZone;
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
        private List<Optional<Duration>> durations;
        private List<Optional<String>> strings;

        @Setup
        public void setup()
        {
            durations = new ArrayList<>(OPERATIONS_PER_INVOCATION);
            strings = new ArrayList<>(OPERATIONS_PER_INVOCATION);

            for (int i = 0; i < OPERATIONS_PER_INVOCATION; i++) {
                durations.add(
                        ThreadLocalRandom.current().nextDouble() < 0.9
                                ? Optional.of(Duration.ofMillis(i))
                                : Optional.empty());

                strings.add(
                        ThreadLocalRandom.current().nextDouble() < 0.9
                                ? Optional.of(Integer.toString(i))
                                : Optional.empty());
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(OPERATIONS_PER_INVOCATION)
    public long orElseDurationConstructor(BenchmarkData data)
    {
        long sum = 0;
        for (Optional<Duration> duration : data.durations) {
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
        for (Optional<Duration> duration : data.durations) {
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

    @Benchmark
    @OperationsPerInvocation(OPERATIONS_PER_INVOCATION)
    public long orElseDefaultTimeZoneId(BenchmarkData data)
    {
        long sum = 0;
        for (Optional<String> string : data.strings) {
            sum += defaultTimeZoneIdWithOrElse(string).length();
        }
        return sum;
    }

    private String defaultTimeZoneIdWithOrElse(Optional<String> string)
    {
        return string
                .orElse(TimeZone.getDefault().getID());
    }

    @Benchmark
    @OperationsPerInvocation(OPERATIONS_PER_INVOCATION)
    public long orElseGetDefaultTimeZoneId(BenchmarkData data)
    {
        long sum = 0;
        for (Optional<String> string : data.strings) {
            sum += defaultTimeZoneIdWithOrElseGet(string).length();
        }
        return sum;
    }

    private String defaultTimeZoneIdWithOrElseGet(Optional<String> string)
    {
        return string
                .orElseGet(() -> TimeZone.getDefault().getID());
    }

    public static void main(String[] args)
            throws Exception
    {
        // assure the benchmarks are valid before running
        BenchmarkData data = new BenchmarkData();
        data.setup();
        new BenchmarkOptional().orElseDurationConstructor(data);
        new BenchmarkOptional().orElseGetDurationConstructor(data);
        new BenchmarkOptional().orElseDefaultTimeZoneId(data);
        new BenchmarkOptional().orElseGetDefaultTimeZoneId(data);

        benchmark(BenchmarkOptional.class)
                // Optional filters, e.g.
                // .includeMethod(".*DefaultTimeZoneId")
                .run();
    }
}
