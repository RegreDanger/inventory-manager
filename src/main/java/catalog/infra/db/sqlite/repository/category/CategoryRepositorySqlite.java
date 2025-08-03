package catalog.infra.db.sqlite.repository.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import kernel.exceptions.impl.InternalServerException;
import kernel.utils.enums.ErrorCode;

public class CategoryRepositorySqlite implements CategoryRepository {
	private static CategoryRepository instance;
	private Connection connection;
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	
	private CategoryRepositorySqlite(Connection connection) {
		this.connection = connection;
		String sql = "CREATE TABLE IF NOT EXISTS category ("
                +  "id TEXT NOT NULL,"
                +  "name TEXT NOT NULL,"
                +  "description TEXT NOT NULL,"
                +  "CONSTRAINT pk_category PRIMARY KEY (id),"
                +  "CONSTRAINT uq_category_name UNIQUE (name)"
                +  ");";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
        	throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
        }
	}
	
	public static CategoryRepository getInstance(Connection connection) {
		if (instance == null) {
			instance = new CategoryRepositorySqlite(connection);
		}
		return instance;
	}
	
	@Override
	public CategoryID create(Category category) {
		String query = "INSERT INTO category (id, name, description) VALUES (?, ?, ?)";
		try(PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, category.getId().getValue());
			stmt.setString(2, category.getName());
			stmt.setString(3, category.getDescription());
			stmt.executeUpdate();
			return category.getId();
		} catch (SQLException e) {
			throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Optional<Category> findById(CategoryID id) {
		String query = "SELECT name, description FROM category WHERE id = ?";
		Optional<Category> result = Optional.empty();
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, id.getValue());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString(NAME);
				String description = rs.getString(DESCRIPTION);
				result = Optional.ofNullable(new Category(id, name, description));
			}
			return result;
		} catch (SQLException e) {
			throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Optional<Category> findByName(String name) {
		String query = "SELECT id, description FROM category WHERE name = ?";
		Optional<Category> result = Optional.empty();
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				CategoryID id = CategoryID.from(rs.getString(ID));
				String description = rs.getString(DESCRIPTION);
				result = Optional.ofNullable(new Category(id, name, description));
			}
			return result;
		} catch (SQLException e) {
			throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Optional<List<Category>> findAll() {
		List<Category> linkedList = new LinkedList<>();
		String query = "SELECT id, name, description FROM category";
		try(PreparedStatement stmt = connection.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				linkedList.add(new Category(CategoryID.from(rs.getString(ID)), rs.getString(NAME), rs.getString(DESCRIPTION)));
			}
			return Optional.ofNullable(linkedList)
					.filter(list -> !list.isEmpty());
		} catch (SQLException e) {
			throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public boolean update(Category category) {
		String query = "UPDATE category SET name = ?, description = ? WHERE id = ?";
		try(PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, category.getName());
			stmt.setString(2, category.getDescription());
			stmt.setString(3, category.getId().getValue());
			int affected = stmt.executeUpdate();
			return affected > 0;
		} catch (SQLException e) {
			throw new InternalServerException("", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public boolean delete(CategoryID id) {
		String query = "DELETE FROM category WHERE id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.getValue());
            int affected = stmt.executeUpdate();
            return affected > 0;
		} catch (SQLException e) {
			throw new InternalServerException("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
}
