package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateFieldProvider extends PrimitiveProvider<Date> {

    private static final ZoneId UTC = ZoneId.of("UTC");

    private final LocalDateFieldProvider localDateProvider;

    public DateFieldProvider(Field field, Options options) {
        super(field, Date.class, options);
        this.localDateProvider = new LocalDateFieldProvider(field, options);
    }

    @Override
    protected Date provideInternal() {
        LocalDate localDate = localDateProvider.provideInternal();
        return Date.from(localDate.atStartOfDay(UTC).toInstant());
    }

    @Override
    protected Date cast(String value) {
        LocalDate localDate = LocalDate.parse(value);
        return Date.from(localDate.atStartOfDay(UTC).toInstant());
    }
}
