package io.github.regychang.java.faker.generator;

import java.io.Serializable;

@FunctionalInterface
public interface FieldGenerator<T> extends Serializable {
    T generate();
}
