package Helpers.ThrowableAssertions;

import java.util.Objects;

public class ObjectAssertion {

    private ObjectAssertion() {
    }

    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        } else {
            return object;
        }
    }

    public static boolean equals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static int compare(int v1, int v2) {
        return Integer.compare(v1, v2);
    }

    public static int compare(long v1, long v2) {
        return Long.compare(v1, v2);
    }


    public static int verifyPositive(int value, String paramName) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
        } else {
            return value;
        }
    }

    public static long verifyPositive(long value, String paramName) {
        if (value <= 0L) {
            throw new IllegalArgumentException(paramName + " > 0 required but it was " + value);
        } else {
            return value;
        }
    }
}
