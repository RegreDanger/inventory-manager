package common.platform.config.javalin.interfaces;

import common.platform.config.javalin.adapters.cqrs.JavalinCommandBus;
import common.platform.config.javalin.adapters.cqrs.JavalinQueryBus;
import io.javalin.Javalin;

public interface JavalinApiModule {
    public void register(Javalin app, JavalinQueryBus queryBus, JavalinCommandBus commandBus);
}
