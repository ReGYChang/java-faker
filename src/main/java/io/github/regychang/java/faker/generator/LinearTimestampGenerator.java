package io.github.regychang.java.faker.generator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class LinearTimestampGenerator implements FieldGenerator<Instant> {

    private final double probability;

    private Instant lastTimestamp;

    public LinearTimestampGenerator(int recordsPerSecond) {
        this(Instant.now(), recordsPerSecond);
    }

    public LinearTimestampGenerator(Instant lastTimestamp, int recordsPerSecond) {
        if (recordsPerSecond <= 0) {
            throw new IllegalArgumentException("recordsPerSecond must be greater than 0");
        }
        this.probability = 1.0 / recordsPerSecond;
        this.lastTimestamp = lastTimestamp;
    }

    @Override
    public Instant generate() {
        double randomValue = ThreadLocalRandom.current().nextDouble();
        if (randomValue < probability) {
            lastTimestamp = lastTimestamp.plus(1, ChronoUnit.SECONDS);
        }

        return lastTimestamp;
    }
}
