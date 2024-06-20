package io.github.regychang.java.faker.annotation;

import io.github.regychang.java.faker.provider.FieldProvider;
import io.github.regychang.java.faker.provider.GenericFieldProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFaker {

    String key() default "";

    String format() default "";

    int length() default 25;

    String[] values() default {};

    int cardinality() default -1;

    Class<? extends FieldProvider<?>> fieldProvider() default GenericFieldProvider.class;
}
