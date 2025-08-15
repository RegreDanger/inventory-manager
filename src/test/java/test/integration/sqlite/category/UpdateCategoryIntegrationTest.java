package test.integration.sqlite.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.db.sqlite.HikariSqlite;

class UpdateCategoryIntegrationTest {
    private CategoryRepository repo;

	@BeforeEach
	void setUp() {
		HikariSqlite.initializeDatabase();
		repo = new CategoryRepositorySqlite(HikariSqlite.getConnection());
		repo.delete(CategoryID.from("8888-8888-8888-8888"));
	}

    @Test
    void shouldUpdateCategory() {
        Category category = new Category(CategoryID.from("8888-8888-8888-8888"), "Old Name", "Old Description");
        repo.create(category);
        category.setName("New Name");
        category.setDescription("New Description");
        repo.update(category);
        Category updatedCategory = repo.findById(CategoryID.from("8888-8888-8888-8888")).orElseThrow();
        assertEquals("New Name", updatedCategory.getName());
        assertEquals("New Description", updatedCategory.getDescription());
    }

}
