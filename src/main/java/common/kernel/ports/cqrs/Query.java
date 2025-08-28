package common.kernel.ports.cqrs;

public interface Query <T, R> extends Handler<T, R> {
    @Override
    R handle(T input);
    
}
