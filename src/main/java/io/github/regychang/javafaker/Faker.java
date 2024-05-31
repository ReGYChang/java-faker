package io.github.regychang.javafaker;

import io.github.regychang.javafaker.annotation.JFaker;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Faker {

    private static final Random RANDOM = new Random();

    public static void fakeData(Object obj) throws Exception {
        fakeData(obj, new Options(), new HashSet<>());
    }

    public static void fakeData(Object obj, Options options) throws Exception {
        fakeData(obj, options, new HashSet<>());
    }

    private static void fakeData(Object obj, Options options, Set<Object> visited) throws Exception {
        if (obj == null || visited.contains(obj) || isPrimitive(obj)) {
            return;
        }

        visited.add(obj);
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isSynthetic()) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                TaggedFunction provider = getProviderByTag(field, options.getFieldProviders());

                try {
                    if (provider != null) {
                        Object value = provider.generate(field);
                        field.set(obj, value);
                    } else if (isPrimitive(fieldType)) {
                        field.set(obj, generateRandomPrimitive(fieldType, options));
                    } else if (fieldType.isArray()) {
                        Object array = createArray(fieldType.getComponentType(), options);
                        field.set(obj, array);
                    } else if (field.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        Object parameterizedInstance = createParameterizedTypeInstance(parameterizedType, options);
                        field.set(obj, parameterizedInstance);
                    } else {
                        Object fieldValue = createInstance(fieldType, options);
                        fakeData(fieldValue, options, visited);
                        field.set(obj, fieldValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static TaggedFunction getProviderByTag(Field field, Map<String, TaggedFunction> providers) {
        return field.isAnnotationPresent(JFaker.class) &&
                providers.containsKey(field.getAnnotation(JFaker.class).value()) ?
                providers.get(field.getAnnotation(JFaker.class).value()) : null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T createInstance(Class<T> clazz, Options options) {
        try {
            if (isPrimitive(clazz)) {
                return (T) generateRandomPrimitive(clazz, options);
            }
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create instance", e);
        }
    }

    private static Object createParameterizedTypeInstance(ParameterizedType parameterizedType, Options options) {
        try {
            Type rawType = parameterizedType.getRawType();

            if (rawType == List.class) {
                Type elementType = parameterizedType.getActualTypeArguments()[0];
                if (elementType instanceof Class) {
                    return createList((Class<?>) elementType, options);
                } else if (elementType instanceof ParameterizedType) {
                    return createList((Class<?>) ((ParameterizedType) elementType).getRawType(), options);
                }
            } else if (rawType == Map.class) {
                Type keyType = parameterizedType.getActualTypeArguments()[0];
                Type valueType = parameterizedType.getActualTypeArguments()[1];

                if (keyType instanceof Class && valueType instanceof Class) {
                    return createMap(
                            (Class<?>) keyType,
                            (Class<?>) valueType,
                            options);
                } else if (keyType instanceof Class && valueType instanceof ParameterizedType) {
                    return createMap(
                            (Class<?>) keyType,
                            createParameterizedTypeInstance((ParameterizedType) valueType, options),
                            options);
                }
            }

            throw new UnsupportedOperationException("Unsupported parameterized type: " + parameterizedType);
        } catch (Exception e) {
            throw new RuntimeException("Could not create parameterized type instance", e);
        }
    }

    public static <T> List<T> createList(Class<T> elementType, Options options) throws Exception {
        int length =
                RANDOM.nextInt(options.getRandomMaxArraySize() - options.getRandomMinArraySize() + 1) +
                        options.getRandomMinArraySize();
        List<T> list = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            T element = createInstance(elementType, options);
            fakeData(element, options);
            list.add(element);
        }

        return list;
    }

    public static <K, V> Map<K, V> createMap(Class<K> keyType, Class<V> valueType, Options options) throws Exception {
        int length =
                RANDOM.nextInt(options.getRandomMaxArraySize() - options.getRandomMinArraySize() + 1) +
                        options.getRandomMinArraySize();
        Map<K, V> map = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            K key = createInstance(keyType, options);
            V value = createInstance(valueType, options);
            fakeData(key);
            fakeData(value);
            map.put(key, value);
        }

        return map;
    }

    public static <K> Map<K, Object> createMap(Class<K> keyType, Object value, Options options) {
        int length =
                RANDOM.nextInt(options.getRandomMaxArraySize() - options.getRandomMinArraySize() + 1) +
                        options.getRandomMinArraySize();
        Map<K, Object> map = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            K key = createInstance(keyType, options);
            map.put(key, value);
        }

        return map;
    }

    private static Object createArray(Class<?> componentType, Options options) throws Exception {
        int length = RANDOM.nextInt(options.getRandomMaxArraySize()) + 1;
        Object array = java.lang.reflect.Array.newInstance(componentType, length);

        for (int i = 0; i < length; i++) {
            Object element = createInstance(componentType, options);
            fakeData(element);
            java.lang.reflect.Array.set(array, i, element);
        }

        return array;
    }

    private static boolean isPrimitive(Object obj) {
        return isPrimitive(obj.getClass());
    }

    private static boolean isPrimitive(Class<?> fieldType) {
        return fieldType.isPrimitive() ||
                fieldType.equals(String.class) || fieldType.equals(Boolean.class) || fieldType.equals(Byte.class) ||
                fieldType.equals(Character.class) || fieldType.equals(Short.class) || fieldType.equals(Integer.class) ||
                fieldType.equals(Long.class) || fieldType.equals(Float.class) || fieldType.equals(Double.class);
    }

    private static Object generateRandomPrimitive(Class<?> fieldType, Options options) {
        if (fieldType == boolean.class || fieldType.equals(Boolean.class)) {
            return randomBoolean();
        } else if (fieldType == char.class || fieldType.equals(Character.class)) {
            return randomString(options).charAt(0);
        } else if (fieldType == byte.class || fieldType.equals(Byte.class)) {
            return (byte) randomInteger(options);
        } else if (fieldType == short.class || fieldType.equals(Short.class)) {
            return (short) randomInteger(options);
        } else if (fieldType == int.class || fieldType.equals(Integer.class)) {
            return randomInteger(options);
        } else if (fieldType == long.class || fieldType.equals(Long.class)) {
            return randomLong(options);
        } else if (fieldType == float.class || fieldType.equals(Float.class)) {
            return randomFloat(options);
        } else if (fieldType == double.class || fieldType.equals(Double.class)) {
            return randomDouble(options);
        } else if (fieldType == String.class) {
            return randomString(options);
        }
        return null;
    }

    public static String randomString(Options options) {
        int length = options.getRandomStringLength();
        String characterSet = options.getRandomStringCharacterSet();

        if (characterSet == null || characterSet.isEmpty()) {
            characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(characterSet.length());
            sb.append(characterSet.charAt(index));
        }

        return sb.toString();
    }

    public static int randomInteger(Options options) {
        RandomBoundary<Integer> boundary = options.getRandomIntegerBoundary();
        int span = boundary.getEnd() - boundary.getStart();
        return span <= 0 ?
                boundary.getStart() : RANDOM.nextInt(span);
    }

    public static long randomLong(Options options) {
        RandomBoundary<Integer> boundary = options.getRandomIntegerBoundary();
        int span = boundary.getEnd() - boundary.getStart();
        return boundary.getStart() + Math.abs(RANDOM.nextLong()) % (span + 1L);
    }


    public static float randomFloat(Options options) {
        RandomBoundary<Float> boundary = options.getRandomFloatBoundary();
        float span = boundary.getEnd() - boundary.getStart();
        return span <= 0 ?
                boundary.getStart() : boundary.getStart() + RANDOM.nextFloat() * span;
    }

    public static double randomDouble(Options options) {
        RandomBoundary<Double> boundary = options.getRandomDoubleBoundary();
        double span = boundary.getEnd() - boundary.getStart();
        return span <= 0 ?
                boundary.getStart() : boundary.getStart() + RANDOM.nextDouble() * span;
    }

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T randomEnum(Options options) {
        Class<T> enumClass = (Class<T>) options.getRandomEnumClass();
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class must be specified in options");
        }
        T[] enumConstants = enumClass.getEnumConstants();
        int index = ThreadLocalRandom.current().nextInt(enumConstants.length);
        return enumConstants[index];
    }

    /**
     * Generates a random LocalDateTime instance within the specified range.
     *
     * @param startDateTime The start of the range, inclusive.
     * @param endDateTime   The end of the range, exclusive.
     * @return A random LocalDateTime instance within the specified range.
     */
    public static LocalDateTime randomLocalDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        long secondsDifference = startDateTime.until(endDateTime, ChronoUnit.SECONDS);
        long randomSeconds = ThreadLocalRandom.current().nextLong(secondsDifference);

        return startDateTime.plusSeconds(randomSeconds);
    }

    public static String randomNullableString(Options options) {
        double nullProbability = options.getNullProbability();
        if (ThreadLocalRandom.current().nextDouble() < nullProbability) {
            return null;
        } else {
            return randomString(options);
        }
    }
}
