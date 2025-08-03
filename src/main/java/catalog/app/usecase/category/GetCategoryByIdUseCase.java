package catalog.app.usecase.category;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import kernel.exceptions.impl.BadRequestException;
import kernel.exceptions.impl.NotFoundException;
import kernel.utils.enums.ErrorCode;

public class GetCategoryByIdUseCase {
	private CategoryRepository categoryRepository;
	
	public GetCategoryByIdUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public Category getCategoryById(String idCategory) {
		if(idCategory == null || idCategory.isEmpty()) {
			throw new BadRequestException("The ID is required", ErrorCode.BAD_REQUEST_ERROR);
		}
		CategoryID id = CategoryID.from(idCategory);
		return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category", ErrorCode.NOT_FOUND_ERROR));
	}
	
}
