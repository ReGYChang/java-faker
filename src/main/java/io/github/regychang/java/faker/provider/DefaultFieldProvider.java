package io.github.regychang.java.faker.provider;

import java.lang.reflect.Field;

public final class DefaultFieldProvider extends CustomFieldProvider<Object> {

    public DefaultFieldProvider(Field field) {
        super(field);
    }

    @Override
    public Object provide() {
        throw new UnsupportedOperationException(
                "DefaultFieldProvider does not provide a default implementation.");
    }
}
