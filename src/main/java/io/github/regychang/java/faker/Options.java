package io.github.regychang.java.faker;

import io.github.regychang.java.faker.provider.FieldProvider;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Options implements Serializable {

    private final Map<String, Void> ignoreFields = new HashMap<>();

    private final Map<String, FieldProvider<?>> fieldProviders = new HashMap<>();

    private int maxDepth = 1;

    private boolean ignoreInterface = false;

    private int randomStringLength = 25;

    private int randomMaxArraySize = 100;

    private int randomMinArraySize = 1;

    private boolean generateUniqueValues = false;

    private int maxGenerateStringRetries = 1000000;

    private boolean setArrayMapNullIfLenZero = false;

    private boolean setArrayMapRandomToZero = false;

    private final RandomBoundary<Integer> randomBoundary = new RandomBoundary<>(0, 100);

    private final RandomBoundary<Instant> randomInstantBoundary = new RandomBoundary<>(Instant.MIN, Instant.MAX);

    private String randomStringCharacterSet;

    private double nullProbability;

    private Class<? extends Enum<?>> randomEnumClass;

    public Options() {
    }

    public Options withIgnoreFields(String... fieldNames) {
        for (String fieldName : fieldNames) {
            ignoreFields.put(fieldName, null);
        }
        return this;
    }

    public Options withFieldProviders(String fieldName, FieldProvider<?> provider) {
        fieldProviders.put(fieldName, provider);
        return this;
    }

    public Options withMaxDepth(int depth) {
        this.maxDepth = depth;
        return this;
    }

    public Options withIgnoreInterface(boolean value) {
        this.ignoreInterface = value;
        return this;
    }

    public Options withRandomStringLength(int size) {
        this.randomStringLength = size;
        return this;
    }

    public Options withRandomMaxArraySize(int size) {
        this.randomMaxArraySize = size;
        return this;
    }

    public Options withRandomMinArraySize(int size) {
        this.randomMinArraySize = size;
        return this;
    }

    public Options withGenerateUniqueValues(boolean unique) {
        this.generateUniqueValues = unique;
        return this;
    }

    public Options withMaxGenerateStringRetries(int retries) {
        this.maxGenerateStringRetries = retries;
        return this;
    }

    public Options withSetArrayMapNullIfLenZero(boolean setNull) {
        this.setArrayMapNullIfLenZero = setNull;
        return this;
    }

    public Options withSetArrayMapRandomToZero(boolean setNumberToZero) {
        this.setArrayMapRandomToZero = setNumberToZero;
        return this;
    }

    public Options withRandomBoundaries(int start, int end) {
        this.randomBoundary.setStart(start);
        this.randomBoundary.setEnd(end);
        return this;
    }

    public Options withRandomInstantBoundaries(Instant start, Instant end) {
        this.randomInstantBoundary.setStart(start);
        this.randomInstantBoundary.setEnd(end);
        return this;
    }

    public Options withRandomStringCharacterSet(String characterSet) {
        this.randomStringCharacterSet = characterSet;
        return this;
    }

    public Options withRandomEnum(Class<? extends Enum<?>> randomEnumClass) {
        this.randomEnumClass = randomEnumClass;
        return this;
    }

    public Options withNullProbability(double nullProbability) {
        this.nullProbability = nullProbability;
        return this;
    }
}
