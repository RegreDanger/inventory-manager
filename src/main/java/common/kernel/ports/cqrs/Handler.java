package common.kernel.ports.cqrs;

public interface Handler<I, O> {
    O handle(I input);
}
