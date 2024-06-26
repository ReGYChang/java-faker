package io.github.regychang.java.faker.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    /**
     * Gets the value of a field from an object using reflection.
     *
     * @param obj       the object from which to get the field value
     * @param fieldName the name of the field
     * @param <T>       the type of the field value
     * @return the value of the field
     * @throws NoSuchFieldException   if the field does not exist
     * @throws IllegalAccessException if the field is not accessible
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }
}
