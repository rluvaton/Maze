package Logger;

public class ConsoleLogger implements Logger {

    private static Logger instance = new ConsoleLogger();

    public static Logger getInstance() {
        return instance;
    }

    private ConsoleLogger() {
    }

    private void print(String s) {
        System.out.println(s);
    }

    private void print(LogLevel level, String s) {
        print((level != null ? "[" + level.toString() + "]" : "") + s);
    }

    private <T>void print(LogLevel level, T t) {
        print(level, String.valueOf(t));
    }

    private <T>void printMany(LogLevel level, T... tItems) {
        StringBuilder values = new StringBuilder();

        for (T t: tItems) {
            values.append(t).append(" ");
        }

        // Delete the last useless space
        values.deleteCharAt(values.length() - 1);

        print(level, values.toString());
    }

    private <T>void printMany(LogLevel level, T t) {
        print(level, String.valueOf(t));
    }

    @Override
    public void log(LogLevel level, byte b) {
        print(level, b);
    }

    @Override
    public void log(LogLevel level, byte... bItems) {
        printMany(level, bItems);
    }

    @Override
    public void log(LogLevel level, short s) {
        print(level, s);
    }

    @Override
    public void log(LogLevel level, short... sItems) {
        printMany(level, sItems);
    }

    @Override
    public void log(LogLevel level, int i) {
        this.print(level, i);

    }

    @Override
    public void log(LogLevel level, int... i) {
        printMany(level, i);
    }

    @Override
    public void log(LogLevel level, long l) {
        print(level, l);

    }

    @Override
    public void log(LogLevel level, long... l) {
        printMany(level, l);
    }

    @Override
    public void log(LogLevel level, double d) {
        print(level, d);

    }

    @Override
    public void log(LogLevel level, double... d) {
        printMany(level, d);
    }

    @Override
    public void log(LogLevel level, float f) {
        print(level, f);
    }

    @Override
    public void log(LogLevel level, float... f) {
        printMany(level, f);
    }

    @Override
    public void log(LogLevel level, char c) {
        print(level, c);
    }

    @Override
    public void log(LogLevel level, char... c) {
        printMany(level, c);
    }

    @Override
    public void log(LogLevel level, char[]... cArrs) {
        StringBuilder values = new StringBuilder();

        for (char[] arr: cArrs) {
            values.append(arr);
        }

        print(level, values.toString());
    }

    @Override
    public void log(LogLevel level, String s) {
        print(level, s);

    }

    @Override
    public void log(LogLevel level, String... s) {
        printMany(level, s);

    }

    @Override
    public <T> void log(LogLevel level, T t) {
        print(level, t);

    }

    @Override
    public <T> void log(LogLevel level, T... t) {
        printMany(level, t);
    }
}
