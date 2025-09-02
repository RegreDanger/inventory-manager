package common.platform.javalin.adapters.cqrs;

import java.util.Map;

import common.kernel.exceptions.api.InternalServerException;
import common.kernel.ports.cqrs.Bus;
import common.kernel.ports.cqrs.Handler;
import common.platform.javalin.annotation.JavalinBusConstructor;
import common.platform.javalin.annotation.JavalinQueryBusClass;

@JavalinQueryBusClass
public class JavalinQueryBus implements Bus {
    public final Map<Class<? extends Handler<?, ?>>, Handler<?, ?>> handlers;

    @JavalinBusConstructor(busType = JavalinQueryBus.class)
    public JavalinQueryBus(Map<Class<? extends Handler<?, ?>>, Handler<?, ?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public <C, R, T extends Handler<C, R>> R send(Class<T> handlerClass, C input) {
        T handler = handlerClass.cast(handlers.get(handlerClass));
        if (handler == null) {
            throw new InternalServerException(new NullPointerException("Handler not found for class: " + handlerClass.getName()));
        }
        return handler.handle(input);
    }

    
}
