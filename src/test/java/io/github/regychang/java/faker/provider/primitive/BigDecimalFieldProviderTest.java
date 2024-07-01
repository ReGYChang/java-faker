package io.github.regychang.java.faker.provider.primitive;

import static org.junit.jupiter.api.Assertions.*;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;
import io.github.regychang.java.faker.annotation.JFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.mockito.Mockito.when;

class BigDecimalFieldProviderTest {

    // TODO: This test assumes the default scale is 2
    private static final int DEFAULT_SCALE = 2;

    @Mock
    private Field mockField;

    @Mock
    private Options mockOptions;

    @Mock
    private JFaker mockAnnotation;

    private BigDecimalFieldProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockAnnotation.min()).thenReturn(0);
        when(mockAnnotation.max()).thenReturn(100);
        when(mockField.getAnnotation(JFaker.class)).thenReturn(mockAnnotation);
        when(mockOptions.getRandomBoundary()).thenReturn(new RandomBoundary<>(0, 100));
    }

    @Test
    void testProvideInternalWithinRange() {
        provider = new BigDecimalFieldProvider(mockField, mockOptions);

        for (int i = 0; i < 1000; i++) {
            BigDecimal result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(result.compareTo(BigDecimal.ZERO) >= 0);
            assertTrue(result.compareTo(BigDecimal.valueOf(100)) <= 0);
            assertEquals(2, result.scale());
        }
    }

    @Test
    void testProvideInternalWithEqualMinMax() {
        when(mockAnnotation.min()).thenReturn(50);
        when(mockAnnotation.max()).thenReturn(50);
        provider = new BigDecimalFieldProvider(mockField, mockOptions);

        BigDecimal result = provider.provideInternal();
        assertEquals(
                BigDecimal.valueOf(50).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP),
                result);
    }

    @Test
    void testProvideInternalWithMaxLessThanMin() {
        when(mockAnnotation.min()).thenReturn(100);
        when(mockAnnotation.max()).thenReturn(0);
        provider = new BigDecimalFieldProvider(mockField, mockOptions);

        BigDecimal result = provider.provideInternal();
        assertEquals(
                BigDecimal.valueOf(100).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP),
                result);
    }

    @Test
    void testCast() {
        provider = new BigDecimalFieldProvider(mockField, mockOptions);
        BigDecimal result = provider.cast("123.45");
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void testCastInvalidInput() {
        provider = new BigDecimalFieldProvider(mockField, mockOptions);
        assertThrows(NumberFormatException.class, () -> provider.cast("invalid"));
    }

    @Test
    void testDetermineScale() {
        provider = new BigDecimalFieldProvider(mockField, mockOptions);
        assertEquals(DEFAULT_SCALE, provider.provideInternal().scale());
    }
}
