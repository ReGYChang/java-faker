package io.github.regychang.java.faker.provider.primitive;

import static org.junit.jupiter.api.Assertions.*;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.mockito.Mockito.when;

class LocalDateFieldProviderTest {

    @Mock
    private Field mockField;

    @Mock
    private Options mockOptions;

    @Mock
    private JFaker mockAnnotation;

    private LocalDateFieldProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockAnnotation.min()).thenReturn(-365);
        when(mockAnnotation.max()).thenReturn(365);
        when(mockField.getAnnotation(JFaker.class)).thenReturn(mockAnnotation);
    }

    @Test
    void testProvideInternalWithinRange() {
        provider = new LocalDateFieldProvider(mockField, mockOptions);
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusDays(365);
        LocalDate maxDate = now.plusDays(365);

        for (int i = 0; i < 1000; i++) {
            LocalDate result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(
                    result.isAfter(minDate.minusDays(1)) &&
                            result.isBefore(maxDate.plusDays(1)));
        }
    }

    @Test
    void testProvideInternalWithCustomRange() {
        when(mockAnnotation.min()).thenReturn(-30);
        when(mockAnnotation.max()).thenReturn(30);
        provider = new LocalDateFieldProvider(mockField, mockOptions);

        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusDays(30);
        LocalDate maxDate = now.plusDays(30);

        for (int i = 0; i < 1000; i++) {
            LocalDate result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(
                    result.isAfter(minDate.minusDays(1)) &&
                            result.isBefore(maxDate.plusDays(1)));
        }
    }

    @Test
    void testProvideInternalWithEqualMinMax() {
        when(mockAnnotation.min()).thenReturn(0);
        when(mockAnnotation.max()).thenReturn(0);
        provider = new LocalDateFieldProvider(mockField, mockOptions);

        LocalDate result = provider.provideInternal();
        assertEquals(LocalDate.now(), result);
    }

    @Test
    void testCast() {
        provider = new LocalDateFieldProvider(mockField, mockOptions);
        LocalDate result = provider.cast("2022-03-15");
        assertEquals(LocalDate.of(2022, 3, 15), result);
    }

    @Test
    void testCastInvalidInput() {
        provider = new LocalDateFieldProvider(mockField, mockOptions);
        assertThrows(DateTimeParseException.class, () -> provider.cast("invalid"));
    }
}
