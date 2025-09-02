package catalog.category.app.usecase.command;

import catalog.category.domain.model.Category;
import catalog.category.domain.model.CategoryID;
import catalog.category.domain.ports.repository.CategoryRepository;
import catalog.infra.api.dto.category.UpdateCategoryDTO;
import catalog.infra.api.mappers.CategoryMapper;
import common.kernel.exceptions.api.NotFoundException;
import common.kernel.ports.cqrs.Command;

public class UpdateCategoryUseCase implements Command<UpdateCategoryDTO, Boolean>{
	private CategoryRepository categoryRepository;
	
	public UpdateCategoryUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Boolean handle(UpdateCategoryDTO input) {
		if(input.id() == null || input.id().isEmpty()) throw new IllegalArgumentException("ID is required for update");
		categoryRepository.findById(CategoryID.from(input.id())).orElseThrow(() -> new NotFoundException("Category"));
		Category category = CategoryMapper.fromUpdateDto(input);
		return categoryRepository.update(category);
	}
	
}
