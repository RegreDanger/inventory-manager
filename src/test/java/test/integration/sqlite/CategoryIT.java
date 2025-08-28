package test.integration.sqlite;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.ports.repository.CategoryRepository;
import catalog.infra.adapters.out.repository.CategoryRepositorySqlite;
import common.platform.config.db.sqlite.HikariSqlite;
import common.kernel.exceptions.api.InternalServerException;

class CategoryIT {

    private CategoryRepository repo;

    @BeforeEach
    void setUp() {
        HikariSqlite sqlite = new HikariSqlite();
        sqlite.initializeDatabase();
        repo = new CategoryRepositorySqlite(sqlite);
        repo.delete(CategoryID.from("8888-8888-8888-8888"));
        repo.delete(CategoryID.from("9999-9999-9999-9999"));
    }

    // CREATE
    @Test
    void shouldInsertCategory() {
        assertEquals("8888-8888-8888-8888",
            repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción")).getValue()
        );
    }

    @Test
    void shouldThrowWhenInsertingDuplicate() {
        repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción"));
        Category duplicate = new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción");
        assertThrows(InternalServerException.class, () -> repo.create(duplicate));
    }

    // READ
    @Test
    void shouldFindCategoryByIdAndName() {
        repo.create(new Category(CategoryID.from("8888-8888-8888-8888"), "Nombre", "Descripción"));
        assertTrue(repo.findById(CategoryID.from("8888-8888-8888-8888")).isPresent());
        assertTrue(repo.findByName("Nombre").isPresent());
    }

    @Test
    void shouldBeEmptyWhenNotFound() {
        assertTrue(repo.findById(CategoryID.from("8888-8888-8888-8888")).isEmpty());
        assertTrue(repo.findByName("Nombre").isEmpty());
    }

    // UPDATE
    @Test
    void shouldUpdateCategory() {
        Category category = new Category(CategoryID.from("8888-8888-8888-8888"), "Old Name", "Old Desc");
        repo.create(category);
        category.setName("New Name");
        category.setDescription("New Desc");
        repo.update(category);

        Category updated = repo.findById(CategoryID.from("8888-8888-8888-8888")).orElseThrow();
        assertEquals("New Name", updated.getName());
        assertEquals("New Desc", updated.getDescription());
    }

    // DELETE
    @Test
    void shouldDeleteCategory() {
        Category category = new Category(CategoryID.from("9999-9999-9999-9999"), "Test Category", "Test Desc");
        repo.create(category);
        boolean deleted = repo.delete(category.getId());
        assertTrue(deleted);
    }

    @Test
    void shouldNotFindDeletedCategory() {
        Category category = new Category(CategoryID.from("8888-8888-8888-8888"), "Test Category", "Test Desc");
        repo.create(category);
        repo.delete(category.getId());
        assertTrue(repo.findById(category.getId()).isEmpty());
    }
}

