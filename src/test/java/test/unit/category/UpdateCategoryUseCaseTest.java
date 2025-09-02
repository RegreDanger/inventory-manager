package test.unit.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import catalog.category.app.usecase.command.UpdateCategoryUseCase;
import catalog.category.domain.model.Category;
import catalog.category.domain.model.CategoryID;
import catalog.category.domain.ports.repository.CategoryRepository;
import catalog.infra.api.dto.category.UpdateCategoryDTO;
import common.kernel.exceptions.api.NotFoundException;

class UpdateCategoryUseCaseTest {
	@Mock
	private CategoryRepository mockRepo;
	@InjectMocks
	private UpdateCategoryUseCase useCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void updateSuccessful() {
		when(mockRepo.update(new Category(CategoryID.from("8767-4567-7890"), "Nombre", "Descripción"))).thenReturn(true);
		when(mockRepo.findById(CategoryID.from("8767-4567-7890"))).thenReturn(Optional.ofNullable(new Category(CategoryID.from("8767-4567-7890"), "Nombre", "Descripción")));
		boolean rs = useCase.handle(new UpdateCategoryDTO("8767-4567-7890", "Nombre", "Descripción"));
		assertEquals(true, rs);
	}
	
	@Test
	void throwsNotFound() {
		when(mockRepo.findById(CategoryID.from("8767-4567-7890"))).thenReturn(Optional.empty());
		UpdateCategoryDTO category = new UpdateCategoryDTO("8767-4567-7890", "Nombre", "Descripción");
		Exception ex = assertThrows(NotFoundException.class, () -> useCase.handle(category));
		assertEquals("Category Not Found", ex.getMessage());
	}
}
