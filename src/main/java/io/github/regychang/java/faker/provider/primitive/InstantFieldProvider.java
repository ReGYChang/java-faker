package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;

import java.lang.reflect.Field;
import java.time.temporal.ChronoUnit;
import java.time.Instant;

public class InstantFieldProvider extends PrimitiveProvider<Instant> {

    public InstantFieldProvider(Field field, Options options) {
        super(field, Instant.class, options);
    }

    @Override
    protected Instant provideInternal() {
        RandomBoundary<Instant> boundary = options.getRandomInstantBoundary();
        Instant startInstant = boundary.getStart();
        Instant endInstant = boundary.getEnd();
        long secondsDifference = startInstant.until(endInstant, ChronoUnit.SECONDS);
        return startInstant.plusSeconds(secondsDifference);
    }

    @Override
    protected Instant cast(String value) {
        return Instant.parse(value);
    }
}
