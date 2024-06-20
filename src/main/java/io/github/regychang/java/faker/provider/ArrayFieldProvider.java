package io.github.regychang.java.faker.provider;

import io.github.regychang.java.faker.utils.ContainerUtils;
import io.github.regychang.java.faker.Faker;
import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.utils.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.stream.IntStream;

public class ArrayFieldProvider extends FieldProvider<Object> {

    private final Faker faker;

    private final Type elementType;

    public ArrayFieldProvider(Field field, Options options) {
        super(field, null, options);
        Type genericType = field.getGenericType();
        if (genericType instanceof GenericArrayType) {
            this.elementType = ((GenericArrayType) genericType).getGenericComponentType();
        } else if (genericType instanceof Class<?>) {
            this.elementType = ((Class<?>) genericType).getComponentType();
        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported type for array field provider: '%s'", genericType));
        }
        this.faker = new Faker();
    }

    @Override
    public Object provide() {
        int length = ContainerUtils.getContainerLength(options);
        Object array = Array.newInstance(TypeUtils.getRawType(elementType), length);
        IntStream.range(0, length).forEach(idx -> setArrayElement(array, idx));
        return array;
    }

    private void setArrayElement(Object array, int idx) {
        try {
            Object element = faker.fakeData(elementType);
            Array.set(array, idx, element);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to provide element for 'array' with '%s' class.",
                            elementType.getTypeName()));
        }
    }
}
