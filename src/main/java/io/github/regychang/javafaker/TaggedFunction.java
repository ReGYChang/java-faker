package io.github.regychang.javafaker;

import java.lang.reflect.Field;

@FunctionalInterface
public interface TaggedFunction {
    Object generate(Field field) throws Exception;
}
