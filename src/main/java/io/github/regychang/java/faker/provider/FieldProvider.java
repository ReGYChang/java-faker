package io.github.regychang.java.faker.provider;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;

public abstract class FieldProvider<T> implements Serializable {

    protected static final Random RANDOM = new Random();

    protected final Field field;

    protected final Class<T> rawType;

    @Nullable
    protected final JFaker annotation;

    protected final long fakerFeatures;

    protected final Options options;

    protected FieldProvider(Field field, Class<T> rawType, Options options) {
        this.field = field;
        this.rawType = rawType;
        this.annotation = instantiateAnnotation(field);
        this.fakerFeatures = instantiateFakerFeatures(annotation);
        this.options = options;
    }

    public abstract T provide();

    protected boolean isFeatureEnabled(JFaker.Feature feature) {
        return (fakerFeatures & feature.mask) != 0;
    }

    private JFaker instantiateAnnotation(Field field) {
        return Optional.ofNullable(field)
                .map(f -> f.getAnnotation(JFaker.class))
                .orElse(null);
    }

    private long instantiateFakerFeatures(JFaker annotation) {
        return Optional.ofNullable(annotation)
                .map(JFaker::features)
                .map(JFaker.Feature::of)
                .orElse(0L);
    }
}
