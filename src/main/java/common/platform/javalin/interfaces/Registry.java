package common.platform.javalin.interfaces;

public interface Registry<T> {
    public T registry(Object... args);
}
