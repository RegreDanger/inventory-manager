package common.platform.javalin.registers;

import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.kernel.exceptions.api.InternalServerException;
import common.kernel.exceptions.base.ApiException;
import common.kernel.utils.mappers.UserMessageMapper;
import common.platform.javalin.adapters.mappers.JavalinErrorCodeMapper;
import common.platform.javalin.enums.PackagePaths;
import common.platform.javalin.interfaces.Registry;
import io.javalin.Javalin;

public class ExceptionsRegistry implements Registry<Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsRegistry.class);
    private static final String ERROR_JSON = "{\"error\": \"";

    protected ExceptionsRegistry() {}

    @Override
    public Void registry(Object... args) {
        if(args.length != 2) {
            throw new InternalServerException(new IllegalArgumentException("Expected two arguments: Javalin and JavalinErrorCodeMapper instance"));
        }
        if(!(args[0] instanceof Javalin app) || !(args[1] instanceof JavalinErrorCodeMapper mapper)) {
            throw new InternalServerException(new IllegalArgumentException("The first argument must be a Javalin instance and the second argument must be a JavalinErrorCodeMapper instance"));
        }
        Set<Class<? extends ApiException>> classes = new Reflections(PackagePaths.EXCEPTIONS_PATH.toString()).getSubTypesOf(ApiException.class);
        classes.forEach(clazz -> addException(app, clazz, mapper));

        return null;
    }

    private void addException(Javalin app, Class<? extends ApiException> clazz, JavalinErrorCodeMapper mapper) {
        app.exception(clazz, (e, ctx) -> {
            ctx.status(mapper.convert(e.getErrorCode()));
            LOGGER.error("Handling exception {} for path {}: {}", clazz.getSimpleName(), ctx.path(), e.getMessage(), e);
            ctx.json(ERROR_JSON + UserMessageMapper.getMessage(e) + "\"}");
        });
    }

}
