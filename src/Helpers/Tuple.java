package Helpers;

/**
 * Custom Tuple Class
 *
 * @param <X> The first type
 * @param <Y> the second type
 */
public class Tuple<X, Y> {
    /**
     * First value
     */
    public X item1;

    /**
     * Second value
     */
    public Y item2;

    public Tuple(X item1, Y item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    /**
     * Clone Tuple
     * @param source Source tuple to clone
     * @param <X> First type of the tuple
     * @param <Y> Second type of the tuple
     * @return cloned tuple
     */
    public static <X, Y> Tuple<X, Y> clone(Tuple<X, Y> source) {
        return source == null ? null : new Tuple<>(source.item1, source.item2);
    }

    /**
     * Clone Tuple
     * @return cloned tuple
     */
    @Override
    public Tuple<X, Y> clone() {
        return Tuple.clone(this);
    }
}