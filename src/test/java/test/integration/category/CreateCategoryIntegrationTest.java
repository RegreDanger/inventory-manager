package test.integration.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.db.sqlite.repository.category.CategoryRepositorySqlite;
import infra.shared.config.DatabaseConnection;
import kernel.exceptions.impl.InternalServerException;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CreateCategoryIntegrationTest {
	private CategoryRepository repo;
	
	@BeforeAll
	void setUp() {
		repo = CategoryRepositorySqlite.getInstance(DatabaseConnection.getConnection());
		repo.delete(CategoryID.from("8888-8888-8888-8888"));
	}
	
	@Test
	@Order(1)
    void shouldInsertCategory() {
        assertEquals("8888-8888-8888-8888", repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción")).getValue());
    }
	
	@Test
	@Order(2)
	void shouldThrowsWhenInsertingDuplicate() {
		Category duplicateCategory = new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción");
		Exception ex = assertThrows(InternalServerException.class, () -> repo.create(duplicateCategory));
		assertEquals("Internal Server Error", ex.getMessage());
		assertEquals("Internal Server Error", ex.getMessage());
	}
	
}
