package common.platform.config.javalin.registers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.reflections.Reflections;

import common.kernel.exceptions.api.InternalServerException;
import common.platform.config.javalin.adapters.cqrs.JavalinCommandBus;
import common.platform.config.javalin.adapters.cqrs.JavalinQueryBus;
import common.platform.config.javalin.annotation.JavalinEndpoint;
import common.platform.config.javalin.interfaces.JavalinApiModule;
import common.platform.config.javalin.interfaces.Registry;
import io.javalin.Javalin;

public final class EndpointRegistry implements Registry<Void> {

    protected EndpointRegistry() {}

    @Override
    public Void registry(Object... args) {
        if (args.length != 3) {
            throw new InternalServerException(new IllegalArgumentException("Expected three arguments: Javalin, JavalinCommandBus, JavalinQueryBus"));
        }

        if (!(args[0] instanceof Javalin app) || !(args[1] instanceof JavalinCommandBus commandBus) || !(args[2] instanceof JavalinQueryBus queryBus)) {
            throw new InternalServerException(new IllegalArgumentException("Arguments must be: Javalin, JavalinCommandBus, JavalinQueryBus"));
        }

        Reflections reflections = new Reflections("infra.shared.config.javalin");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(JavalinEndpoint.class);
        for (Class<?> clazz : classes) {
            if (JavalinEndpoint.class.isAssignableFrom(clazz)) {
                try {
                    JavalinApiModule module = (JavalinApiModule) clazz.getDeclaredConstructor().newInstance();
                    module.register(app, queryBus, commandBus);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new InternalServerException("Failed to initialize Javalin API module.", e);
                }
            }
        }
        return null;
    }
}
