package io.github.regychang.javafaker;

import io.github.regychang.javafaker.annotation.JFaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class FakerTest {

    @Test
    @DisplayName("Test empty list and map generation")
    void testEmptyListAndMap() throws Exception {
        Options ops =
                new Options()
                        .withRandomMinArraySize(0)
                        .withRandomMaxArraySize(0);

        Assertions.assertTrue(Faker.createList(String.class, ops).isEmpty(), "Generated list should be empty");
        Assertions.assertTrue(Faker.createMap(String.class, String.class, ops).isEmpty(), "Generated map should be empty");
    }

    @Test
    @DisplayName("Test random long generation within specified boundaries")
    void testRandomLong() {
        Options ops = new Options().withRandomIntegerBoundaries(10, 20);
        for (int i = 0; i < 100; i++) {
            long randomLong = Faker.randomLong(ops);
            Assertions.assertTrue(randomLong >= 10 && randomLong <= 20, "Generated long should be within specified boundaries");
        }
    }

    @Test
    @DisplayName("Test random double generation within specified boundaries")
    void testRandomDouble() {
        Options ops = new Options().withRandomDoubleBoundaries(10.5, 20.5);
        for (int i = 0; i < 100; i++) {
            double randomDouble = Faker.randomDouble(ops);
            Assertions.assertTrue(randomDouble >= 10.5 && randomDouble <= 20.5, "Generated double should be within specified boundaries");
        }
    }

    @Test
    @DisplayName("Test random boolean generation")
    void testRandomBoolean() {
        int trueCount = 0;
        int falseCount = 0;
        for (int i = 0; i < 1000; i++) {
            boolean randomBoolean = Faker.randomBoolean();
            if (randomBoolean) {
                trueCount++;
            } else {
                falseCount++;
            }
        }
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
        Assertions.assertTrue(randomString.chars().allMatch(c -> c == 'a' || c == 'b' || c == 'c'),
                "Generated string should only contain characters from the specified character set");
    }

    @Test
    @DisplayName("Test random enum generation")
    void testRandomEnum() {
        Options ops = new Options().withRandomEnum(SimpleEnum.class);
        SimpleEnum randomEnum = Faker.randomEnum(ops);
        Assertions.assertNotNull(randomEnum, "Generated enum should not be null");
        Assertions.assertTrue(Arrays.asList(SimpleEnum.values()).contains(randomEnum), "Generated enum should be a valid value from the TestEnum");
    }

    @Test
    @DisplayName("Test null value probability")
    void testNullValueProbability() {
        Options ops = new Options().withNullProbability(0.5);
        int nullCount = 0;
        int notNullCount = 0;
        for (int i = 0; i < 1000; i++) {
            String randomString = Faker.randomNullableString(ops);
            if (randomString == null) {
                nullCount++;
            } else {
                notNullCount++;
            }
        }
        Assertions.assertTrue(nullCount > 0, "There should be at least one null value generated");
        Assertions.assertTrue(notNullCount > 0, "There should be at least one not-null value generated");
    }

    @Test
    @DisplayName("Test simple class data generation")
    void testSimpleClass() throws Exception {
        SimpleClass simpleClass = new SimpleClass();
        Faker.fakeData(simpleClass);

        Assertions.assertNotNull(simpleClass.a);
        Assertions.assertNotNull(simpleClass.b);
        Assertions.assertNotNull(simpleClass.c);
        Assertions.assertNotNull(simpleClass.d);
        Assertions.assertNotNull(simpleClass.e);
        Assertions.assertNotNull(simpleClass.f);
    }

    @Test
    @DisplayName("Test nested class data generation")
    void testNestedClass() throws Exception {
        NestedClass nestedClass = new NestedClass();
        Faker.fakeData(nestedClass);

        Assertions.assertNotNull(nestedClass.aa);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Assertions.assertNotNull(nestedClass.simpleClass);
        Assertions.assertNotNull(nestedClass.simpleClass);
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

        for (SimpleClass item : listClass.simpleClassList) {
            Assertions.assertNotNull(item);
            Assertions.assertNotNull(item.a);
            Assertions.assertNotNull(item.b);
            Assertions.assertNotNull(item.c);
            Assertions.assertNotNull(item.d);
            Assertions.assertNotNull(item.e);
            Assertions.assertNotNull(item.f);
        }

        for (NestedClass item : listClass.nestedClassList) {
            Assertions.assertNotNull(item);
            Assertions.assertNotNull(item.aa);
            Assertions.assertNotNull(item.simpleClass);
            Assertions.assertNotNull(item.simpleClass.a);
            Assertions.assertNotNull(item.simpleClass.b);
            Assertions.assertNotNull(item.simpleClass.c);
            Assertions.assertNotNull(item.simpleClass.d);
            Assertions.assertNotNull(item.simpleClass.e);
            Assertions.assertNotNull(item.simpleClass.f);
        }
    }


    @Test
    @DisplayName("Test map class data generation")
    void testMapClass() throws Exception {
        MapClass mapClass = new MapClass();
        Faker.fakeData(mapClass);

        Assertions.assertNotNull(mapClass.simpleClassMap);
        Assertions.assertNotNull(mapClass.nestedClassMap);

        for (Map.Entry<String, SimpleClass> entry : mapClass.simpleClassMap.entrySet()) {
            Assertions.assertNotNull(entry.getKey());
            Assertions.assertNotNull(entry.getValue());
        }

        for (Map.Entry<String, NestedClass> entry : mapClass.nestedClassMap.entrySet()) {
            Assertions.assertNotNull(entry.getKey());
            Assertions.assertNotNull(entry.getValue());
        }
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

        for (Map.Entry<String, List<SimpleClass>> entry : mapListClass.simpleClassListMap.entrySet()) {
            Assertions.assertNotNull(entry.getKey());

            for (SimpleClass item : entry.getValue()) {
                Assertions.assertNotNull(item);
                Assertions.assertNotNull(item.a);
                Assertions.assertNotNull(item.b);
                Assertions.assertNotNull(item.c);
                Assertions.assertNotNull(item.d);
                Assertions.assertNotNull(item.e);
                Assertions.assertNotNull(item.f);
            }
        }

        for (Map.Entry<String, List<NestedClass>> entry : mapListClass.nestedClassListMap.entrySet()) {
            Assertions.assertNotNull(entry.getKey());

            for (NestedClass item : entry.getValue()) {
                Assertions.assertNotNull(item);
                Assertions.assertNotNull(item.aa);
                Assertions.assertNotNull(item.simpleClass);
                Assertions.assertNotNull(item.simpleClass.a);
                Assertions.assertNotNull(item.simpleClass.b);
                Assertions.assertNotNull(item.simpleClass.c);
                Assertions.assertNotNull(item.simpleClass.d);
                Assertions.assertNotNull(item.simpleClass.e);
                Assertions.assertNotNull(item.simpleClass.f);
            }
        }
    }

    @Test
    void testWithFieldProvider() throws Exception {
        Options options = new Options()
                .withFieldProvider("a", field -> 42);

        SimpleClass simpleClass = new SimpleClass();
        Faker.fakeData(simpleClass, options);

        Assertions.assertNotNull(simpleClass.a);
        Assertions.assertNotNull(simpleClass.b);
        Assertions.assertNotNull(simpleClass.c);
        Assertions.assertNotNull(simpleClass.d);
        Assertions.assertNotNull(simpleClass.e);
        Assertions.assertNotNull(simpleClass.f);
        Assertions.assertEquals(42, simpleClass.a);
    }

    //    @Test
//    void testGenerateUniqueValues() {
//        // Assuming a new method 'generateUniqueStrings' is implemented in the io.github.regychang.javafaker.Faker class
//        io.github.regychang.javafaker.Options options = new io.github.regychang.javafaker.Options().withGenerateUniqueValues(true);
//        Set<String> uniqueStrings = io.github.regychang.javafaker.Faker.generateUniqueStrings(100);
//        assertEquals(100, uniqueStrings.size(), "Generated set should contain 100 unique strings");
//    }

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
        @JFaker("f")
        public String f;
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