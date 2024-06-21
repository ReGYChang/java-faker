package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class InstantFieldProvider extends PrimitiveProvider<Instant> {

    private static final Duration CENTURY = Duration.ofDays(365 * 100);

    private Instant lastTimestamp;

    private final double probability;

    public InstantFieldProvider(Field field, Options options) {
        super(field, Instant.class, options);
        this.lastTimestamp = Instant.now();
        // TODO: dynamically calculate probability
        this.probability = 1.0 / 1000;
    }

    @Override
    protected Instant provideInternal() {
        return isLinear() ? generateLinearInstant() : generateRandomInstant();
    }

    private Instant generateLinearInstant() {
        double randomValue = ThreadLocalRandom.current().nextDouble();
        if (randomValue < probability) {
            lastTimestamp = lastTimestamp.plus(1, ChronoUnit.SECONDS);
        }

        return lastTimestamp.plusMillis(ThreadLocalRandom.current().nextLong(1000));
    }

    private Instant generateRandomInstant() {
        Instant minInstant = Instant.EPOCH;
        Instant maxInstant = minInstant.plus(CENTURY);
        long millisDifference = minInstant.until(maxInstant, ChronoUnit.MILLIS);
        long randomMilliseconds = ThreadLocalRandom.current().nextLong(millisDifference);
        return minInstant.plusMillis(randomMilliseconds);
    }

    @Override
    protected Instant cast(String value) {
        return Instant.parse(value);
    }

    private boolean isLinear() {
        return isFeatureEnabled(JFaker.Feature.LinearTimestamp);
    }
}
