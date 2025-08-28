package common.platform.config.javalin.registers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import common.kernel.exceptions.api.InternalServerException;
import common.kernel.exceptions.technical.CQRSException;
import common.kernel.exceptions.technical.CQRSModuleInstantiationException;
import common.kernel.ports.cqrs.Command;
import common.kernel.ports.cqrs.Query;
import common.platform.config.javalin.annotation.JavalinCommandHandler;
import common.platform.config.javalin.annotation.JavalinConstructorAutowired;
import common.platform.config.javalin.annotation.JavalinQueryHandler;
import common.platform.config.javalin.annotation.JavalinUseCaseModule;
import common.platform.config.javalin.enums.PackagePaths;
import common.platform.config.javalin.interfaces.Registry;

import org.reflections.Reflections;

public final class UseCasesRegistry implements Registry<Void> {

    private Map<Class<?>, Query<?, ?>> queries = new HashMap<>();
    private Map<Class<?>, Command<?, ?>> commands = new HashMap<>();

    protected UseCasesRegistry() {}

    @Override
    public Void registry(Object... args) {
        if (args.length != 1) {
            throw new InternalServerException(new IllegalArgumentException("Expected one argument: DependencyRegistry instance"));
        }
        if(!(args[0] instanceof DependencyRegistry di)) {
            throw new InternalServerException(new IllegalArgumentException("The first arg must be instace of " + DependencyRegistry.class.getName() + "class"));
        }
        initializeModulesRegistry(di, new Reflections(PackagePaths.JAVALIN_MODULES_PATH.toString()));
        commands = Map.copyOf(commands);
        queries = Map.copyOf(queries);
        return null;
    }

    public void initializeModulesRegistry(DependencyRegistry di, Reflections modulesReflections) {
        Set<Class<?>> moduleClasses = modulesReflections.getTypesAnnotatedWith(JavalinUseCaseModule.class);
        moduleClasses.forEach(moduleClass -> {
            try {
                registerUseCases(instantiateModule(di, moduleClass));
            } catch (CQRSModuleInstantiationException e) {
                throw new InternalServerException(e);
            }
        });
    }

    private Object instantiateModule(DependencyRegistry di, Class<?> moduleClass) throws CQRSModuleInstantiationException {
        Constructor <?> ctor = Stream.of(moduleClass.getConstructors())
                                                        .filter(c -> c.isAnnotationPresent(JavalinConstructorAutowired.class))
                                                            .findFirst().orElseThrow(() -> new IllegalStateException(
            "No @JavalinConstructorAutowired constructor found in module: " + moduleClass.getName()
        ));
        try {
            return DependencyResolver.resolve(di, ctor);
        } catch (InstantiationException | IllegalAccessException | IllegalStateException | NullPointerException
                    | InvocationTargetException e) {
               throw new CQRSModuleInstantiationException("Failed to instantiate module '" + moduleClass.getName() +  "' using constructor '" + ctor.toGenericString() + "'", e);
        }
    }

    private void registerUseCases(Object moduleInstance) throws CQRSException {
        for (Method method : moduleInstance.getClass().getMethods()) {
            try {
                if (method.isAnnotationPresent(JavalinCommandHandler.class) ||
                    method.isAnnotationPresent(JavalinQueryHandler.class)) {
                    Object useCase = method.invoke(moduleInstance);
                    Class<?> returnType = method.getReturnType();
                    if (Command.class.isAssignableFrom(returnType)) {
                        commands.put(returnType, (Command<?, ?>) useCase);
                    } else if (Query.class.isAssignableFrom(returnType)) {
                        queries.put(returnType, (Query<?, ?>) useCase);
                    } else {
                        throw new CQRSException("The annotated method "
                        + method.getName() + " returns an object that not implements " 
                        + Command.class.getName() + " or " + Query.class.getName() + " interface");
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CQRSException(method.getName(), e);
            }
        }
    }

    public <I, O> Command<I, O> getCommand(Class<? extends Command<I, O>> clazz) {
        return clazz.cast(commands.get(clazz));
    }

    public <I, O> Query<I, O> getQuery(Class<? extends Query<I, O>> clazz) {
        return clazz.cast(queries.get(clazz));
    }


    public Map<Class<?>, Command<?, ?>> getAllCommands() {
        return Map.copyOf(commands);
    }

    public Map<Class<?>, Query<?, ?>> getAllQueries() {
        return Map.copyOf(queries);
    }

}
