# Product Inventory API

A RESTful API for managing products in an inventory system, developed with **Spring Boot**, **Maven**, **Java 17+**,
**MapStruct**, and **H2 Database**.

## 📚 Features

- Create, retrieve, update, and delete products
- Create, retrieve, update, and delete categories
- Assign and remove categories from products
- Pagination support for listing products and categories
- Validation of request data
- Optimistic locking to prevent concurrent updates
- Comprehensive error handling with proper HTTP responses
- API documentation with **Swagger/OpenAPI**
- Unit and integration tests with **JUnit 5** and **Mockito**

---

## 🚀 Getting Started

### 🖥️ Requirements

- **Java 17+**
- **Maven 3.6+**

---

### ⚙️ Build and Run

```bash
# Clean and compile the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at:  
`http://localhost:8080`

---

### 🌐 API Documentation (Swagger/OpenAPI)

Access the Swagger UI at:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

This interface allows you to explore and test all API endpoints with sample requests and responses.

---

### 🧪 Running Tests

Run all unit and integration tests using:

```bash
mvn test
```

---

### 🗂️ Project Structure

```
src/
 └── main/
     ├── java/
     │    └── com/phoenix/productinventory/
     │          ├── controller/       # REST Controllers
     │          ├── dto/              # Data Transfer Objects (Request/Response)
     │          ├── exception/        # Custom exception classes and handlers
     │          ├── mapper/           # MapStruct mappers
     │          ├── model/            # JPA entities
     │          ├── repository/       # Spring Data JPA repositories
     │          ├── service/          # Business logic and service layer
     │          └── ProductInventoryApplication.java
     └── resources/
         └── application.properties   # App configuration
```

---

### 💡 Notes

- **Database**: An embedded **H2 Database** is used for simplicity.
- **Pagination**: Supports query parameters `page` and `size`.
- **Validation**: All input data is validated, and errors return meaningful responses.
- **Optimistic Locking**: Updates may fail with HTTP `409 Conflict` if another transaction modified the data.
- **Exception Handling**: All exceptions are translated into appropriate HTTP responses.
- **Category Relationship**: Each product can be assigned to a category. Use the dedicated endpoints to manage these
  associations.
- **Pagination Serialization**: Pagination responses are serialized using `PageSerializationMode.VIA_DTO` for a stable
  and predictable JSON structure.

---

### 👤 Author

Developed by **Marc Pedemonte** for a software engineering interview.
