package io.github.regychang.java.faker.provider.collection;

import io.github.regychang.java.faker.utils.ContainerUtils;
import io.github.regychang.java.faker.Faker;
import io.github.regychang.java.faker.provider.FieldProvider;
import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class CollectionFieldProvider<E> extends FieldProvider<Collection<E>> {

    protected final Faker faker;

    protected final Type elementType;

    public CollectionFieldProvider(Field field, ParameterizedType parameterizedType, Options options) {
        super(field, null, options);
        this.elementType = ContainerUtils.getElementType(field, parameterizedType, 0);
        this.faker = new Faker();
    }

    @Override
    public Collection<E> provide() {
        int length = ContainerUtils.getContainerLength(options);
        return IntStream.range(0, length)
                .mapToObj(idx -> safelyProvideElement())
                .collect(
                        Collectors.toCollection(
                                () -> instantiateCollection(length)));
    }

    private E safelyProvideElement() {
        try {
            return faker.fakeData(elementType);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to provide element for '%s' with '%s' class.",
                            field.getType().getName(),
                            elementType.getTypeName()));
        }
    }

    protected abstract Collection<E> instantiateCollection(int initialCapacity);
}
