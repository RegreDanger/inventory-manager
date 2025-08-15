package catalog.app.usecase.category;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.api.dto.category.UpdateCategoryDTO;
import catalog.infra.api.mappers.category.CategoryDtoMapper;
import kernel.impl.exceptions.NotFoundException;
import kernel.utils.enums.ErrorCode;

public class UpdateCategoryUseCase {
	private CategoryRepository categoryRepository;
	
	public UpdateCategoryUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public boolean updateCategory(UpdateCategoryDTO dto) {
		if(dto.id() == null || dto.id().isEmpty()) throw new IllegalArgumentException("ID is required for update");
		categoryRepository.findById(CategoryID.from(dto.id())).orElseThrow(() -> new NotFoundException("Category", ErrorCode.NOT_FOUND_ERROR));
		Category category = CategoryDtoMapper.fromUpdateDto(dto);
		return categoryRepository.update(category);
	}
	
}
