package common.platform.config.javalin.interfaces;

public interface Registry<T> {
    public T registry(Object... args);
}
