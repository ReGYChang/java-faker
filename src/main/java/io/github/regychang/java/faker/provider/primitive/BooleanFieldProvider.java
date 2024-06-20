package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class BooleanFieldProvider extends PrimitiveProvider<Boolean> {

    public BooleanFieldProvider(Field field, Options options) {
        super(field, Boolean.class, options);
    }

    @Override
    protected Boolean provideInternal() {
        return RANDOM.nextBoolean();
    }

    @Override
    protected Boolean cast(String value) {
        return Boolean.valueOf(value);
    }
}
