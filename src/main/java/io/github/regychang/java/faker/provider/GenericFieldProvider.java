package io.github.regychang.java.faker.provider;

import io.github.regychang.java.faker.Faker;
import io.github.regychang.java.faker.Options;
import jakarta.annotation.Nonnull;

import java.lang.reflect.Constructor;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

public class GenericFieldProvider extends FieldProvider<Object> {

    private final Faker faker;

    private final Constructor<?> constructor;

    private final Field[] fields;

    @SuppressWarnings("unchecked")
    public GenericFieldProvider(@Nonnull Class<?> genericType, Options options) {
        super(null, (Class<Object>) genericType, options);
        this.fields = genericType.getDeclaredFields();
        this.constructor = getConstructor(genericType);
        this.faker = new Faker();
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(
                    String.format(
                            "No public no-argument constructor of class '%s' found.",
                            clazz.getName()));
        }
    }

    @Override
    public Object provide() {
        return createInstance()
                .map(this::populateFields)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        String.format(
                                                "Failed to create instance for class '%s'.",
                                                super.rawType.getName())));
    }

    private Optional<Object> createInstance() {
        try {
            return Optional.of(constructor.newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Object populateFields(Object obj) {
        Stream.of(fields).forEach(field -> setFieldValue(obj, field));
        return obj;
    }

    private void setFieldValue(Object obj, Field field) {
        try {
            Object value = faker.fakeData(field);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
