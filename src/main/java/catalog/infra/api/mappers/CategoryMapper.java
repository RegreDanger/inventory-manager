package catalog.infra.api.mappers;

import catalog.category.domain.model.Category;
import catalog.category.domain.model.CategoryID;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.api.dto.category.UpdateCategoryDTO;

public class CategoryMapper {
	private CategoryMapper() {}
	
	public static Category fromCreateDto(CreateCategoryDTO dto) {
		return new Category(CategoryID.generate(dto.name(), dto.description()), dto.name(), dto.description());
	}
	
	public static Category fromUpdateDto(UpdateCategoryDTO dto) {
		return new Category(CategoryID.from(dto.id()), dto.name(), dto.description());
	}
	
}
