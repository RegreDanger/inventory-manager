package catalog.infra.di.category;

import catalog.app.domain.repository.category.CategoryRepository;
import catalog.app.usecase.category.CreateCategoryUseCase;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.DatabaseConnection;

public class DiContainerCategoryCreator {

    private static final CategoryRepository categoryRepositorySqlite = CategoryRepositorySqlite.getInstance(DatabaseConnection.getConnection());
    
    public CreateCategoryUseCase getCreateCategoryUseCaseSqlite() {
        return new CreateCategoryUseCase(categoryRepositorySqlite);
    }
}
