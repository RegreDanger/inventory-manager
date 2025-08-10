package test.integration.sqlite.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.db.sqlite.DatabaseSqliteConnection;
import kernel.exceptions.impl.InternalServerException;

class CreateCategoryIntegrationTest {
	private CategoryRepository repo;
	
	@BeforeEach
	void setUp() {
		DatabaseSqliteConnection.initializeConnection();
		repo = CategoryRepositorySqlite.getInstance(DatabaseSqliteConnection.getConnection());
		repo.delete(CategoryID.from("8888-8888-8888-8888"));
	}
	
	@Test
    void shouldInsertCategory() {
        assertEquals("8888-8888-8888-8888", repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción")).getValue());
    }
	
	@Test
	void shouldThrowsWhenInsertingDuplicate() {
		repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción"));
		Category duplicateCategory = new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción");
		Exception ex = assertThrows(InternalServerException.class, () -> repo.create(duplicateCategory));
		assertEquals("Internal Server Error", ex.getMessage());
		assertEquals("Internal Server Error", ex.getMessage());
	}
	
}
