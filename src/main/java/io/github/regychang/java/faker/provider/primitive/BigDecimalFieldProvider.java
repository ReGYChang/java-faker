package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalFieldProvider extends NumberFieldProvider<BigDecimal> {

    private final int scale;

    private final BigDecimal span;

    private final BigDecimal minDecimal;

    public BigDecimalFieldProvider(Field field, Options options) {
        super(field, BigDecimal.class, options);
        this.scale = determineScale();
        this.minDecimal = BigDecimal.valueOf(min).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal maxDecimal = BigDecimal.valueOf(max).setScale(2, RoundingMode.HALF_DOWN);
        this.span = maxDecimal.subtract(minDecimal);
    }

    @Override
    protected BigDecimal provideInternal() {
        if (span.compareTo(BigDecimal.ZERO) <= 0) {
            return minDecimal;
        }

        BigDecimal randomFactor = BigDecimal.valueOf(RANDOM.nextDouble());
        return minDecimal.add(span.multiply(randomFactor))
                .setScale(scale, RoundingMode.HALF_UP);
    }

    @Override
    protected BigDecimal cast(String value) {
        return new BigDecimal(value);
    }

    // TODO: implement custom logic to determine the scale based on field annotations
    private int determineScale() {
        return 2;
    }
}
