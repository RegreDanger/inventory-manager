package common.platform.config.javalin.registers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import common.kernel.exceptions.api.InternalServerException;
import common.kernel.ports.cqrs.Command;
import common.kernel.ports.cqrs.Handler;
import common.kernel.ports.cqrs.Query;
import common.platform.config.javalin.adapters.cqrs.JavalinCommandBus;
import common.platform.config.javalin.adapters.cqrs.JavalinQueryBus;
import common.platform.config.javalin.annotation.JavalinBusConstructor;
import common.platform.config.javalin.annotation.JavalinCommandBusClass;
import common.platform.config.javalin.annotation.JavalinQueryBusClass;
import common.platform.config.javalin.enums.PackagePaths;
import common.platform.config.javalin.interfaces.Registry;

public class BusRegistry implements Registry<Void> {
    private JavalinCommandBus commandBus;
    private JavalinQueryBus queryBus;

    protected BusRegistry() {}

    @Override
    public Void registry(Object... args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected two arguments: commands map and queries map");
        }

        if (!(args[0] instanceof Map<?, ?>) || !(args[1] instanceof Map<?, ?>)) {
            throw new InternalServerException(new IllegalArgumentException("The first and second arg must be instances of java.util.Map"));
        }

        Map<?, ?> rawCommands = (Map<?, ?>) args[0];
        Map<?, ?> rawQueries = (Map<?, ?>) args[1];

        Map<Class<?>, Command<?, ?>> commands = rawCommands.entrySet().stream()
            .collect(Collectors.toMap(
                e -> (Class<?>) e.getKey(),
                e -> (Command<?, ?>) e.getValue()
            ));

        Map<Class<?>, Query<?, ?>> queries = rawQueries.entrySet().stream()
            .collect(Collectors.toMap(
                e -> (Class<?>) e.getKey(),
                e -> (Query<?, ?>) e.getValue()
            ));

        registry(commands, queries);
        return null;
    }


    /**
     * Typed overload: preferred way to initialize the BusRegistry.
     */
    public void registry(Map<Class<?>, Command<?, ?>> commands, Map<Class<?>, Query<?, ?>> queries) {
        Reflections reflections = new Reflections(PackagePaths.JAVALIN_CQRS_PATH.toString());

        this.commandBus = initializeBus(JavalinCommandBus.class, commands, JavalinCommandBusClass.class, JavalinBusConstructor.class, reflections);
        this.queryBus = initializeBus(JavalinQueryBus.class, queries, JavalinQueryBusClass.class, JavalinBusConstructor.class, reflections);
    }

    @SuppressWarnings("unchecked")
    // Safe cast: all entries in 'usecases' are guaranteed by registration to implement Handler<?, ?>
    private <T> T initializeBus(Class<T> busClass, Map<Class<?>, ?> usecases, Class<? extends Annotation> annotationBusClass, Class<? extends Annotation> annotationBusConstructor, Reflections reflections) {

        Map<Class<? extends Handler<?, ?>>, Handler<?, ?>> safeMap = usecases.entrySet()
            .stream()
                .collect(Collectors.toMap(
                    e -> (Class<? extends Handler<?, ?>>) e.getKey(),
                    e -> (Handler<?, ?>) e.getValue()
                ));

        Set<Class<?>> busClasses = reflections.getTypesAnnotatedWith(annotationBusClass);

        if (busClasses.isEmpty()) {
            throw new InternalServerException(
                new IllegalStateException("No classes annotated with " + annotationBusClass.getSimpleName() + " found")
            );
        }

        Class<?> selectedBus = busClasses.stream()
            .filter(busClazz -> Arrays.stream(busClazz.getConstructors())
                .anyMatch(ctor -> ctor.isAnnotationPresent(annotationBusConstructor)))
                    .findFirst()
                        .orElseThrow(() -> new InternalServerException(new IllegalStateException(
                            "No classes with an annotated constructor found for " + annotationBusConstructor.getSimpleName())));

        Constructor<?> annotatedConstructor = findAnnotatedConstructor(selectedBus, annotationBusConstructor);
        return createBusInstance(busClass, safeMap, selectedBus, annotatedConstructor);
    }

    private Constructor<?> findAnnotatedConstructor(Class<?> busClazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(busClazz.getConstructors())
            .filter(c -> {
                if (c.isAnnotationPresent(annotation)) {
                    JavalinBusConstructor busAnnotation = c.getAnnotation(JavalinBusConstructor.class);
                    return busAnnotation.busType().equals(busClazz);
                }
                return false;
            })
            .reduce((a, b) -> {
                throw new InternalServerException(new IllegalStateException("Multiple constructors annotated with @" + annotation.getSimpleName() + " in " + busClazz.getName()));
            })
            .orElseThrow(() -> new InternalServerException(new IllegalStateException("No constructor annotated with @" + annotation.getSimpleName() + " in " + busClazz.getName())));
    }


    private <T> T createBusInstance(Class<T> busClass, Map<Class<? extends Handler<?, ?>>, Handler<?, ?>> usecases, Class<?> busClazz, Constructor<?> annotatedConstructor) {
        JavalinBusConstructor ann = annotatedConstructor.getAnnotation(JavalinBusConstructor.class);
        Class<?> typeFromAnnotation = ann.busType();

        if (typeFromAnnotation.equals(Void.class) || !busClass.isAssignableFrom(typeFromAnnotation)) {
            throw new InternalServerException(new IllegalStateException("Annotated bus type " + typeFromAnnotation.getName() + " is not compatible with requested bus class " + busClass.getName()));
        }

        try {
            Object instance = annotatedConstructor.newInstance(usecases);
            return busClass.cast(instance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InternalServerException("Cannot instantiate/access bus class " + busClazz.getName(), e);
        } catch (InvocationTargetException e) {
            throw new InternalServerException("Bus constructor invocation failed for " + busClazz.getName(), e);
        }
    }

    public JavalinCommandBus getCommandBus() {
        if (commandBus == null) {
            throw new InternalServerException(new NullPointerException("Command bus is not initialized"));
        }
        return commandBus;
    }

    public JavalinQueryBus getQueryBus() {
        if (queryBus == null) {
            throw new InternalServerException(new NullPointerException("Query bus is not initialized"));
        }
        return queryBus;
    }
}
