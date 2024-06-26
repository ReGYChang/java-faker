package io.github.regychang.java.faker.provider.primitive;

import static org.junit.jupiter.api.Assertions.*;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.RandomBoundary;
import io.github.regychang.java.faker.annotation.JFaker;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class IntegerFieldProviderTest {

    @Mock
    private Options options;

    private Field field;

    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        options = mock(Options.class);
        field = TestClass.class.getDeclaredField("integerField");
    }

    @Test
    public void testInitializationWithDefaultOptions() {
        when(options.getRandomBoundary()).thenReturn(new RandomBoundary<>(0, 100));
        IntegerFieldProvider provider = new IntegerFieldProvider(null, options);

        assertEquals(0, provider.min);
        assertEquals(100, provider.max);
    }

    @Test
    public void testProvideMethodWithinRange() {
        when(options.getRandomBoundary()).thenReturn(new RandomBoundary<>(0, 100));
        IntegerFieldProvider provider = new IntegerFieldProvider(null, options);

        for (int i = 0; i < 100; i++) {
            int value = provider.provide();
            assertTrue(value >= 0 && value < 100);
        }
    }

    @Test
    public void testProvideMethodWithMinEqualsMax() {
        when(options.getRandomBoundary()).thenReturn(new RandomBoundary<>(50, 50));
        IntegerFieldProvider provider = new IntegerFieldProvider(null, options);

        int value = provider.provide();
        assertEquals(50, value);
    }

    @Test
    public void testProvideMethodWithAnnotation() {
        when(options.getRandomBoundary()).thenReturn(new RandomBoundary<>(1, 100));
        IntegerFieldProvider provider = new IntegerFieldProvider(field, options);

        for (int i = 0; i < 100; i++) {
            int value = provider.provide();
            assertTrue(value >= 10 && value < 20);
        }
    }

    @Test
    public void testCastMethod() {
        when(options.getRandomBoundary()).thenReturn(new RandomBoundary<>(1, 100));
        IntegerFieldProvider provider = new IntegerFieldProvider(null, options);
        assertEquals(123, provider.cast("123"));
    }

    @Getter
    @Setter
    private static class TestClass {
        @JFaker(min = 10, max = 20)
        private Integer integerField;
    }
}
