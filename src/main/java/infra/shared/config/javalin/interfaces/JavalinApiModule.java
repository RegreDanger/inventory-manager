package infra.shared.config.javalin.interfaces;

import io.javalin.Javalin;

public interface JavalinApiModule {
    public void register(Javalin app, String addQueryBus, String addCommandBus);
}
