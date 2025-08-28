package test.unit.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import catalog.app.domain.model.category.CategoryID;
import catalog.app.domain.ports.repository.CategoryRepository;
import catalog.app.usecase.category.DeleteCategoryUseCase;


class DeleteCategoryUseCaseTest {
    @Mock
    private CategoryRepository mockRepo;
    @InjectMocks
    private DeleteCategoryUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDeleteCategorySuccess() {
        String categoryId = "8888-8888-8888-8888";
        CategoryID id = CategoryID.from(categoryId);
        when(mockRepo.delete(id)).thenReturn(true);
        boolean result = useCase.handle(categoryId);
        assertEquals(true, result);
    }

    @Test
    void testDeleteCategoryNotFound() {
        String categoryId = "8888-8888-8888-8888";
        CategoryID id = CategoryID.from(categoryId);
        when(mockRepo.delete(id)).thenReturn(false);
        boolean result = useCase.handle(categoryId);
        assertEquals(false, result);
    }
}
