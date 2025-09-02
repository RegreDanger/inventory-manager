package catalog.infra.adapters.in.javalin.modules;

import catalog.category.app.usecase.command.CreateCategoryUseCase;
import catalog.category.app.usecase.command.DeleteCategoryUseCase;
import catalog.category.app.usecase.command.UpdateCategoryUseCase;
import catalog.category.app.usecase.query.GetCategoryByIdUseCase;
import catalog.category.domain.ports.repository.CategoryRepository;
import common.platform.javalin.annotation.JavalinCommandHandler;
import common.platform.javalin.annotation.JavalinConstructorAutowired;
import common.platform.javalin.annotation.JavalinQueryHandler;
import common.platform.javalin.annotation.JavalinUseCaseModule;

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
