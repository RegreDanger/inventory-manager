package common.platform.javalin.registers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.kernel.exceptions.api.InternalServerException;
import common.kernel.ports.repository.CrudRepository;
import common.platform.javalin.annotation.JavalinRepositoryAnnotation;
import common.platform.javalin.enums.PackagePaths;
import common.platform.javalin.interfaces.Registry;
import common.platform.db.sqlite.HikariSqlite;

public final class RepositoryRegistry implements Registry<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryRegistry.class);
    private Map<Class<?>, Object> repositories;

    protected RepositoryRegistry() {}

    @Override
    public Void registry(Object... args) {
        LOGGER.info("Initializing registry...");
        if (args.length != 1) {
            throw new InternalServerException(new IllegalArgumentException("Expected one argument: HikariSqlite instance"));
        }
        if(!(args[0] instanceof HikariSqlite sqlite)) {
            throw new InternalServerException(new IllegalArgumentException("The first arg must be instace of " + HikariSqlite.class.getName() + "class"));
        }
        Reflections reflections = new Reflections(PackagePaths.SQLITE_REPOSITORY_PATH.toString());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(JavalinRepositoryAnnotation.class);
        Map<Class<?>, Object> localRepositories = new HashMap<>();
        classes.stream()
            .filter(this::isValidRepositoryClass)
                .forEach(clazz -> {
                    try {
                        localRepositories.put(clazz, instantiateRepository(clazz, sqlite));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new InternalServerException("Failed to initialize repository: " + clazz.getName(), e);
                    }
                });
        repositories = Map.copyOf(localRepositories);
        LOGGER.info("Repositories registry success!");
        return null;
    }

    private boolean isValidRepositoryClass(Class<?> clazz) {
        LOGGER.debug("Validating constructor for: {}", clazz.getSimpleName());
        boolean result = CrudRepository.class.isAssignableFrom(clazz) &&
            !Modifier.isAbstract(clazz.getModifiers()) &&
            !clazz.isInterface();
         if(result) {
            LOGGER.info("Registered repository: {}", clazz.getSimpleName());
         } else {
            LOGGER.warn("Repository not registered: {}", clazz.getSimpleName());
         }
        return result;
    }

    private CrudRepository<?, ?> instantiateRepository(Class<?> clazz, HikariSqlite sqlite) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException  {
        return (CrudRepository<?, ?>) clazz.getConstructor(HikariSqlite.class).newInstance(sqlite);
    }

    public DependencyRegistry injectRepositoriesInto(DependencyRegistry diRegistry) {
        if (repositories == null) {
            throw new InternalServerException(new NullPointerException("Repositories have not been scanned. Call scanAndRegistryRepositories() first."));
        }
        return diRegistry.addInstances(repositories);
    }



}
