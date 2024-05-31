package io.github.regychang.java.faker;

import java.io.Serializable;
import java.lang.reflect.Field;

@FunctionalInterface
public interface TaggedFunction extends Serializable {
    Object generate(Field field) throws Exception;
}
