package common.platform.javalin.interfaces;

import common.platform.javalin.adapters.cqrs.JavalinCommandBus;
import common.platform.javalin.adapters.cqrs.JavalinQueryBus;
import io.javalin.Javalin;

public interface JavalinApiModule {
    public void register(Javalin app, JavalinQueryBus queryBus, JavalinCommandBus commandBus);
}
