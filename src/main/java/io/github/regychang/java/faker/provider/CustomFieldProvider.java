package io.github.regychang.java.faker.provider;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public abstract class CustomFieldProvider<T> extends FieldProvider<T> {

    @SuppressWarnings("unchecked")
    protected CustomFieldProvider(Field field) {
        super(field, (Class<T>) field.getType(), new Options());
    }
}
