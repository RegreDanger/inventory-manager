package bootstrap;
import catalog.app.usecase.category.CreateCategoryUseCase;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.db.sqlite.HikariSqlite;

public class Main {
    public static void main(String[] args) {
        HikariSqlite.initializeDatabase();
        CreateCategoryUseCase createCategoryUseCase = new CreateCategoryUseCase(new CategoryRepositorySqlite(HikariSqlite.getConnection()));
        createCategoryUseCase.createCategory(new CreateCategoryDTO("name", "description"));
    }
}
