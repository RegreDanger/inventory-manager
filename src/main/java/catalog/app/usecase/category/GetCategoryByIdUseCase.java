package catalog.app.usecase.category;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.ports.repository.CategoryRepository;
import common.kernel.exceptions.api.BadRequestException;
import common.kernel.exceptions.api.NotFoundException;
import common.kernel.ports.cqrs.Query;

public class GetCategoryByIdUseCase implements Query<String, Category> {
	private CategoryRepository categoryRepository;
	
	public GetCategoryByIdUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Category handle(String input) {
		if(input == null || input.isEmpty()) {
			throw new BadRequestException("The ID is required");
		}
		CategoryID id = CategoryID.from(input);
		return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category"));
	}
	
}
