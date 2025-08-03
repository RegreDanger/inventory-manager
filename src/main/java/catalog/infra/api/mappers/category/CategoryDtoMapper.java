package catalog.infra.api.mappers.category;

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.api.dto.category.UpdateCategoryDTO;

public class CategoryDtoMapper {
	private CategoryDtoMapper() {}
	
	public static Category fromCreateDto(CreateCategoryDTO dto) {
		return new Category(CategoryID.generate(dto.name(), dto.description()), dto.name(), dto.description());
	}
	
	public static Category fromUpdateDto(UpdateCategoryDTO dto) {
		return new Category(CategoryID.from(dto.id()), dto.name(), dto.description());
	}
	
}
