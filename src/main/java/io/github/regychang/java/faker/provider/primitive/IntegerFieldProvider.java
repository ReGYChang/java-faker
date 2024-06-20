package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;

import java.lang.reflect.Field;

public class IntegerFieldProvider extends PrimitiveProvider<Integer> {

    public IntegerFieldProvider(Field field, Options options) {
        super(field, Integer.class, options);
    }

    @Override
    protected Integer provideInternal() {
        RandomBoundary<Integer> boundary = options.getRandomIntegerBoundary();
        int span = boundary.getEnd() - boundary.getStart();
        return span <= 0 ?
                boundary.getStart() : RANDOM.nextInt(span);
    }

    @Override
    protected Integer cast(String value) {
        return Integer.valueOf(value);
    }
}
