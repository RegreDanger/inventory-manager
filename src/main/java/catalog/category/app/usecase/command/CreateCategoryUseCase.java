package catalog.category.app.usecase.command;

import catalog.category.domain.model.Category;
import catalog.category.domain.model.CategoryID;
import catalog.category.domain.ports.repository.CategoryRepository;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.api.mappers.CategoryMapper;
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
		Category category = CategoryMapper.fromCreateDto(dto);
		return categoryRepository.create(category);
	}
	
}
