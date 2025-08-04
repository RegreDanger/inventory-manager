# Inventory Manager

Inventory Manager is an open-source application designed to efficiently manage inventories. The project is currently under development, with the following short-term goals:

## Short-Term Goals

1. **Category Management**:
   - Create, update, and delete categories to organize products.

2. **Product Management**:
   - Register products with details such as name, description, price, and category.

3. **Sales Management**:
   - Record sales, associate sold products, and calculate totals.

## Technologies Used

- **Language**: Java
- **Framework**: Javalin
- **Database**: SQLite
- **Testing Framework**: JUnit
- **Build Tool**: Maven

## Project Architecture

The project follows a hybrid architecture combining **Domain-Driven Design (DDD)**, **Hexagonal Architecture**, and **Clean Architecture** principles.

### Layers

- **Domain Layer (app/domain)**  
  Contains the core business logic, entities, value objects, repositories (interfaces), and domain-specific exceptions.

- **Application Layer (app/usecase)**  
  Holds use cases that orchestrate the interaction between domain entities and infrastructure. It contains the logic that represents application-specific operations.

- **Infrastructure Layer (infra)**  
  Contains implementations of repositories (SQLite/JPA), API controllers, DTO mappers, and dependency injection containers.

- **Kernel (kernel)**  
  Shared components across all contexts: exceptions, enums, interfaces, mappers, and utilities without depending on any Framework.

- **Shared Infrastructure (infra/shared)**  
  Configuration files (e.g., database connection), shared mappers or adapters used by multiple contexts.

- **ACL (kernel/acl)**  
  Anti-Corruption Layer for context-to-context communication. Interfaces and adapters that isolate domain logic from external influence.

### Directory Structure Example

src/
├── main/
│   ├── java/
│   │   └── catalog/
│   │       ├── app/
│   │       │   ├── domain/
│   │       │   │   ├── exceptions/
│   │       │   │   │   └── category/
│   │       │   │   ├── model/
│   │       │   │   │   └── category/
│   │       │   │   └── repository/
│   │       │   │       └── category/
│   │       │   └── usecase/
│   │       │       └── category/
│   │       └── infra/
│   │           ├── api/
│   │           │   ├── dto/
│   │           │   │   └── category/
│   │           │   └── mappers/
│   │           │       └── category/
│   │           ├── db/
│   │           │   └── sqlite/
│   │           │       └── repository/
│   │           │           └── category/
│   │           └── di/
│   │               └── category/
│   ├── infra/
│   │   └── shared/
│   │       ├── config/
│   │       └── mappers/
│   │           └── javalin/
│   └── kernel/
│       ├── exceptions/
│       │   ├── impl/
│       │   └── interfaces/
│       ├── mappers/
│       ├── repository/
│       └── utils/
│           └── enums/

### Design Principles

- **Separation of concerns**: Each layer has a clear and distinct responsibility.
- **Dependency inversion**: Higher-level modules (use cases, domain) are not coupled to implementations.
- **Pluggability**: Infrastructure (like SQLite or Spring Data JPA) can be replaced without affecting core logic.
- **Testability**: Domain and application logic can be tested independently of the infrastructure.

---

This architecture supports future scalability and maintainability, keeping domain logic isolated, clean, and protected from external dependencies.

---

## Installation and Setup

1. Clone this repository:

   ```bash
   git clone https://github.com/RegreDanger/inventory-manager.git
   ```

2. Configure the SQLite database:
   - Ensure the `database.db` file is in the project root.

3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Run the tests:

   ```bash
   mvn test
   ```

## Next Steps

- Implement product management functionality.
- Develop a graphical user interface or API for system interaction.
- Add sales reports and statistics.

## License

This project is licensed under the **NonCommercial Open Source License**. See the `LICENSE` file for more details.

---

Thank you for using Inventory Manager! If you have any questions or suggestions, feel free to reach out.
