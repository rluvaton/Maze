package Helpers.Builder;

public interface IBuilder<T> {
    T build() throws BuilderException;
}
