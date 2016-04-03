package aspectj.trace.util;

import java.util.HashMap;
import java.util.List;

/**
 * Created by syc on 4/1/16.
 */
public class Pair<A, B> {
    public final A first;
    public final B second;

    public Pair(A first, B second) {
        this.first = first == null ? null : first;
        this.second = second == null ? null : second;
    }

    public B getSecond() {
        return second;
    }

    public A getFirst() {
        return first;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!first.equals(pair.first)) return false;
        return second.equals(pair.second);
    }

}
