package io.github.regychang.java.faker;

import io.github.regychang.java.faker.annotation.JFaker;
import io.github.regychang.java.faker.generator.FieldGenerator;
import io.github.regychang.java.faker.provider.ArrayFieldProvider;
import io.github.regychang.java.faker.provider.CustomFieldProvider;
import io.github.regychang.java.faker.provider.DefaultFieldProvider;
import io.github.regychang.java.faker.provider.FieldProvider;
import io.github.regychang.java.faker.provider.MapFieldProvider;
import io.github.regychang.java.faker.provider.primitive.BooleanFieldProvider;
import io.github.regychang.java.faker.provider.primitive.ByteFieldProvider;
import io.github.regychang.java.faker.provider.primitive.CharacterFieldProvider;
import io.github.regychang.java.faker.provider.primitive.DoubleFieldProvider;
import io.github.regychang.java.faker.provider.primitive.EnumFieldProvider;
import io.github.regychang.java.faker.provider.primitive.FloatFieldProvider;
import io.github.regychang.java.faker.provider.GenericFieldProvider;
import io.github.regychang.java.faker.provider.primitive.InstantFieldProvider;
import io.github.regychang.java.faker.provider.primitive.IntegerFieldProvider;
import io.github.regychang.java.faker.provider.collection.ListFieldProvider;
import io.github.regychang.java.faker.provider.primitive.LongFieldProvider;
import io.github.regychang.java.faker.provider.collection.SetFieldProvider;
import io.github.regychang.java.faker.provider.primitive.ShortFieldProvider;
import io.github.regychang.java.faker.provider.primitive.StringFieldProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Faker {

    private static final Random RANDOM = new Random();

    private final ConcurrentHashMap<Integer, FieldProvider<?>> PROVIDERS = new ConcurrentHashMap<>();

    public <T> T fakeData(@Nonnull Class<T> clazz, Options options) {
        Objects.requireNonNull(clazz, "Class cannot be null");
        return getOrCreateProvider(clazz, options).provide();
    }

    @SuppressWarnings("unchecked")
    public <T> T fakeData(@Nonnull Field field, Options options) {
        Objects.requireNonNull(field, "Field cannot be null");
        return (T) getOrCreateProvider(field, options).provide();
    }

    @SuppressWarnings("unchecked")
    public <T> T fakeData(Type type) {
        return type instanceof Class ?
                fakeData((Class<T>) type) :
                (T) getOrCreateProvider((ParameterizedType) type, new Options()).provide();
    }

    public <T> T fakeData(@Nonnull Class<T> clazz) {
        return fakeData(clazz, new Options());
    }

    public <T> T fakeData(@Nonnull Field field) {
        return fakeData(field, new Options());
    }

    @SuppressWarnings("unchecked")
    public <T> FieldProvider<T> getOrCreateProvider(
            @Nonnull ParameterizedType parameterizedType, Options options) {
        Type rawType = parameterizedType.getRawType();
        return rawType instanceof Class ?
                createProvider(null, (Class<T>) rawType, parameterizedType, options) :
                getOrCreateProvider((ParameterizedType) rawType, options);
    }

    public <T> FieldProvider<T> getOrCreateProvider(@Nonnull Class<T> clazz, Options options) {
        return getOrCreateProvider(null, clazz, options);
    }

    @SuppressWarnings("unchecked")
    public <T> FieldProvider<T> getOrCreateProvider(@Nonnull Field field, Options options) {
        return getOrCreateProvider(field, (Class<T>) field.getType(), options);
    }

    @SuppressWarnings("unchecked")
    public <T> FieldProvider<T> getOrCreateProvider(
            @Nullable Field field, @Nonnull Class<T> clazz, Options options) {
        return (FieldProvider<T>) PROVIDERS.computeIfAbsent(
                getProviderKey(field, clazz),
                key -> createProvider(field, clazz, null, options));
    }

    @SuppressWarnings("unchecked")
    private <T> FieldProvider<T> createProvider(
            @Nullable Field field, Class<T> clazz, @Nullable ParameterizedType parameterizedType, Options options) {
        if (isCustomFieldProviderPresent(field)) {
            return instantiateCustomFieldProvider(field);
        }
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return (FieldProvider<T>) new BooleanFieldProvider(field, options);
        }
        if (clazz.equals(Character.class) || clazz.equals(char.class)) {
            return (FieldProvider<T>) new CharacterFieldProvider(field, options);
        }
        if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return (FieldProvider<T>) new ByteFieldProvider(field, options);
        }
        if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return (FieldProvider<T>) new ShortFieldProvider(field, options);
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return (FieldProvider<T>) new IntegerFieldProvider(field, options);
        }
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return (FieldProvider<T>) new LongFieldProvider(field, options);
        }
        if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return (FieldProvider<T>) new FloatFieldProvider(field, options);
        }
        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return (FieldProvider<T>) new DoubleFieldProvider(field, options);
        }
        if (clazz.equals(String.class)) {
            return (FieldProvider<T>) new StringFieldProvider(field, options);
        }
        if (clazz.equals(Instant.class)) {
            return (FieldProvider<T>) new InstantFieldProvider(field, options);
        }
        if (clazz.isEnum()) {
            return (FieldProvider<T>) new EnumFieldProvider(field, (Class<Enum<?>>) clazz, options);
        }
        if (clazz.equals(List.class)) {
            return (FieldProvider<T>) new ListFieldProvider<>(field, parameterizedType, options);
        }
        if (clazz.equals(Set.class)) {
            return (FieldProvider<T>) new SetFieldProvider<>(field, parameterizedType, options);
        }
        if (clazz.equals(Map.class)) {
            return (FieldProvider<T>) new MapFieldProvider(field, parameterizedType, options);
        }
        if (field != null && field.getType().isArray()) {
            return (FieldProvider<T>) new ArrayFieldProvider(field, options);
        }

        return (FieldProvider<T>) new GenericFieldProvider(clazz, options);
    }

    private int getProviderKey(Field field, Class<?> clazz) {
        return field == null ? clazz.hashCode() : field.hashCode();
    }

    private boolean isCustomFieldProviderPresent(Field field) {
        return Optional.ofNullable(field)
                .map(f -> f.getAnnotation(JFaker.class))
                .map(JFaker::provider)
                .filter(provider -> !provider.equals(DefaultFieldProvider.class))
                .isPresent();
    }

    @SuppressWarnings("unchecked")
    private <T> FieldProvider<T> instantiateCustomFieldProvider(Field field) {
        try {
            Class<? extends CustomFieldProvider<T>> providerClass =
                    (Class<? extends CustomFieldProvider<T>>) field.getAnnotation(JFaker.class).provider();
            checkTypeCompatibility(field, providerClass);
            return providerClass.getDeclaredConstructor(Field.class).newInstance(field);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to instantiate custom field provider for field '%s'. Reason: %s",
                            field.getName(),
                            e.getMessage()));
        }
    }

    private <T> void checkTypeCompatibility(
            Field field, Class<? extends CustomFieldProvider<T>> providerClass) {
        Optional<Type> elementType =
                Optional.of(providerClass)
                        .map(Class::getGenericSuperclass)
                        .filter(ParameterizedType.class::isInstance)
                        .map(ParameterizedType.class::cast)
                        .map(ParameterizedType::getActualTypeArguments)
                        .filter(args -> args.length > 0)
                        .map(args -> args[0]);

        elementType.ifPresent(
                type -> {
                    Type fieldType = field.getGenericType();
                    if (!type.equals(fieldType)) {
                        throw new IllegalArgumentException(
                                String.format(
                                        "Type mismatch between custom field provider(%s) and field(%s)",
                                        type.getTypeName(),
                                        fieldType.getTypeName()));
                    }
                });
    }

    @Deprecated
    public static void fakeData(Object obj) {
        fakeData(obj, new Options(), new HashSet<>());
    }

    @Deprecated
    public static void fakeData(Object obj, Options options) {
        fakeData(obj, options, new HashSet<>());
    }

    private static void fakeData(Object obj, Options options, Set<Object> visited) {
        if (obj == null || visited.contains(obj) || isPrimitive(obj)) {
            return;
        }

        visited.add(obj);
        Class<?> clazz = obj.getClass();

        Stream.of(clazz.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .forEach(
                        field -> {
                            field.setAccessible(true);
                            Class<?> fieldType = field.getType();
                            JFaker annotation = field.getAnnotation(JFaker.class);
                            FieldGenerator<?> fieldGenerator =
                                    Optional.ofNullable(options.getFieldProviders())
                                            .filter(providers -> annotation != null)
                                            .map(
                                                    providers ->
                                                            providers.getOrDefault(
                                                                    annotation.key(),
                                                                    null))
                                            .orElse(null);

                            try {
                                if (fieldGenerator != null) {
                                    Object value = fieldGenerator.generate();
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
    }

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

    public static <T> List<T> createList(Class<T> elementType, Options options) {
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

    public static <K, V> Map<K, V> createMap(Class<K> keyType, Class<V> valueType, Options options) {
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

    private static Object createArray(Class<?> componentType, Options options) {
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
                fieldType.equals(Long.class) || fieldType.equals(Float.class) || fieldType.equals(Double.class) ||
                fieldType.equals(Instant.class);
    }

    private static Object generateRandomPrimitive(Class<?> fieldType, Options options) {
        String typeName = fieldType.getName();

        switch (typeName) {
            case "boolean":
            case "java.lang.Boolean":
                return randomBoolean();
            case "char":
            case "java.lang.Character":
                return randomString(options).charAt(0);
            case "byte":
            case "java.lang.Byte":
                return (byte) randomInteger(options);
            case "short":
            case "java.lang.Short":
                return (short) randomInteger(options);
            case "int":
            case "java.lang.Integer":
                return randomInteger(options);
            case "long":
            case "java.lang.Long":
                return randomLong(options);
            case "float":
            case "java.lang.Float":
                return randomFloat(options);
            case "double":
            case "java.lang.Double":
                return randomDouble(options);
            case "java.lang.String":
                return randomString(options);
            case "java.time.Instant":
                return randomInstant(options);
            default:
                return null;
        }
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

    public static Instant randomInstant(Options options) {
        RandomBoundary<Instant> boundary = options.getRandomInstantBoundary();
        Instant startInstant = boundary.getStart();
        Instant endInstant = boundary.getEnd();
        long secondsDifference = startInstant.until(endInstant, ChronoUnit.SECONDS);
        return startInstant.plusSeconds(secondsDifference);
    }

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
