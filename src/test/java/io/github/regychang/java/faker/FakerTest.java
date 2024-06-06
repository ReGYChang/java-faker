package io.github.regychang.java.faker;

import io.github.regychang.java.faker.annotation.JFaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class FakerTest {

    @Test
    @DisplayName("Test empty list and map generation")
    void testEmptyListAndMap() throws Exception {
        Options ops =
                new Options()
                        .withRandomMinArraySize(0)
                        .withRandomMaxArraySize(0);

        Assertions.assertTrue(
                Faker.createList(String.class, ops).isEmpty(),
                "Generated list should be empty");
        Assertions.assertTrue(
                Faker.createMap(String.class, String.class, ops).isEmpty(),
                "Generated map should be empty");
    }

    @Test
    @DisplayName("Test random long generation within specified boundaries")
    void testRandomLong() {
        Options ops = new Options().withRandomIntegerBoundaries(10, 20);
        IntStream.range(0, 100)
                .forEach(
                        idx -> {
                            long randomLong = Faker.randomLong(ops);
                            Assertions.assertTrue(
                                    randomLong >= 10 && randomLong <= 20,
                                    "Generated long should be within specified boundaries");
                        });
    }

    @Test
    @DisplayName("Test random double generation within specified boundaries")
    void testRandomDouble() {
        Options ops = new Options().withRandomDoubleBoundaries(10.5, 20.5);
        IntStream.range(0, 100)
                .forEach(
                        idx -> {
                            double randomDouble = Faker.randomDouble(ops);
                            Assertions.assertTrue(
                                    randomDouble >= 10.5 && randomDouble <= 20.5,
                                    "Generated double should be within specified boundaries");
                        });
    }

    @Test
    @DisplayName("Test random boolean generation")
    void testRandomBoolean() {
        long trueCount =
                IntStream.range(0, 1000)
                        .mapToObj(idx -> Faker.randomBoolean())
                        .filter(Boolean::booleanValue)
                        .count();
        long falseCount = 1000 - trueCount;

        Assertions.assertTrue(trueCount > 0, "There should be at least one true value generated");
        Assertions.assertTrue(falseCount > 0, "There should be at least one false value generated");
    }

    @Test
    @DisplayName("Test random string generation with custom length")
    void testRandomString() {
        Options ops = new Options().withRandomStringLength(10);
        String randomString = Faker.randomString(ops);
        Assertions.assertEquals(10, randomString.length(), "Generated string should have the specified length");
    }

    @Test
    @DisplayName("Test random string generation with custom character set")
    void testRandomStringWithCharacterSet() {
        Options ops = new Options().withRandomStringLength(20).withRandomStringCharacterSet("abc");
        String randomString = Faker.randomString(ops);
        Assertions.assertTrue(
                randomString.chars().allMatch(c -> c == 'a' || c == 'b' || c == 'c'),
                "Generated string should only contain characters from the specified character set");
    }

    @Test
    @DisplayName("Test random enum generation")
    void testRandomEnum() {
        Options ops = new Options().withRandomEnum(SimpleEnum.class);
        SimpleEnum randomEnum = Faker.randomEnum(ops);
        Assertions.assertNotNull(randomEnum, "Generated enum should not be null");
        Assertions.assertTrue(
                Arrays.asList(SimpleEnum.values()).contains(randomEnum),
                "Generated enum should be a valid value from the TestEnum");
    }

    @Test
    @DisplayName("Test random instant generation")
    void testRandomInstant() {
        Instant now = Instant.now();
        Options ops = new Options().withRandomInstantBoundaries(Instant.MIN, now);
        Instant randomInstant = Faker.randomInstant(ops);
        Assertions.assertNotNull(randomInstant, "Generated instant should not be null");
        Assertions.assertTrue(randomInstant.isBefore(now));
    }

    @Test
    @DisplayName("Test null value probability")
    void testNullValueProbability() {
        Options ops = new Options().withNullProbability(0.5);
        long nullCount =
                IntStream.range(0, 1000)
                        .mapToObj(idx -> Faker.randomNullableString(ops))
                        .filter(Objects::isNull)
                        .count();
        long notNullCount = 1000 - nullCount;

        Assertions.assertTrue(nullCount > 0, "There should be at least one null value generated");
        Assertions.assertTrue(notNullCount > 0, "There should be at least one not-null value generated");
    }

    @Test
    @DisplayName("Test simple class data generation")
    void testSimpleClass() throws Exception {
        SimpleClass simpleClass = new SimpleClass();
        Faker.fakeData(simpleClass);

        Arrays.asList(simpleClass.a, simpleClass.b, simpleClass.c, simpleClass.d, simpleClass.e, simpleClass.f, simpleClass.g)
                .forEach(Assertions::assertNotNull);
    }

    @Test
    @DisplayName("Test nested class data generation")
    void testNestedClass() throws Exception {
        NestedClass nestedClass = new NestedClass();
        Faker.fakeData(nestedClass);

        Assertions.assertNotNull(nestedClass.aa);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Arrays.asList(
                        nestedClass.simpleClass.a,
                        nestedClass.simpleClass.b,
                        nestedClass.simpleClass.c,
                        nestedClass.simpleClass.d,
                        nestedClass.simpleClass.e,
                        nestedClass.simpleClass.f,
                        nestedClass.simpleClass.g)
                .forEach(Assertions::assertNotNull);
    }

    @Test
    void testArrayClass() throws Exception {
        ArrayClass arrayClass = new ArrayClass();
        Faker.fakeData(arrayClass);

        Assertions.assertNotNull(arrayClass.intArray);
        Assertions.assertTrue(arrayClass.intArray.length > 0);
    }

    @Test
    @DisplayName("Test list class data generation")
    void testListClass() throws Exception {
        ListClass listClass = new ListClass();
        Faker.fakeData(listClass);

        Assertions.assertNotNull(listClass.simpleClassList);
        Assertions.assertNotNull(listClass.nestedClassList);
        Assertions.assertFalse(listClass.simpleClassList.isEmpty());
        Assertions.assertFalse(listClass.nestedClassList.isEmpty());

        listClass.simpleClassList.forEach(item -> {
            Assertions.assertNotNull(item);
            Arrays.asList(item.a, item.b, item.c, item.d, item.e, item.f, item.g).forEach(Assertions::assertNotNull);
        });

        listClass.nestedClassList.forEach(item -> {
            Assertions.assertNotNull(item);
            Assertions.assertNotNull(item.aa);
            Assertions.assertNotNull(item.simpleClass);
            Arrays.asList(
                            item.simpleClass.a,
                            item.simpleClass.b,
                            item.simpleClass.c,
                            item.simpleClass.d,
                            item.simpleClass.e,
                            item.simpleClass.f,
                            item.simpleClass.g)
                    .forEach(Assertions::assertNotNull);
        });
    }

    @Test
    @DisplayName("Test map class data generation")
    void testMapClass() throws Exception {
        MapClass mapClass = new MapClass();
        Faker.fakeData(mapClass);

        Assertions.assertNotNull(mapClass.simpleClassMap);
        Assertions.assertNotNull(mapClass.nestedClassMap);

        mapClass.simpleClassMap.forEach(
                (key, value) -> {
                    Assertions.assertNotNull(key);
                    Assertions.assertNotNull(value);
                });

        mapClass.nestedClassMap.forEach(
                (key, value) -> {
                    Assertions.assertNotNull(key);
                    Assertions.assertNotNull(value);
                });
    }

    @Test
    @DisplayName("Test generic in map data generation")
    void testGenericInMap() throws Exception {
        MapListClass mapListClass = new MapListClass();
        Faker.fakeData(mapListClass);

        Assertions.assertNotNull(mapListClass.simpleClassListMap);
        Assertions.assertNotNull(mapListClass.nestedClassListMap);
        Assertions.assertFalse(mapListClass.simpleClassListMap.isEmpty());
        Assertions.assertFalse(mapListClass.nestedClassListMap.isEmpty());

        mapListClass.simpleClassListMap.forEach(
                (key, value) -> {
                    Assertions.assertNotNull(key);
                    value.forEach(
                            item -> {
                                Assertions.assertNotNull(item);
                                Arrays.asList(item.a, item.b, item.c, item.d, item.e, item.f, item.g)
                                        .forEach(Assertions::assertNotNull);
                            });
                });

        mapListClass.nestedClassListMap.forEach(
                (key, value) -> {
                    Assertions.assertNotNull(key);
                    value.forEach(
                            item -> {
                                Assertions.assertNotNull(item);
                                Assertions.assertNotNull(item.aa);
                                Assertions.assertNotNull(item.simpleClass);
                                Arrays.asList(
                                                item.simpleClass.a,
                                                item.simpleClass.b,
                                                item.simpleClass.c,
                                                item.simpleClass.d,
                                                item.simpleClass.e,
                                                item.simpleClass.f,
                                                item.simpleClass.g)
                                        .forEach(Assertions::assertNotNull);
                            });
                });
    }

    @Test
    @DisplayName("Test field provider")
    void testWithFieldProvider() throws Exception {
        Options options = new Options().withFieldProvider("a", field -> 42);

        SimpleClass simpleClass = new SimpleClass();
        Faker.fakeData(simpleClass, options);

        Arrays.asList(
                        simpleClass.a,
                        simpleClass.b,
                        simpleClass.c,
                        simpleClass.d,
                        simpleClass.e,
                        simpleClass.f,
                        simpleClass.g)
                .forEach(Assertions::assertNotNull);
        Assertions.assertEquals(42, simpleClass.a);
    }

    @Test
    @DisplayName("Test formatted string provider")
    void testWithFormattedStringProvider() throws Exception {
        SimpleClass simpleClass = new SimpleClass();
        Faker.fakeData(simpleClass);

        Assertions.assertNotNull(simpleClass.f);

        Field field = SimpleClass.class.getDeclaredField("f");
        JFaker annotation = field.getAnnotation(JFaker.class);
        String regexFormat = annotation.format();

        Assertions.assertTrue(
                Pattern.matches(regexFormat, simpleClass.f),
                "Field 'f' should match the specified regex format");
    }

    @Test
    @DisplayName("Test options serializability")
    void testOptionsSerializability() {
        Options options = new Options().withFieldProvider("a", field -> 42);
        Assertions.assertTrue(isSerializable(options), "Options should be serializable");
    }

    private boolean isSerializable(Object obj) {
        if (!(obj instanceof Serializable)) {
            return false;
        }

        try {
            // Serialize the object
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
            objectOutStream.writeObject(obj);
            objectOutStream.close();

            // Deserialize the object
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream objectInStream = new ObjectInputStream(byteInStream);
            objectInStream.readObject();
            objectInStream.close();

            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public enum SimpleEnum {
        VALUE1, VALUE2, VALUE3
    }

    public static class SimpleClass {
        @JFaker("a")
        public Integer a;
        @JFaker("b")
        public Long b;
        @JFaker("c")
        public Double c;
        @JFaker("d")
        public Float d;
        @JFaker("e")
        public Boolean e;
        @JFaker(value = "f", format = "[0-9]{2}[A-Z]{2}[0-9]{2}[A-Z][0-9]-[A-Z]{2}[0-9][A-Z][0-9]{2}")
        public String f;
        @JFaker("g")
        public Instant g;
    }

    public static class NestedClass {
        public Integer aa;
        public SimpleClass simpleClass;
    }

    public static class ArrayClass {
        public int[] intArray;
    }

    public static class ListClass {
        public List<SimpleClass> simpleClassList;
        public List<NestedClass> nestedClassList;
    }

    public static class MapClass {
        public Map<String, SimpleClass> simpleClassMap;
        public Map<String, NestedClass> nestedClassMap;
    }

    public static class MapListClass {
        public Map<String, List<SimpleClass>> simpleClassListMap;
        public Map<String, List<NestedClass>> nestedClassListMap;
    }
}