package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class DoubleFieldProvider extends NumberFieldProvider<Double> {

    public DoubleFieldProvider(Field field, Options options) {
        super(field, Double.class, options);
    }

    @Override
    protected Double provideInternal() {
        double span = max.doubleValue() - min.doubleValue();
        return span <= 0 ?
                min :
                min + RANDOM.nextDouble() * span;
    }

    @Override
    protected Double cast(String value) {
        return Double.valueOf(value);
    }
}
