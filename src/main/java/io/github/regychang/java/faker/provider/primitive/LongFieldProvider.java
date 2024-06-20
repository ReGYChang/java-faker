package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;

import java.lang.reflect.Field;

public class LongFieldProvider extends PrimitiveProvider<Long> {

    public LongFieldProvider(Field field, Options options) {
        super(field, Long.class, options);
    }

    @Override
    protected Long provideInternal() {
        RandomBoundary<Integer> boundary = options.getRandomIntegerBoundary();
        int span = boundary.getEnd() - boundary.getStart();
        return boundary.getStart() +
                Math.abs(RANDOM.nextLong()) % (span + 1L);
    }

    @Override
    protected Long cast(String value) {
        return Long.valueOf(value);
    }
}
