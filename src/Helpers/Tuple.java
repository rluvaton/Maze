package Helpers;

/**
 * Custom Tuple Class
 * @param <X> The first type
 * @param <Y> the second type
 */
public class Tuple<X, Y>
{
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
}