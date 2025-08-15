package test.integration.sqlite.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.db.sqlite.HikariSqlite;


class DeleteCategoryIntegrationTest {
    private CategoryRepository repo;

    @BeforeEach
    void setUp() {
        HikariSqlite.initializeDatabase();
		repo = new CategoryRepositorySqlite(HikariSqlite.getConnection());
        repo.delete(CategoryID.from("8888-8888-8888-8888"));
    }

    @Test
    void shouldDeleteCategory() {
        Category category = new Category(CategoryID.from("9999-9999-9999-9999"), "Test Category", "Test Description");
        repo.create(category);
        boolean deleted = repo.delete(category.getId());
        assertEquals(true, deleted);
    }

    @Test
    void shouldNotFindDeletedCategory() {
        Category category = new Category(CategoryID.from("8888-8888-8888-8888"), "Test Category", "Test Description");
        repo.create(category);
        repo.delete(category.getId());
        assertEquals(true, repo.findById(category.getId()).isEmpty());
    }
}
