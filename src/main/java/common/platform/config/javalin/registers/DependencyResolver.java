package common.platform.config.javalin.registers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class DependencyResolver {

    private DependencyResolver() {}

    public static Object resolve(DependencyRegistry di, Constructor<?> ctor) throws InstantiationException, IllegalAccessException, InvocationTargetException, NullPointerException {
        Object[] parameters = resolveParameters(di, ctor);
        return ctor.newInstance(parameters);
    }

    private static Object[] resolveParameters(DependencyRegistry di, Constructor<?> ctor) {
        return Arrays.stream(ctor.getParameterTypes())
            .map(paramType -> {
                Object instance = di.get(paramType);
                if (instance == null) {
                    throw new NullPointerException(
                        "Null dependency in map: " + paramType.getName()
                    );
                }
                return instance;
            })
            .toArray();
    }

}
