package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class ByteFieldProvider extends NumberFieldProvider<Byte> {

    private final IntegerFieldProvider fieldProvider;

    public ByteFieldProvider(Field field, Options options) {
        super(field, Byte.class, options);
        fieldProvider = new IntegerFieldProvider(field, options);
    }

    @Override
    protected Byte provideInternal() {
        return fieldProvider.provide().byteValue();
    }

    @Override
    protected Byte cast(String value) {
        return Byte.valueOf(value);
    }
}
