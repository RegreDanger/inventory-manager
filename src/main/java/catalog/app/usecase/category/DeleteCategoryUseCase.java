package catalog.app.usecase.category;

import kernel.exceptions.impl.BadRequestException;
import kernel.utils.enums.ErrorCode;

import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;


public class DeleteCategoryUseCase {
    private CategoryRepository categoryRepository;
	
	public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public boolean deleteCategory(String idCategory) {
		if(idCategory == null || idCategory.isEmpty()) {
			throw new BadRequestException("The ID is required", ErrorCode.BAD_REQUEST_ERROR);
		}
        CategoryID id = CategoryID.from(idCategory);
        return categoryRepository.delete(id);
    }
}
