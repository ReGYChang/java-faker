package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;

public class CharacterFieldProvider extends PrimitiveProvider<Character> {

    private final StringFieldProvider fieldProvider;

    public CharacterFieldProvider(Field field, Options options) {
        super(field, Character.class, options);
        fieldProvider = new StringFieldProvider(field, options);
    }

    @Override
    protected Character provideInternal() {
        return fieldProvider.provide().charAt(0);
    }

    @Override
    protected Character cast(String value) {
        return value.charAt(0);
    }
}
