package test.integration.sqlite.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.db.sqlite.DatabaseSqliteConnection;

class GetCategoryByIdIntegrationTest {
	private CategoryRepository repo;
	
	@BeforeEach
	void setUp() {
		DatabaseSqliteConnection.initializeConnection();
		repo = CategoryRepositorySqlite.getInstance(DatabaseSqliteConnection.getConnection());
		repo.delete(CategoryID.from("8888-8888-8888-8888"));
	}
	
	@Test
    void shouldFindCategoryByIdAndName() {
		repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción"));
        assertEquals(true, repo.findById(CategoryID.from("8888-8888-8888-8888")).isPresent());
        assertEquals(true, repo.findByName("Nombre").isPresent());
    }
	
	@Test
	void shouldBeEmptyWhenNotFound() {
		assertEquals(true, repo.findById(CategoryID.from("8888-8888-8888-8888")).isEmpty());
        assertEquals(true, repo.findByName("Nombre").isEmpty());
	}
}
