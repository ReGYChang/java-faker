package io.github.regychang.java.faker;

import io.github.regychang.java.faker.provider.FieldProvider;

public class TestFieldProvider extends FieldProvider<Integer> {

    public TestFieldProvider() {
        super(null, Integer.class, new Options());
    }

    @Override
    public Integer provide() {
        return 42;
    }
}
