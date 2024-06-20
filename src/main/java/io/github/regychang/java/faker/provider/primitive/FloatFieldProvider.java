package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;

import java.lang.reflect.Field;

public class FloatFieldProvider extends PrimitiveProvider<Float> {

    public FloatFieldProvider(Field field, Options options) {
        super(field, Float.class, options);
    }

    @Override
    protected Float provideInternal() {
        RandomBoundary<Float> boundary = options.getRandomFloatBoundary();
        float span = boundary.getEnd() - boundary.getStart();
        return span <= 0 ?
                boundary.getStart() :
                boundary.getStart() + RANDOM.nextFloat() * span;
    }

    @Override
    protected Float cast(String value) {
        return Float.valueOf(value);
    }
}
