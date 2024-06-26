package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class LongFieldProvider extends NumberFieldProvider<Long> {

    public LongFieldProvider(Field field, Options options) {
        super(field, Long.class, options);
    }

    @Override
    protected Long provideInternal() {
        int span = max - min;
        return min + Math.abs(RANDOM.nextLong()) % (span + 1L);
    }

    @Override
    protected Long cast(String value) {
        return Long.valueOf(value);
    }
}
