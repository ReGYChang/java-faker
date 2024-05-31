package io.github.regychang.java.faker;

import java.io.Serializable;

public class RandomBoundary<T extends Number> implements Serializable {

    private T start;

    private T end;

    public RandomBoundary(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return end;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public void setEnd(T end) {
        this.end = end;
    }
}
