package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class IntegerFieldProvider extends NumberFieldProvider<Integer> {

    public IntegerFieldProvider(Field field, Options options) {
        super(field, Integer.class, options);
    }

    @Override
    protected Integer provideInternal() {
        int span = max - min;
        return span <= 0 ?
                min :
                min + RANDOM.nextInt(span);
    }

    @Override
    protected Integer cast(String value) {
        return Integer.valueOf(value);
    }
}
