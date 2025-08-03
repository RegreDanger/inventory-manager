package catalog.infra.di.category;

import catalog.app.domain.repository.category.CategoryRepository;
import catalog.app.usecase.category.GetCategoryByIdUseCase;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.DatabaseConnection;

public class DiContainerCategoryReader {
    private static final CategoryRepository categoryRepositorySqlite = CategoryRepositorySqlite.getInstance(DatabaseConnection.getConnection());
    
    public GetCategoryByIdUseCase getGetCategoryByIdUseCaseSqlite() {
        return new GetCategoryByIdUseCase(categoryRepositorySqlite);
    }
}
