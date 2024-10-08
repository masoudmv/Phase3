package shared.model;

import java.io.Serializable;
import java.util.Objects;

public class Pair<T, U> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T first;
    private U second;

    public Pair() {
        // Default constructor needed by Jackson
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }




    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
