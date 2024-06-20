package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.provider.FieldProvider;
import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PrimitiveProvider<T> extends FieldProvider<T> {

    protected final Integer cardinality;

    protected final List<T> valueList;

    protected PrimitiveProvider(@Nullable Field field, Class<T> rawType, Options options) {
        super(field, rawType, options);
        this.cardinality = getCardinality(annotation);
        this.valueList = initializeValueList();
    }

    private int getCardinality(JFaker annotation) {
        return annotation != null ? annotation.cardinality() : -1;
    }

    private List<T> initializeValueList() {
        if (annotation == null || field == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(annotation.values())
                .map(this::cast)
                .collect(Collectors.toList());
    }

    @Override
    public T provide() {
        if (!valueList.isEmpty()) {
            if (cardinality == valueList.size()) {
                return provideWithValueList();
            }
            if (cardinality > 0) {
                return provideWithCardinality();
            }
            return provideWithValueList();
        }
        return provideInternal();
    }

    protected T provideWithCardinality() {
        T randomValue = provideInternal();
        valueList.add(randomValue);
        return randomValue;
    }

    private T provideWithValueList() {
        return valueList.get(RANDOM.nextInt(valueList.size()));
    }

    protected abstract T provideInternal();

    protected abstract T cast(String value);
}
