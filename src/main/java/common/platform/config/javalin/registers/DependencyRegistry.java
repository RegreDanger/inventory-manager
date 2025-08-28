package common.platform.config.javalin.registers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import common.kernel.exceptions.api.InternalServerException;
import common.platform.config.javalin.interfaces.Registry;

public final class DependencyRegistry implements Registry<DependencyRegistry> {
    private Map<Class<?>, Object> instances = Map.of();

    protected DependencyRegistry() {}

    private DependencyRegistry(Map<Class<?>, Object> init) {
        if (init != null && !init.isEmpty()) {
            this.instances = Map.copyOf(init);
        }
    }

    public DependencyRegistry addInstances(Map<Class<?>, Object> map) {
        return new DependencyRegistry(combine(this.instances, map));
    }

    public Map<Class<?>, Object> getInstances() {
        return instances;
    }

    public <T> T get(Class<T> clazz) {
        for (Map.Entry<Class<?>, Object> entry : instances.entrySet()) {
            if (clazz.isAssignableFrom(entry.getKey())) {
                return clazz.cast(entry.getValue());
            }
        }
        throw new InternalServerException(new NullPointerException("Dependency null: " + clazz));
    }

    private Map<Class<?>, Object> combine(Map<Class<?>, Object> a, Map<Class<?>, Object> b) {
        Map<Class<?>, Object> combined = new HashMap<>(a);
        combined.putAll(b);
        return Map.copyOf(combined);
    }

    @Override
    public DependencyRegistry registry(Object... args) {
        return new DependencyRegistry(combine(this.instances,
            Arrays.stream(args).collect(Collectors.toMap(Object::getClass, obj -> obj))));
    }

}
