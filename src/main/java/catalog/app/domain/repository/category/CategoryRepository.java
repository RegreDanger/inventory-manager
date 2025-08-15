package catalog.app.domain.repository.category;

import java.util.List;
import java.util.Optional;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import kernel.interfaces.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, CategoryID> {
	
	@Override
	CategoryID create(Category category);
	
	@Override
	Optional<Category> findById(CategoryID id);
	
	Optional<Category> findByName(String name);
	
	@Override
	Optional<List<Category>> findAll();
	
	@Override
	boolean update(Category category);
	
	@Override
	boolean delete(CategoryID id);
	
}