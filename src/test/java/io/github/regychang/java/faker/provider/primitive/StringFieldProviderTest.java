package io.github.regychang.java.faker.provider.primitive;

import io.github.regychang.java.faker.Options;
import io.github.regychang.java.faker.annotation.JFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StringFieldProviderTest {

    @Mock
    private Field mockField;

    @Mock
    private Options mockOptions;

    @Mock
    private JFaker mockAnnotation;

    private StringFieldProvider stringFieldProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockField.getAnnotation(JFaker.class)).thenReturn(mockAnnotation);
        when(mockAnnotation.length()).thenReturn(25);
        stringFieldProvider = new StringFieldProvider(mockField, mockOptions);
    }

    @Test
    public void testGenerateRandomString() {
        when(mockAnnotation.length()).thenReturn(10);
        when(mockOptions.getRandomStringCharacterSet()).thenReturn("abc123");

        stringFieldProvider = new StringFieldProvider(mockField, mockOptions);
        String randomString = stringFieldProvider.provide();

        assertEquals(
                10,
                randomString.length(),
                "The generated string should have a length of 10.");
        assertTrue(
                randomString.chars().allMatch(c -> "abc123".indexOf(c) >= 0),
                "The generated string should only contain characters from the set 'abc123'.");
    }

    @Test
    public void testGenerateWithRegexPattern() {
        when(mockAnnotation.format()).thenReturn("[a-z]{5}");

        stringFieldProvider = new StringFieldProvider(mockField, mockOptions);
        String regexString = stringFieldProvider.provide();

        assertTrue(
                regexString.matches("[a-z]{5}"),
                "The generated string should match the regex pattern '[a-z]{5}'.");
    }

    @Test
    public void testGenerateWithValueList() {
        when(mockAnnotation.values()).thenReturn(new String[]{"a", "b", "c"});

        stringFieldProvider = new StringFieldProvider(mockField, mockOptions);
        Set<String> distinctStrings = generateDistinctStrings(stringFieldProvider);

        assertEquals(
                stringFieldProvider.valueList.size(),
                distinctStrings.size(),
                "The size of distinct strings should be equal to the number of unique values provided.");
        assertTrue(
                stringFieldProvider.valueList.containsAll(distinctStrings),
                "All distinct strings should be in the value list.");
    }

    @Test
    public void testProvideWithCardinality() {
        when(mockAnnotation.cardinality()).thenReturn(5);

        stringFieldProvider = new StringFieldProvider(mockField, mockOptions);
        Set<String> distinctStrings = generateDistinctStrings(stringFieldProvider);

        assertEquals(
                stringFieldProvider.cardinality,
                distinctStrings.size(),
                "The size of distinct strings should be equal to the cardinality.");
    }

    @Test
    public void testGenerateWithValueListAndCardinality() {
        when(mockAnnotation.values()).thenReturn(new String[]{"a", "b", "c"});
        when(mockAnnotation.cardinality()).thenReturn(2);

        stringFieldProvider = new StringFieldProvider(mockField, mockOptions);
        Set<String> distinctStrings = generateDistinctStrings(stringFieldProvider);

        assertEquals(
                stringFieldProvider.valueList.size(),
                distinctStrings.size(),
                "The size of distinct strings should be equal to the number of unique values provided.");
        assertTrue(
                stringFieldProvider.valueList.containsAll(distinctStrings),
                "All distinct strings should be in the value list.");
    }

    private Set<String> generateDistinctStrings(StringFieldProvider stringFieldProvider) {
        return IntStream.range(0, 1000)
                .mapToObj(idx -> stringFieldProvider.provide())
                .reduce(
                        new HashSet<>(),
                        (set, str) -> {
                            set.add(str);
                            return set;
                        },
                        (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        });
    }
}
