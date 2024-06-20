package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;

import java.lang.reflect.Field;

public class DoubleFieldProvider extends PrimitiveProvider<Double> {

    public DoubleFieldProvider(Field field, Options options) {
        super(field, Double.class, options);
    }

    @Override
    protected Double provideInternal() {
        RandomBoundary<Double> boundary = options.getRandomDoubleBoundary();
        double span = boundary.getEnd() - boundary.getStart();
        return span <= 0 ?
                boundary.getStart() :
                boundary.getStart() + RANDOM.nextDouble() * span;
    }

    @Override
    protected Double cast(String value) {
        return Double.valueOf(value);
    }
}
