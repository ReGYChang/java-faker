package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class ShortFieldProvider extends NumberFieldProvider<Short> {

    private final IntegerFieldProvider fieldProvider;

    public ShortFieldProvider(Field field, Options options) {
        super(field, Short.class, options);
        fieldProvider = new IntegerFieldProvider(field, options);
    }

    @Override
    protected Short provideInternal() {
        return fieldProvider.provide().shortValue();
    }

    @Override
    protected Short cast(String value) {
        return Short.valueOf(value);
    }
}
