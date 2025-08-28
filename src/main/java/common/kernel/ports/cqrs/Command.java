package common.kernel.ports.cqrs;

public interface Command<T, R> extends Handler<T, R> {
    @Override
    R handle(T input);
    
}
