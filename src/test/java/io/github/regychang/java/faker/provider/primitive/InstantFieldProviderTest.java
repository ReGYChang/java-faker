package io.github.regychang.java.faker.provider.primitive;

import static org.junit.jupiter.api.Assertions.*;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import io.github.regychang.java.faker.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.*;

public class InstantFieldProviderTest {

    private static final String INSTANT_FIELD = "instantField";

    private static final String LINEAR_INSTANT_FIELD = "linearInstantField";

    private static final String LAST_TIMESTAMP_FIELD = "lastTimestamp";

    private static final String PROBABILITY_FIELD = "probability";

    private Options options;

    private Field instantField;

    private Field linearInstantField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        options = mock(Options.class);
        instantField = TestClass.class.getDeclaredField(INSTANT_FIELD);
        linearInstantField = TestClass.class.getDeclaredField(LINEAR_INSTANT_FIELD);
    }

    @Test
    public void testInitializationWithDefaultOptions() throws Exception {
        InstantFieldProvider provider = new InstantFieldProvider(linearInstantField, options);

        assertNotNull(provider);
        assertNotNull(ReflectionUtils.getFieldValue(provider, LAST_TIMESTAMP_FIELD));
        assertEquals(1.0 / 1000, ReflectionUtils.getFieldValue(provider, PROBABILITY_FIELD));
    }

    @Test
    public void testProvideMethodForLinearInstantGeneration() throws Exception {
        InstantFieldProvider provider = new InstantFieldProvider(linearInstantField, options);

        Instant initialTimestamp = ReflectionUtils.getFieldValue(provider, LAST_TIMESTAMP_FIELD);
        Instant generatedInstant = provider.provide();

        assertTrue(generatedInstant.isAfter(initialTimestamp));
    }

    @Test
    public void testProvideMethodForRandomInstantGeneration() {
        InstantFieldProvider provider = new InstantFieldProvider(instantField, options);

        Instant generatedInstant = provider.provide();

        Instant minInstant = Instant.EPOCH;
        Instant maxInstant = minInstant.plus(365 * 100, ChronoUnit.DAYS);

        assertTrue(generatedInstant.isAfter(minInstant) && generatedInstant.isBefore(maxInstant));
    }

    @Test
    public void testCastMethod() {
        InstantFieldProvider provider = new InstantFieldProvider(linearInstantField, options);
        Instant instant = Instant.now();
        String instantString = instant.toString();

        assertEquals(instant, provider.cast(instantString));
    }

    @Getter
    @Setter
    private static class TestClass {

        private Instant instantField;

        @JFaker(features = JFaker.Feature.LinearTimestamp)
        private Instant linearInstantField;
    }
}
