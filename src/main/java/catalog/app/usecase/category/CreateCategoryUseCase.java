package catalog.app.usecase.category;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.ports.repository.CategoryRepository;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.api.mappers.category.CategoryDtoMapper;
import common.kernel.exceptions.api.DuplicatedException;
import common.kernel.ports.cqrs.Command;

public class CreateCategoryUseCase implements Command<CreateCategoryDTO, CategoryID> {
	private CategoryRepository categoryRepository;
	
	public CreateCategoryUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public CategoryID handle(CreateCategoryDTO dto) {
		categoryRepository.findByName(dto.name()).ifPresent(c -> {
			throw new DuplicatedException("Category duplicated");
		});
		Category category = CategoryDtoMapper.fromCreateDto(dto);
		return categoryRepository.create(category);
	}
	
}
