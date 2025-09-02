package test.integration.javalin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import catalog.category.app.usecase.command.CreateCategoryUseCase;
import catalog.category.app.usecase.query.GetCategoryByIdUseCase;
import common.kernel.ports.cqrs.Command;
import common.kernel.ports.cqrs.Query;
import common.platform.javalin.adapters.cqrs.JavalinCommandBus;
import common.platform.javalin.adapters.cqrs.JavalinQueryBus;
import common.platform.javalin.adapters.mappers.JavalinErrorCodeMapper;
import common.platform.javalin.registers.BusRegistry;
import common.platform.javalin.registers.DependencyRegistry;
import common.platform.javalin.registers.EndpointRegistry;
import common.platform.javalin.registers.ExceptionsRegistry;
import common.platform.javalin.registers.JavalinContext;
import common.platform.javalin.registers.RepositoryRegistry;
import common.platform.javalin.registers.UseCasesRegistry;
import common.platform.db.sqlite.HikariSqlite;
import io.javalin.Javalin;

class BootstrappingIT {

    private static Javalin app;

    @BeforeAll
    static void setup() {
        app = Javalin.create();

        ExceptionsRegistry expRegistry = JavalinContext.getRegistry(ExceptionsRegistry.class);
        assertNotNull(expRegistry, "ExceptionsRegistry debe existir");
        expRegistry.registry(app, new JavalinErrorCodeMapper());

        DependencyRegistry di = JavalinContext.getRegistry(DependencyRegistry.class);
        assertNotNull(di, "DependencyRegistry debe existir");

        HikariSqlite sqlite = new HikariSqlite();
        sqlite.initializeDatabase();

        RepositoryRegistry repoRegistry = JavalinContext.getRegistry(RepositoryRegistry.class);
        assertNotNull(repoRegistry, "RepositoryRegistry debe existir");
        repoRegistry.registry(sqlite);
        di = repoRegistry.injectRepositoriesInto(di);

        UseCasesRegistry useRegistry = JavalinContext.getRegistry(UseCasesRegistry.class);
        assertNotNull(useRegistry, "UseCasesRegistry debe existir");
        useRegistry.registry(di);

        BusRegistry busRegistry = JavalinContext.getRegistry(BusRegistry.class);
        assertNotNull(busRegistry, "BusRegistry debe existir");
        busRegistry.registry(useRegistry.getAllCommands(), useRegistry.getAllQueries());

        EndpointRegistry endRegistry = JavalinContext.getRegistry(EndpointRegistry.class);
        assertNotNull(endRegistry, "EndpointRegistry debe existir");
        endRegistry.registry(app, busRegistry.getCommandBus(), busRegistry.getQueryBus());
    }

    @Test
    void testPipelineInitialized() {
        DependencyRegistry di = JavalinContext.getRegistry(DependencyRegistry.class);
        UseCasesRegistry useRegistry = JavalinContext.getRegistry(UseCasesRegistry.class);
        BusRegistry busRegistry = JavalinContext.getRegistry(BusRegistry.class);

        assertNotNull(di);
        assertNotNull(useRegistry);
        assertNotNull(busRegistry);

        Map<Class<?>, Command<?, ?>> commands = useRegistry.getAllCommands();
        Map<Class<?>, Query<?, ?>> queries = useRegistry.getAllQueries();
        assertFalse(commands.isEmpty(), "Debe haber commands registrados");
        assertFalse(queries.isEmpty(), "Debe haber queries registrados");

        assertTrue(commands.containsKey(CreateCategoryUseCase.class), "Debe contener CreateCategoryUseCase");
        assertTrue(queries.containsKey(GetCategoryByIdUseCase.class), "Debe contener GetCategoryByIdUseCase");

        JavalinCommandBus commandBus = busRegistry.getCommandBus();
        JavalinQueryBus queryBus = busRegistry.getQueryBus();
        assertNotNull(commandBus);
        assertNotNull(queryBus);
    }

}
