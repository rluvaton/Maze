package Logger;

public interface Logger {
    
    // region Log
    void log(LogLevel level, byte b);
    void log(LogLevel level, byte... b);
    void log(LogLevel level, short s);
    void log(LogLevel level, short ...s);
    void log(LogLevel level, int i);
    void log(LogLevel level, int... i);
    void log(LogLevel level, long l);
    void log(LogLevel level, long... l);
    void log(LogLevel level, double d);
    void log(LogLevel level, double... d);
    void log(LogLevel level, float f);
    void log(LogLevel level, float... f);
    void log(LogLevel level, char c);
    void log(LogLevel level, char... c);
    void log(LogLevel level, char[] ... c);
    void log(LogLevel level, String s);
    void log(LogLevel level, String... s);
    <T>void log(LogLevel level, T t);
    <T>void log(LogLevel level, T... t);
    
    // endregion

    // region Error

    default void error(byte b) {
        log(LogLevel.ERROR, b);
    }
    default void error(byte... b) {
        log(LogLevel.ERROR, b);
    }
    default void error(short s) {
        log(LogLevel.ERROR, s);
    }
    default void error(short ...s) {
        log(LogLevel.ERROR, s);
    }
    default void error(int i) {
        log(LogLevel.ERROR, i);
    }
    default void error(int... i) {
        log(LogLevel.ERROR, i);
    }
    default void error(long l) {
        log(LogLevel.ERROR, l);
    }
    default void error(long... l) {
        log(LogLevel.ERROR, l);
    }
    default void error(double d) {
        log(LogLevel.ERROR, d);
    }
    default void error(double... d) {
        log(LogLevel.ERROR, d);
    }
    default void error(float f) {
        log(LogLevel.ERROR, f);
    }
    default void error(float... f) {
        log(LogLevel.ERROR, f);
    }
    default void error(char c) {
        log(LogLevel.ERROR, c);
    }
    default void error(char... c) {
        log(LogLevel.ERROR, c);
    }
    default void error(char[] ... c) {
        log(LogLevel.ERROR, c);
    }
    default void error(String s) {
        log(LogLevel.ERROR, s);
    }
    default void error(String... s) {
        log(LogLevel.ERROR, s);
    }
    default <T>void error(T t) {
        log(LogLevel.ERROR, t);
    }
    default <T>void error(T... t) {
        log(LogLevel.ERROR, t);
    }

    // endregion
    
    // region Warn

    default void warn(byte b) {
        log(LogLevel.WARN, b);
    }
    default void warn(byte... b) {
        log(LogLevel.WARN, b);
    }
    default void warn(short s) {
        log(LogLevel.WARN, s);
    }
    default void warn(short ...s) {
        log(LogLevel.WARN, s);
    }
    default void warn(int i) {
        log(LogLevel.WARN, i);
    }
    default void warn(int... i) {
        log(LogLevel.WARN, i);
    }
    default void warn(long l) {
        log(LogLevel.WARN, l);
    }
    default void warn(long... l) {
        log(LogLevel.WARN, l);
    }
    default void warn(double d) {
        log(LogLevel.WARN, d);
    }
    default void warn(double... d) {
        log(LogLevel.WARN, d);
    }
    default void warn(float f) {
        log(LogLevel.WARN, f);
    }
    default void warn(float... f) {
        log(LogLevel.WARN, f);
    }
    default void warn(char c) {
        log(LogLevel.WARN, c);
    }
    default void warn(char... c) {
        log(LogLevel.WARN, c);
    }
    default void warn(char[] ... c) {
        log(LogLevel.WARN, c);
    }
    default void warn(String s) {
        log(LogLevel.WARN, s);
    }
    default void warn(String... s) {
        log(LogLevel.WARN, s);
    }
    default <T>void warn(T t) {
        log(LogLevel.WARN, t);
    }
    default <T>void warn(T... t) {
        log(LogLevel.WARN, t);
    }

    // endregion

    // region Debug

