package Helpers;

public class CallbackFns {
    @FunctionalInterface
    public interface ArgsCallbackFunction<T> {
        T run(T t);
    }

    @FunctionalInterface
    public interface ArgsVoidCallbackFunction<T> {
        void run(T t);
    }

    @FunctionalInterface
    public interface NoArgsCallbackFunction<T> {
        T run();
    }

    @FunctionalInterface
    public interface NoArgsVoidCallbackFunction {
        void run();
    }
}
