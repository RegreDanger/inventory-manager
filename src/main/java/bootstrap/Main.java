package bootstrap;

import catalog.app.usecase.category.CreateCategoryUseCase;
import catalog.infra.adapters.out.repository.CategoryRepositorySqlite;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import common.platform.config.db.sqlite.HikariSqlite;

public class Main {
    public static void main(String[] args) {
        HikariSqlite sqlite = new HikariSqlite();
        sqlite.initializeDatabase();
        CreateCategoryUseCase createCategoryUseCase = new CreateCategoryUseCase(new CategoryRepositorySqlite(sqlite));
        createCategoryUseCase.handle(new CreateCategoryDTO("name", "description"));
    }
}
