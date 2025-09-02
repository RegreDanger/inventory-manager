package catalog.category.domain.ports.repository;

import java.util.List;
import java.util.Optional;

import catalog.category.domain.model.Category;
import catalog.category.domain.model.CategoryID;
import common.kernel.ports.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, CategoryID> {
	
	@Override
	CategoryID create(Category category);
	
	@Override
	Optional<Category> findById(CategoryID id);
	
	Optional<Category> findByName(String name);
	
	@Override
	List<Category> findAll();
	
	@Override
	boolean update(Category category);
	
	@Override
	boolean delete(CategoryID id);
	
}