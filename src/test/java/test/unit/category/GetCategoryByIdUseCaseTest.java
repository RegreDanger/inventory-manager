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

import catalog.app.domain.model.category.Category;
import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.ports.repository.CategoryRepository;
import catalog.app.usecase.category.GetCategoryByIdUseCase;
import common.kernel.exceptions.api.NotFoundException;

class GetCategoryByIdUseCaseTest {
	@Mock
	private CategoryRepository mockRepo;
	@InjectMocks
	private GetCategoryByIdUseCase useCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void getSuccessful() {
		Category rt = new Category(CategoryID.from("8767-4567-7890"), "Nombre", "Descripción");
		when(mockRepo.findById(CategoryID.from("8767-4567-7890"))).thenReturn(Optional.ofNullable(rt));
		Category category = useCase.handle("8767-4567-7890");
		assertEquals("8767-4567-7890", category.getId().getValue());
	}
	
	@Test
	void throwsNotFound() {
		when(mockRepo.findById(CategoryID.from("8767-4567-7890"))).thenReturn(Optional.empty());
		Exception ex = assertThrows(NotFoundException.class, () -> {
			useCase.handle("8767-4567-7890");
		});
		
		assertEquals("Category Not Found", ex.getMessage());
	}
}
