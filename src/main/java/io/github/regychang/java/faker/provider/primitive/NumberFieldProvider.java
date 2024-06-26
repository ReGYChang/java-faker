package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.Optional;

public abstract class NumberFieldProvider<T extends Number> extends PrimitiveProvider<T> {

    protected final Integer min;

    protected final Integer max;

    @SuppressWarnings("unchecked")
    protected NumberFieldProvider(@Nullable Field field, Class<? extends Number> rawType, Options options) {
        super(field, (Class<T>) rawType, options);
        this.min = Optional.ofNullable(annotation).map(JFaker::min).orElse(options.getRandomBoundary().getStart());
        this.max = Optional.ofNullable(annotation).map(JFaker::max).orElse(options.getRandomBoundary().getEnd());
    }
}
