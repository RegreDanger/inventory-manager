package catalog.infra.adapters.in.javalin.modules;

import catalog.app.domain.ports.repository.CategoryRepository;
import catalog.app.usecase.category.CreateCategoryUseCase;
import catalog.app.usecase.category.UpdateCategoryUseCase;
import catalog.app.usecase.category.DeleteCategoryUseCase;
import catalog.app.usecase.category.GetCategoryByIdUseCase;

import common.platform.config.javalin.annotation.JavalinCommandHandler;
import common.platform.config.javalin.annotation.JavalinConstructorAutowired;
import common.platform.config.javalin.annotation.JavalinQueryHandler;
import common.platform.config.javalin.annotation.JavalinUseCaseModule;

@JavalinUseCaseModule
public class CategoryUseCaseConfiguration {
    
    private CategoryRepository categoryRepository;

    @JavalinConstructorAutowired
    public CategoryUseCaseConfiguration(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @JavalinCommandHandler
    public CreateCategoryUseCase createCategoryUseCase() {
        return new CreateCategoryUseCase(categoryRepository);
    }

    @JavalinCommandHandler
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new UpdateCategoryUseCase(categoryRepository);
    }

    @JavalinCommandHandler
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DeleteCategoryUseCase(categoryRepository);
    }

    @JavalinQueryHandler
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new GetCategoryByIdUseCase(categoryRepository);
    }

}
