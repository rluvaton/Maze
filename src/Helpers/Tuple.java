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
     *
     * @return cloned tuple
     * @see #clone(Tuple) For static method
     */
    @Override
    public Tuple<X, Y> clone() {
        return Tuple.clone(this);
    }

    /**
     * Clone Tuple
     *
     * @param source Source tuple to clone
     * @param <X>    First type of the tuple
     * @param <Y>    Second type of the tuple
     * @return cloned tuple
     * @see #clone() For instance method
     */
    public static <X, Y> Tuple<X, Y> clone(Tuple<X, Y> source) {
        return source == null ? null : new Tuple<>(source.item1, source.item2);
    }

    /**
     * Compare Tuples
     *
     * @param tuple Tuple to compare with
     * @return if the tuples are equals
     * @see #compare(Tuple, Tuple) For static method
     */
    public boolean compare(Tuple<X, Y> tuple) {
        return tuple != null && tuple.item1 == this.item1 && tuple.item2 == this.item2;
    }

    /**
     * Compare 2 tuples
     *
     * @param tuple1 Tuple 1 to compare
     * @param tuple2 Tuple 2 to compare
     * @param <X>    First type of the tuples
     * @param <Y>    Second type of the tuples
     * @return if the tuples are equals
     * @see #compare(Tuple) For instance method
     */
    public static <X, Y> boolean compare(Tuple<X, Y> tuple1, Tuple<X, Y> tuple2) {
        return tuple1 == tuple2 || (tuple1 != null && tuple2 != null && tuple1.item1 == tuple2.item1 && tuple1.item2 == tuple2.item2);
    }
}