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
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.mockito.Mockito.when;

class DateFieldProviderTest {

    private static final ZoneId UTC = ZoneId.of("UTC");

    @Mock
    private Field mockField;

    @Mock
    private Options mockOptions;

    @Mock
    private JFaker mockAnnotation;

    private DateFieldProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockAnnotation.min()).thenReturn(-365);
        when(mockAnnotation.max()).thenReturn(365);
        when(mockField.getAnnotation(JFaker.class)).thenReturn(mockAnnotation);
    }

    @Test
    void testProvideInternalWithinRange() {
        provider = new DateFieldProvider(mockField, mockOptions);
        LocalDate now = LocalDate.now(UTC);
        Date minDate = Date.from(now.minusDays(365).atStartOfDay(UTC).toInstant());
        Date maxDate = Date.from(now.plusDays(365).atStartOfDay(UTC).toInstant());

        for (int i = 0; i < 1000; i++) {
            Date result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(result.compareTo(minDate) >= 0 && result.compareTo(maxDate) <= 0);
        }
    }

    @Test
    void testProvideInternalWithCustomRange() {
        when(mockAnnotation.min()).thenReturn(-30);
        when(mockAnnotation.max()).thenReturn(30);
        provider = new DateFieldProvider(mockField, mockOptions);

        LocalDate now = LocalDate.now(UTC);
        Date minDate = Date.from(now.minusDays(30).atStartOfDay(UTC).toInstant());
        Date maxDate = Date.from(now.plusDays(30).atStartOfDay(UTC).toInstant());

        for (int i = 0; i < 1000; i++) {
            Date result = provider.provideInternal();
            assertNotNull(result);
            assertTrue(result.compareTo(minDate) >= 0 && result.compareTo(maxDate) <= 0);
        }
    }

    @Test
    void testProvideInternalWithEqualMinMax() {
        when(mockAnnotation.min()).thenReturn(0);
        when(mockAnnotation.max()).thenReturn(0);
        provider = new DateFieldProvider(mockField, mockOptions);

        Date result = provider.provideInternal();
        assertEquals(Date.from(LocalDate.now(UTC).atStartOfDay(UTC).toInstant()), result);
    }

    @Test
    void testCast() {
        provider = new DateFieldProvider(mockField, mockOptions);

        Date result = provider.cast("2022-03-15");
        LocalDate localDate = result.toInstant().atZone(UTC).toLocalDate();

        assertEquals("2022-03-15", localDate.toString());
    }

    @Test
    void testCastInvalidInput() {
        provider = new DateFieldProvider(mockField, mockOptions);
        assertThrows(DateTimeParseException.class, () -> provider.cast("invalid"));
    }
}
