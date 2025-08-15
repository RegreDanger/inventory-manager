package catalog.infra.db.sqlite.repository.category;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import kernel.impl.exceptions.InternalServerException;
import kernel.utils.LoggingUtils;
import kernel.utils.enums.ErrorCode;
import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import org.slf4j.Logger;


public class CategoryRepositorySqlite implements CategoryRepository {
    private final Connection conn;
    private static final Logger logger = LoggingUtils.getLogger(CategoryRepositorySqlite.class);
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    public CategoryRepositorySqlite(Connection conn) {
        if (conn == null) {
            Throwable cause = new NullPointerException("Connection null");
            throw new InternalServerException("Connection cannot be null", ErrorCode.INTERNAL_SERVER_ERROR, cause);
        }
        this.conn = conn;
	}

    @Override
    public CategoryID create(Category category) {
        String query = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getId().getValue());
            stmt.setString(2, category.getName());
            stmt.setString(3, category.getDescription());
            stmt.executeUpdate();

            logger.info("Category created successfully: {}", category);
            return category.getId();

        } catch (SQLException e) {
            throw new InternalServerException("Failed to create category in the database.", ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        String query = "SELECT name, description FROM category WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id.getValue());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(id, rs.getString(NAME), rs.getString(DESCRIPTION));
                    logger.info("Category found by ID: {}", id);
                    return Optional.of(category);
                } else {
                    logger.warn("No category found with ID: {}", id);
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new InternalServerException("Failed to retrieve category by ID from the database.", ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Optional<Category> findByName(String name) {
        String query = "SELECT id, description FROM category WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CategoryID id = CategoryID.from(rs.getString(ID));
                    Category category = new Category(id, name, rs.getString(DESCRIPTION));
                    logger.info("Category found by name: {}", name);
                    return Optional.of(category);
                } else {
                    logger.warn("No category found with name: {}", name);
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new InternalServerException("Failed to retrieve category by name from the database.", ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Optional<List<Category>> findAll() {
        String query = "SELECT id, name, description FROM category";
        List<Category> categories = new LinkedList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(new Category(
                        CategoryID.from(rs.getString(ID)),
                        rs.getString(NAME),
                        rs.getString(DESCRIPTION)
                ));
            }

            logger.info("Retrieved all categories. Count: {}", categories.size());
            return Optional.ofNullable(categories).filter(list -> !list.isEmpty());

        } catch (SQLException e) {
            throw new InternalServerException("Failed to retrieve all categories from the database.", ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public boolean update(Category category) {
        String query = "UPDATE category SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setString(3, category.getId().getValue());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                logger.info("Category updated successfully: {}", category);
                return true;
            } else {
                logger.warn("No category updated. ID: {}", category.getId());
                return false;
            }

        } catch (SQLException e) {
            throw new InternalServerException("Failed to update category in the database.", ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public boolean delete(CategoryID id) {
        String query = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id.getValue());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                logger.info("Category deleted successfully. ID: {}", id);
                return true;
            } else {
                logger.warn("No category deleted. ID: {}", id);
                return false;
            }

        } catch (SQLException e) {
            throw new InternalServerException("Failed to delete category from the database.", ErrorCode.INTERNAL_SERVER_ERROR, e);
        }
    }
}
