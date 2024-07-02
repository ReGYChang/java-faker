package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class LocalDateTimeFieldProvider extends TemporalFieldProvider<LocalDateTime> {

    public LocalDateTimeFieldProvider(Field field, Options options) {
        super(field, LocalDateTime.class, options);
    }

    @Override
    protected LocalDateTime getCurrentTemporal() {
        return LocalDateTime.now();
    }

    @Override
    protected LocalDateTime plusDays(LocalDateTime temporal, long days) {
        return temporal.plusDays(days);
    }

    @Override
    protected LocalDateTime cast(String value) {
        return LocalDateTime.parse(value);
    }
}
