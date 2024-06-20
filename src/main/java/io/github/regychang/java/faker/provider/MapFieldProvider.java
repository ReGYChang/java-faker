package io.github.regychang.java.faker.provider;

import io.github.regychang.java.faker.utils.ContainerUtils;
import io.github.regychang.java.faker.Faker;
import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class MapFieldProvider extends FieldProvider<Map<?, ?>> {

    private final Faker faker;

    private final Type keyType;

    private final Type valueType;

    public MapFieldProvider(Field field, ParameterizedType parameterizedType, Options options) {
        super(field, null, options);
        this.faker = new Faker();
        this.keyType = ContainerUtils.getElementType(field, parameterizedType, 0);
        this.valueType = ContainerUtils.getElementType(field, parameterizedType, 1);
    }

    @Override
    public Map<?, ?> provide() {
        int length = ContainerUtils.getContainerLength(options);
        HashMap<Object, Object> map = new HashMap<>(length);
        IntStream
                .range(0, length)
                .forEach(
                        idx ->
                                map.put(faker.fakeData(keyType), faker.fakeData(valueType)));

        return map;
    }
}
