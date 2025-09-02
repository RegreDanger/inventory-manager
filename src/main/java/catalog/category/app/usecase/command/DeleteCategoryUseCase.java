package catalog.category.app.usecase.command;

import catalog.category.domain.model.CategoryID;
import catalog.category.domain.ports.repository.CategoryRepository;
import common.kernel.exceptions.api.BadRequestException;
import common.kernel.ports.cqrs.Command;


public class DeleteCategoryUseCase implements Command<String, Boolean> {
    private CategoryRepository categoryRepository;
	
	public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Boolean handle(String input) {
		if(input == null || input.isEmpty()) {
			throw new BadRequestException("The ID is required");
		}
        CategoryID id = CategoryID.from(input);
        return categoryRepository.delete(id);
    }
}
