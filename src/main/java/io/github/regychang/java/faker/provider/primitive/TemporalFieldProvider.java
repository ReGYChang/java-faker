package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;

public abstract class TemporalFieldProvider<T extends Temporal> extends PrimitiveProvider<T> {

    protected final int minDays;

    protected final int maxDays;

    protected TemporalFieldProvider(Field field, Class<T> type, Options options) {
        super(field, type, options);
        this.minDays = getMinDays();
        this.maxDays = getMaxDays();
    }

    protected abstract T getCurrentTemporal();

    protected abstract T plusDays(T temporal, long days);

    @Override
    protected T provideInternal() {
        if (minDays == maxDays) {
            return plusDays(getCurrentTemporal(), minDays);
        }
        long randomDays = minDays + (long) (Math.random() * (maxDays - minDays + 1));
        return plusDays(getCurrentTemporal(), randomDays);
    }

    private int getMinDays() {
        return annotation == null ? -365 : annotation.min();
    }

    private int getMaxDays() {
        return annotation == null ? 365 : annotation.max();
    }
}
