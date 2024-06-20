package io.github.regychang.java.faker;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class RandomBoundary<T extends Comparable<T>> implements Serializable {

    private T start;

    private T end;

    public RandomBoundary(T start, T end) {
        this.start = start;
        this.end = end;
    }
}
