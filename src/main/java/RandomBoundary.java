/**
 * @author regy
 */
public class RandomBoundary<T extends Number> {
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
