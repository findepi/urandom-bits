package findepi.benchmark;

import org.intellij.lang.annotations.Language;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.runner.options.WarmupMode;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Objects.requireNonNull;

// Copied from https://github.com/trinodb/trino/blob/332c4529401d64bff17fdf59574bf09f232675b9/testing/trino-testing-services/src/main/java/io/trino/jmh/Benchmarks.java
public final class Benchmarks
{
    private Benchmarks() {}

    public static BenchmarkBuilder benchmark(Class<?> benchmarkClass)
    {
        ChainedOptionsBuilder optionsBuilder = new OptionsBuilder()
                .verbosity(VerboseMode.NORMAL)
                .resultFormat(ResultFormatType.JSON)
                .result(format("%s/%s-result-%s.json", System.getProperty("java.io.tmpdir"), benchmarkClass.getSimpleName(), ISO_DATE_TIME.format(LocalDateTime.now())));
        return new BenchmarkBuilder(optionsBuilder, benchmarkClass);
    }

    public static BenchmarkBuilder benchmark(Class<?> benchmarkClass, WarmupMode warmupMode)
    {
        return benchmark(benchmarkClass)
                .withOptions(optionsBuilder -> optionsBuilder.warmupMode(warmupMode));
    }

    public static class BenchmarkBuilder
    {
        private final ChainedOptionsBuilder optionsBuilder;
        private final Class<?> benchmarkClass;

        private BenchmarkBuilder(ChainedOptionsBuilder optionsBuilder, Class<?> benchmarkClass)
        {
            this.optionsBuilder = requireNonNull(optionsBuilder, "optionsBuilder is null");
            this.benchmarkClass = requireNonNull(benchmarkClass, "benchmarkClass is null");
        }

        public BenchmarkBuilder withOptions(Consumer<ChainedOptionsBuilder> optionsConsumer)
        {
            optionsConsumer.accept(optionsBuilder);
            return this;
        }

        public BenchmarkBuilder includeAll()
        {
            optionsBuilder.include("^\\Q" + benchmarkClass.getName() + ".\\E");
            return this;
        }

        public BenchmarkBuilder includeMethod(@Language("RegExp") String benchmarkMethod)
        {
            optionsBuilder.include("^\\Q" + benchmarkClass.getName() + ".\\E(" + benchmarkMethod + ")$");
            return this;
        }

        public Collection<RunResult> run()
                throws RunnerException
        {
            if (optionsBuilder.build().getIncludes().isEmpty()) {
                includeAll();
            }
            return new Runner(optionsBuilder.build()).run();
        }
    }
}
