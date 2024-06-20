package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class EnumFieldProvider extends PrimitiveProvider<Enum<?>> {

    public EnumFieldProvider(Field field, Class<Enum<?>> enumType, Options options) {
        super(field, enumType, options);
    }

    @Override
    protected Enum<?> provideInternal() {
        return Optional.of(rawType)
                .map(Class::getEnumConstants)
                .map(
                        constants -> {
                            int idx = ThreadLocalRandom.current().nextInt(constants.length);
                            return constants[idx];
                        })
                .orElseThrow(
                        () ->
                                new IllegalArgumentException("Enum class must be specified in options"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Enum<?> cast(String value) {
        return Enum.valueOf((Class) rawType, value);
    }
}
