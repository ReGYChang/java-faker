package io.github.regychang.java.faker;

import java.io.Serializable;
import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldProvider extends Serializable {
    Object generate(Field field) throws Exception;
}
