package infra.shared.config.javalin;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import kernel.impl.exceptions.InternalServerException;
import kernel.utils.enums.ErrorCode;

import infra.shared.config.javalin.interfaces.JavalinApiModule;
import infra.shared.config.javalin.annotation.JavalinApiModuleAnnotation;
import io.javalin.Javalin;

public class EndpointRegistry {
    
    private EndpointRegistry() {}

    //Query and Command buses will be replaced with the actual implementations
    public static void initializeEndpoints(Javalin app, String addQueryBus, String addCommandBus) {
        if (app == null) {
            Throwable cause = new NullPointerException("Javalin app is null");
            throw new InternalServerException("Javalin app is null.",
                    ErrorCode.INTERNAL_SERVER_ERROR, cause);
        }
        if (addQueryBus == null || addCommandBus == null) {
            Throwable cause = new NullPointerException("Query or Command bus is null");
            throw new InternalServerException("Query or Command bus is null.",
                    ErrorCode.INTERNAL_SERVER_ERROR, cause);
        }
        
        Reflections reflections = new Reflections("infra.shared.config.javalin");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(JavalinApiModuleAnnotation.class);
        for (Class<?> clazz : classes) {
            if (JavalinApiModule.class.isAssignableFrom(clazz)) {
                try {
                    JavalinApiModule module = (JavalinApiModule) clazz.getDeclaredConstructor().newInstance();
                    module.register(app, addQueryBus, addCommandBus);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new InternalServerException("Failed to initialize Javalin API module.",
                            ErrorCode.INTERNAL_SERVER_ERROR, e);
                }
            }
        }
    }
}
