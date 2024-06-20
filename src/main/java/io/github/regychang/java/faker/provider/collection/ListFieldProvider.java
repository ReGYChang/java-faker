package io.github.regychang.java.faker.provider.collection;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

public class ListFieldProvider<E> extends CollectionFieldProvider<E> {

    public ListFieldProvider(Field field, ParameterizedType parameterizedType, Options options) {
        super(field, parameterizedType, options);
    }

    @Override
    protected Collection<E> instantiateCollection(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }
}
