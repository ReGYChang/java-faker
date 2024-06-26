package io.github.regychang.java.faker.annotation;

import io.github.regychang.java.faker.provider.CustomFieldProvider;
import io.github.regychang.java.faker.provider.DefaultFieldProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFaker {

    String key() default "";

    String format() default "";

    int length() default 25;

    int min() default 1;

    int max() default 100;

    String[] values() default {};

    int cardinality() default -1;

    Feature[] features() default {};

    Class<? extends CustomFieldProvider> provider() default DefaultFieldProvider.class;

    enum Feature {

        LinearTimestamp(1L);

        public final long mask;

        Feature(long mask) {
            this.mask = mask;
        }

        public static long of(Feature[] features) {
            return features == null ?
                    0L :
                    Arrays.stream(features)
                            .mapToLong(feature -> feature.mask)
                            .reduce(
                                    0L,
                                    (a, b) -> a | b);
        }
    }
}
