package io.github.regychang.javafaker;

import java.util.HashMap;
import java.util.Map;

public class Options {

    private final Map<String, Void> ignoreFields = new HashMap<>();

    private final Map<String, TaggedFunction> fieldProviders = new HashMap<>();

    private int maxDepth = 1;

    private boolean ignoreInterface = false;

    private int randomStringLength = 25;

    private int randomMaxArraySize = 100;

    private int randomMinArraySize = 0;

    private boolean generateUniqueValues = false;

    private int maxGenerateStringRetries = 1000000;

    private boolean setArrayMapNullIfLenZero = false;

    private boolean setArrayMapRandomToZero = false;

    private final RandomBoundary<Integer> randomIntegerBoundary = new RandomBoundary<>(0, 100);

    private final RandomBoundary<Float> randomFloatBoundary = new RandomBoundary<>(0f, 100f);

    private final RandomBoundary<Double> randomDoubleBoundary = new RandomBoundary<>(0.0, 100.0);

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

    public Options withFieldProvider(String fieldName, TaggedFunction provider) {
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

    public Options withRandomIntegerBoundaries(int start, int end) {
        this.randomIntegerBoundary.setStart(start);
        this.randomIntegerBoundary.setEnd(end);
        return this;
    }

    public Options withRandomFloatBoundaries(float start, float end) {
        this.randomFloatBoundary.setStart(start);
        this.randomFloatBoundary.setEnd(end);
        return this;
    }

    public Options withRandomDoubleBoundaries(double start, double end) {
        this.randomDoubleBoundary.setStart(start);
        this.randomDoubleBoundary.setEnd(end);
        return this;
    }

    public Map<String, Void> getIgnoreFields() {
        return ignoreFields;
    }

    public Map<String, TaggedFunction> getFieldProviders() {
        return fieldProviders;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public boolean isIgnoreInterface() {
        return ignoreInterface;
    }

    public int getRandomStringLength() {
        return randomStringLength;
    }

    public int getRandomMaxArraySize() {
        return randomMaxArraySize;
    }

    public int getRandomMinArraySize() {
        return randomMinArraySize;
    }

    public boolean isGenerateUniqueValues() {
        return generateUniqueValues;
    }

    public int getMaxGenerateStringRetries() {
        return maxGenerateStringRetries;
    }

    public boolean isSetArrayMapNullIfLenZero() {
        return setArrayMapNullIfLenZero;
    }

    public boolean isSetArrayMapRandomToZero() {
        return setArrayMapRandomToZero;
    }

    public RandomBoundary<Integer> getRandomIntegerBoundary() {
        return randomIntegerBoundary;
    }

    public RandomBoundary<Float> getRandomFloatBoundary() {
        return randomFloatBoundary;
    }

    public RandomBoundary<Double> getRandomDoubleBoundary() {
        return randomDoubleBoundary;
    }

    public Options withRandomStringCharacterSet(String characterSet) {
        this.randomStringCharacterSet = characterSet;
        return this;
    }

    public String getRandomStringCharacterSet() {
        return randomStringCharacterSet;
    }

    public Options withRandomEnum(Class<? extends Enum<?>> randomEnumClass) {
        this.randomEnumClass = randomEnumClass;
        return this;
    }

    public Class<? extends Enum<?>> getRandomEnumClass() {
        return randomEnumClass;
    }

    public Options withNullProbability(double nullProbability) {
        this.nullProbability = nullProbability;
        return this;
    }

    public double getNullProbability() {
        return nullProbability;
    }
}
