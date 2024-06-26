package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class FloatFieldProvider extends NumberFieldProvider<Float> {

    public FloatFieldProvider(Field field, Options options) {
        super(field, Float.class, options);
    }

    @Override
    protected Float provideInternal() {
        float span = max.floatValue() - min.floatValue();
        return span <= 0 ?
                min :
                min + RANDOM.nextFloat() * span;
    }

    @Override
    protected Float cast(String value) {
        return Float.valueOf(value);
    }
}