    default void debug(byte b) {
        log(LogLevel.DEBUG, b);
    }
    default void debug(byte... b) {
        log(LogLevel.DEBUG, b);
    }
    default void debug(short s) {
        log(LogLevel.DEBUG, s);
    }
    default void debug(short ...s) {
        log(LogLevel.DEBUG, s);
    }
    default void debug(int i) {
        log(LogLevel.DEBUG, i);
    }
    default void debug(int... i) {
        log(LogLevel.DEBUG, i);
    }
    default void debug(long l) {
        log(LogLevel.DEBUG, l);
    }
    default void debug(long... l) {
        log(LogLevel.DEBUG, l);
    }
    default void debug(double d) {
        log(LogLevel.DEBUG, d);
    }
    default void debug(double... d) {
        log(LogLevel.DEBUG, d);
    }
    default void debug(float f) {
        log(LogLevel.DEBUG, f);
    }
    default void debug(float... f) {
        log(LogLevel.DEBUG, f);
    }
    default void debug(char c) {
        log(LogLevel.DEBUG, c);
    }
    default void debug(char... c) {
        log(LogLevel.DEBUG, c);
    }
    default void debug(char[] ... c) {
        log(LogLevel.DEBUG, c);
    }
    default void debug(String s) {
        log(LogLevel.DEBUG, s);
    }
    default void debug(String... s) {
        log(LogLevel.DEBUG, s);
    }
    default <T>void debug(T t) {
        log(LogLevel.DEBUG, t);
    }
    default <T>void debug(T... t) {
        log(LogLevel.DEBUG, t);
    }

    // endregion
    
    // region Info

    default void info(byte b) {
        log(LogLevel.INFO, b);
    }
    default void info(byte... b) {
        log(LogLevel.INFO, b);
    }
    default void info(short s) {
        log(LogLevel.INFO, s);
    }
    default void info(short ...s) {
        log(LogLevel.INFO, s);
    }
    default void info(int i) {
        log(LogLevel.INFO, i);
    }
    default void info(int... i) {
        log(LogLevel.INFO, i);
    }
    default void info(long l) {
        log(LogLevel.INFO, l);
    }
    default void info(long... l) {
        log(LogLevel.INFO, l);
    }
    default void info(double d) {
        log(LogLevel.INFO, d);
    }
    default void info(double... d) {
        log(LogLevel.INFO, d);
    }
    default void info(float f) {
        log(LogLevel.INFO, f);
    }
    default void info(float... f) {
        log(LogLevel.INFO, f);
    }
    default void info(char c) {
        log(LogLevel.INFO, c);
    }
    default void info(char... c) {
        log(LogLevel.INFO, c);
    }
    default void info(char[] ... c) {
        log(LogLevel.INFO, c);
    }
    default void info(String s) {
        log(LogLevel.INFO, s);
    }
    default void info(String... s) {
        log(LogLevel.INFO, s);
    }
    default <T>void info(T t) {
        log(LogLevel.INFO, t);
    }
    default <T>void info(T... t) {
        log(LogLevel.INFO, t);
    }

    // endregion
    
    // region Verbose

    default void verbose(byte b) {
        log(LogLevel.VERBOSE, b);
    }
    default void verbose(byte... b) {
        log(LogLevel.VERBOSE, b);
    }
    default void verbose(short s) {
        log(LogLevel.VERBOSE, s);
    }
    default void verbose(short ...s) {
        log(LogLevel.VERBOSE, s);
    }
    default void verbose(int i) {
        log(LogLevel.VERBOSE, i);
    }
    default void verbose(int... i) {
        log(LogLevel.VERBOSE, i);
    }
    default void verbose(long l) {
        log(LogLevel.VERBOSE, l);
    }
    default void verbose(long... l) {
        log(LogLevel.VERBOSE, l);
    }
    default void verbose(double d) {
        log(LogLevel.VERBOSE, d);
    }
    default void verbose(double... d) {
        log(LogLevel.VERBOSE, d);
    }
    default void verbose(float f) {
        log(LogLevel.VERBOSE, f);
    }
    default void verbose(float... f) {
        log(LogLevel.VERBOSE, f);
    }
    default void verbose(char c) {
        log(LogLevel.VERBOSE, c);
    }
    default void verbose(char... c) {
        log(LogLevel.VERBOSE, c);
    }
    default void verbose(char[] ... c) {
        log(LogLevel.VERBOSE, c);
    }
    default void verbose(String s) {
        log(LogLevel.VERBOSE, s);
    }
    default void verbose(String... s) {
        log(LogLevel.VERBOSE, s);
    }
    default <T>void verbose(T t) {
        log(LogLevel.VERBOSE, t);
    }
    default <T>void verbose(T... t) {
        log(LogLevel.VERBOSE, t);
    }

    // endregion

}

