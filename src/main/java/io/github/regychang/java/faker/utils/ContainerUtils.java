package io.github.regychang.java.faker.utils;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;

public class ContainerUtils {

    private static final Random RANDOM = new Random();

    private ContainerUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Generates a random container length within the specified range in options.
     *
     * @param options the options containing the min and max array size
     * @return a random container length
     */
    public static int getContainerLength(Options options) {
        int minSize = options.getRandomMinArraySize();
        int maxSize = options.getRandomMaxArraySize();
        if (minSize > maxSize) {
            throw new IllegalArgumentException("Min array size cannot be greater than max array size");
        }
        return RANDOM.nextInt(maxSize - minSize + 1) + minSize;
    }

    public static Type getElementType(Field field, ParameterizedType parameterizedType, int index) {
        return field == null ?
                parameterizedType.getActualTypeArguments()[index] :
                TypeUtils.getTypeArguments(field)[index];
    }
}
