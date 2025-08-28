package test.unit.category;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import catalog.app.domain.ports.repository.CategoryRepository;
import catalog.app.usecase.category.CreateCategoryUseCase;
import catalog.infra.api.dto.category.CreateCategoryDTO;
import catalog.infra.api.mappers.category.CategoryDtoMapper;
import common.kernel.exceptions.api.DuplicatedException;

class CreateCategoryUseCaseTest {
	@Mock
	private CategoryRepository mockRepo;
	@InjectMocks
	private CreateCategoryUseCase useCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void insertSuccessful() {
		CreateCategoryDTO dto = new CreateCategoryDTO("Nombre", "Descripción");
		when(mockRepo.findByName(dto.name())).thenReturn(Optional.empty());
		assertDoesNotThrow(() -> {
			useCase.handle(dto);
		});
	}
	
	@Test
	void throwsAlreadyExists() {
		CreateCategoryDTO dto = new CreateCategoryDTO("Nombre", "Descripción");
		when(mockRepo.findByName(dto.name())).thenReturn(Optional.of(CategoryDtoMapper.fromCreateDto(dto)));
		Exception ex = assertThrows(DuplicatedException.class, () -> {
			useCase.handle(dto);
		});

		assertEquals("Category already exists!", ex.getMessage());
	}
}
