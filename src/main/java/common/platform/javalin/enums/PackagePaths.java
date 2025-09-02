package common.platform.javalin.enums;

public enum PackagePaths {
    EXCEPTIONS_PATH("common.kernel.exceptions"),
    SQLITE_REPOSITORY_PATH("catalog.infra.adapters.out.repository"),
    JAVALIN_REGISTRY_PATH("common.platform.javalin.registers"),
    JAVALIN_MODULES_PATH("catalog.infra.adapters.in.javalin.modules"),
    JAVALIN_CQRS_PATH("common.platform.javalin.adapters.cqrs");

    private final String path;

    private PackagePaths(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return this.path;
    }

}
