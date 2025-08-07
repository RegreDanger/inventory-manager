package catalog.infra.di.category;

import catalog.app.domain.repository.category.CategoryRepository;
import catalog.app.usecase.category.UpdateCategoryUseCase;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.db.sqlite.DatabaseSqliteConnection;

public class DiContainerCategoryUpdater {
    private static final CategoryRepository categoryRepositorySqlite = CategoryRepositorySqlite.getInstance(DatabaseSqliteConnection.getConnection());

    public UpdateCategoryUseCase getUpdateCategoryUseCaseSqlite() {
        return new UpdateCategoryUseCase(categoryRepositorySqlite);
    }
}
