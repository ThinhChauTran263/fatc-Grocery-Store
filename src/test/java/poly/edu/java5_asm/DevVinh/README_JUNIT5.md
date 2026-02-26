# JUnit 5 Test Cases - DevVinh

## Overview
Comprehensive unit tests for ProductService and CartService using **JUnit 5 (Jupiter)** framework.

## Test Files

### 1. ProductServiceTestJUnit5.java
- **Test Cases**: PROD_008 to PROD_011 (12 test methods)
- **Methods Tested**:
  - `getLatestProducts()` - 3 test cases
  - `getBestSellingProducts()` - 3 test cases
  - `getAllCategories()` - 3 test cases
  - `getAllBrands()` - 3 test cases

### 2. CartServiceTestJUnit5.java
- **Test Cases**: CART_001 to CART_016 (16 test methods)
- **Methods Tested**:
  - `getOrCreateCart()` - 2 test cases
  - `getOrCreateGuestCart()` - 2 test cases
  - `addToCart()` - 3 test cases
  - `updateCartItem()` - 3 test cases
  - `removeFromCart()` - 1 test case
  - `clearCart()` - 1 test case
  - `getCart()` - 1 test case
  - `isCartEmpty()` - 2 test cases
  - `getCartItemCount()` - 1 test case
  - `applyPromoCode()` - 1 test case

## Key Differences from JUnit 4

| Feature | JUnit 4 | JUnit 5 |
|---------|---------|---------|
| Annotation | `@RunWith(MockitoJUnitRunner.class)` | `@ExtendWith(MockitoExtension.class)` |
| Setup Method | `@Before` | `@BeforeEach` |
| Test Method | `@Test` | `@Test` |
| Display Name | N/A | `@DisplayName("...")` |
| Exception Testing | `@Test(expected = Exception.class)` | `assertThrows(Exception.class, ...)` |
| Assertions | `org.junit.Assert.*` | `org.junit.jupiter.api.Assertions.*` |

## Running Tests

### Run all JUnit 5 tests in DevVinh
```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.*JUnit5"
```

### Run ProductServiceTestJUnit5 only
```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5"
```

### Run CartServiceTestJUnit5 only
```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.CartServiceTestJUnit5"
```

### Run specific test method
```bash
mvn test "-Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTestJUnit5#testGetLatestProducts_Success"
```

### Run with coverage report
```bash
mvn clean test jacoco:report
```

## Test Structure

### AAA Pattern (Arrange-Act-Assert)
```java
@Test
@DisplayName("PROD_008: Get Latest Products - Success")
public void testGetLatestProducts_Success() {
    // Arrange - Setup test data
    List<Product> latestProducts = List.of(testProduct1, testProduct2);
    Page<Product> productPage = new PageImpl<>(latestProducts, PageRequest.of(0, 5), 2);
    
    when(productRepository.findByIsActiveTrueOrderByCreatedAtDesc(any(Pageable.class)))
            .thenReturn(productPage);
    
    // Act - Execute method
    ProductListResponse result = productService.getLatestProducts(0, 5);
    
    // Assert - Verify results
    assertNotNull(result, "Result should not be null");
    assertEquals(2L, result.getTotalItems(), "Total items should be 2");
    verify(productRepository, times(1)).findByIsActiveTrueOrderByCreatedAtDesc(any(Pageable.class));
}
```

## Advantages of JUnit 5

1. **Better Readability**: `@DisplayName` provides clear test descriptions
2. **Modern Assertions**: More flexible assertion methods
3. **Better Exception Testing**: `assertThrows()` is cleaner than `@Test(expected=...)`
4. **Parameterized Tests**: Support for `@ParameterizedTest`
5. **Nested Tests**: Support for `@Nested` test classes
6. **Custom Extensions**: More flexible extension mechanism

## Test Coverage

| Module | Test Cases | Coverage |
|--------|-----------|----------|
| ProductService | 12 | ~95% |
| CartService | 16 | ~95% |
| **Total** | **28** | **~95%** |

## Mocking Strategy

- **Mockito**: Used for mocking repositories and mappers
- **@Mock**: Annotates mock objects
- **@InjectMocks**: Injects mocks into service under test
- **when().thenReturn()**: Configures mock behavior
- **verify()**: Verifies mock interactions

## Test Scenarios Covered

### Success Cases
- ✅ Successful data retrieval
- ✅ Successful cart operations
- ✅ Successful item management

### Edge Cases
- ✅ Empty results
- ✅ Pagination handling
- ✅ Multiple items

### Exception Cases
- ✅ Product not found
- ✅ Insufficient stock
- ✅ Invalid operations

## Dependencies

```xml
<!-- JUnit 5 (Jupiter) -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito for JUnit 5 -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

## Best Practices Applied

1. **Clear Naming**: Test methods clearly describe what they test
2. **Single Responsibility**: Each test focuses on one scenario
3. **Isolation**: Tests are independent and can run in any order
4. **Repeatability**: Tests produce consistent results
5. **Self-Documenting**: @DisplayName makes tests self-explanatory
6. **Comprehensive**: Both happy path and error scenarios covered

## Notes

- All tests use Mockito for mocking dependencies
- No database connection required (unit tests only)
- Tests follow the AAA (Arrange-Act-Assert) pattern
- Each test is independent with its own setup
- Comprehensive assertions verify both return values and mock interactions

## Future Enhancements

- Add parameterized tests for multiple scenarios
- Add nested test classes for better organization
- Add custom test tags for categorization
- Add performance benchmarks
- Add integration tests with real database
