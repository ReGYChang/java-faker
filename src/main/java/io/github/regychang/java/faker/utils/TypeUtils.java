package io.github.regychang.java.faker.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class TypeUtils {

    private TypeUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Checks if a field is parameterized.
     *
     * @param field the field to check
     * @return true if the field is parameterized, false otherwise
     * @throws IllegalArgumentException if the field is null
     */
    public static boolean isParameterizedType(Field field) {
        return Optional.ofNullable(field)
                .map(f -> f.getGenericType() instanceof ParameterizedType)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException("Field cannot be null"));
    }

    public static boolean isParameterizedType(Type type) {
        return Optional.ofNullable(type)
                .map(t -> t instanceof ParameterizedType)
                .orElseThrow(
                        () ->
                                new IllegalArgumentException("Type cannot be null"));
    }

    /**
     * Retrieves the parameterized type of field.
     *
     * @param field the field to inspect
     * @return the parameterized type of the field
     * @throws IllegalArgumentException if the field is not parameterized
     */
    public static ParameterizedType getParameterizedType(Field field) {
        return Optional.ofNullable(field)
                .filter(TypeUtils::isParameterizedType)
                .map(f -> (ParameterizedType) f.getGenericType())
                .orElseThrow(
                        () ->
                                new IllegalArgumentException("Field is not parameterized"));
    }

    /**
     * Retrieves the type arguments of a parameterized field.
     *
     * @param field the field to inspect
     * @return an array of type arguments
     * @throws IllegalArgumentException if the field is not parameterized
     */
    public static Type[] getTypeArguments(Field field) {
        return getParameterizedType(field).getActualTypeArguments();
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
