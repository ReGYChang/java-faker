package io.github.regychang.java.faker.provider.collection;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;

public class SetFieldProvider<E> extends CollectionFieldProvider<E> {

    public SetFieldProvider(Field field, ParameterizedType parameterizedType, Options options) {
        super(field, parameterizedType, options);
    }

    @Override
    protected Collection<E> instantiateCollection(int initialCapacity) {
        return new HashSet<>(initialCapacity);
    }
}
