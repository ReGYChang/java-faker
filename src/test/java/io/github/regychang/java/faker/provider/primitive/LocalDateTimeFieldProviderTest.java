package io.github.regychang.java.faker.provider.primitive;

import static org.junit.jupiter.api.Assertions.*;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.mockito.Mockito.when;

class LocalDateTimeFieldProviderTest {

    @Mock
    private Field mockField;

    @Mock
    private Options mockOptions;

    @Mock
    private JFaker mockAnnotation;

    private LocalDateTimeFieldProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockAnnotation.min()).thenReturn(-365);
        when(mockAnnotation.max()).thenReturn(365);
        when(mockField.getAnnotation(JFaker.class)).thenReturn(mockAnnotation);
    }

    @Test
    void testProvideInternalWithinRange() {
        provider = new LocalDateTimeFieldProvider(mockField, mockOptions);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDateTime = now.minusDays(365);
        LocalDateTime maxDateTime = now.plusDays(365);

        for (int i = 0; i < 1000; i++) {
            LocalDateTime result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(
                    result.isAfter(minDateTime.minusDays(1)) &&
                            result.isBefore(maxDateTime.plusDays(1)));
        }
    }

    @Test
    void testProvideInternalWithCustomRange() {
        when(mockAnnotation.min()).thenReturn(-30);
        when(mockAnnotation.max()).thenReturn(30);
        provider = new LocalDateTimeFieldProvider(mockField, mockOptions);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDateTime = now.minusDays(30);
        LocalDateTime maxDateTime = now.plusDays(30);

        for (int i = 0; i < 1000; i++) {
            LocalDateTime result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(
                    result.isAfter(minDateTime.minusDays(1)) &&
                            result.isBefore(maxDateTime.plusDays(1)));
        }
    }

    @Test
    void testProvideInternalWithEqualMinMax() {
        when(mockAnnotation.min()).thenReturn(0);
        when(mockAnnotation.max()).thenReturn(0);
        provider = new LocalDateTimeFieldProvider(mockField, mockOptions);

        LocalDateTime result = provider.provideInternal();
        assertEquals(LocalDateTime.now().toLocalDate(), result.toLocalDate());
    }

    @Test
    void testCast() {
        provider = new LocalDateTimeFieldProvider(mockField, mockOptions);
        LocalDateTime result = provider.cast("2022-03-15T12:30:00");
        assertEquals(LocalDateTime.of(2022, 3, 15, 12, 30, 0), result);
    }

    @Test
    void testCastInvalidInput() {
        provider = new LocalDateTimeFieldProvider(mockField, mockOptions);
        assertThrows(DateTimeParseException.class, () -> provider.cast("invalid"));
    }
}
