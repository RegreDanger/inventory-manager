package catalog.app.usecase.category;

import catalog.app.domain.exceptions.category.CategoryDuplicatedException;
import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.repository.category.CategoryRepository;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.api.mappers.category.CategoryDtoMapper;
import kernel.utils.enums.ErrorCode;

public class CreateCategoryUseCase {
	private CategoryRepository categoryRepository;
	
	public CreateCategoryUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public CategoryID createCategory(CreateCategoryDTO dto) {
		categoryRepository.findByName(dto.name()).ifPresent(c -> {
			throw new CategoryDuplicatedException("Category already exists!", ErrorCode.CONFLICT_ERROR);
		});
		Category category = CategoryDtoMapper.fromCreateDto(dto);
		return categoryRepository.create(category);
	}
	
}
