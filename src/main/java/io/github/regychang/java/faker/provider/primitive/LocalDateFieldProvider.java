package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class LocalDateFieldProvider extends TemporalFieldProvider<LocalDate> {

    public LocalDateFieldProvider(Field field, Options options) {
        super(field, LocalDate.class, options);
    }

    @Override
    protected LocalDate getCurrentTemporal() {
        return LocalDate.now();
    }

    @Override
    protected LocalDate plusDays(LocalDate temporal, long days) {
        return temporal.plusDays(days);
    }

    @Override
    protected LocalDate cast(String value) {
        return LocalDate.parse(value);
    }
}
