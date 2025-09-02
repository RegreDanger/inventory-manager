package common.platform.javalin.registers;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.kernel.exceptions.api.InternalServerException;
import common.platform.javalin.interfaces.Registry;

public final class JavalinContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavalinContext.class);

    private static final Map<Class<? extends Registry<?>>, Registry<?>> registries = new HashMap<>();
    
    private JavalinContext() {}

    public static <T extends Registry<?>> T getRegistry(Class<T> clazz) {
        LOGGER.info("Attempting to get {} registry", clazz.getName());
        return clazz.cast(registries.computeIfAbsent(clazz, c -> {
            try {
                LOGGER.debug("Registering new instance of {} registry", clazz.getName());
                return c.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new InternalServerException("Cannot instantiate/access registry class " + clazz.getName(), e);
            } catch (InvocationTargetException e) {
                throw new InternalServerException("Registry constructor invocation failed for " + clazz.getName(), e);
            }
        }));
    }
}
