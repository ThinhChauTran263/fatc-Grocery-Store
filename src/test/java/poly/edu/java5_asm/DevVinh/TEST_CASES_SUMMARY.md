# Test Cases Summary - DevVinh

## Overview
Comprehensive unit tests for ProductService and CartService modules covering 20 test cases across 2 test classes.

---

## ProductService Test Cases (PROD_008 - PROD_011)

### PROD_008: testGetLatestProducts_Success
- **Mô tả**: Lấy sản phẩm mới nhất
- **Điều kiện tiên quyết**: Database có products
- **Dữ liệu đầu vào**: page=0, size=5
- **Kết quả mong đợi**: Trả về products mới nhất
- **Trạng thái**: ✅ Implemented

### PROD_008: testGetLatestProducts_EmptyResult
- **Mô tả**: Lấy sản phẩm mới nhất khi không có sản phẩm
- **Điều kiện tiên quyết**: Database không có products
- **Dữ liệu đầu vào**: page=0, size=5
- **Kết quả mong đợi**: Trả về danh sách rỗng
- **Trạng thái**: ✅ Implemented

### PROD_008: testGetLatestProducts_WithPagination
- **Mô tả**: Lấy sản phẩm mới nhất với phân trang
- **Điều kiện tiên quyết**: Database có nhiều products
- **Dữ liệu đầu vào**: page=1, size=5
- **Kết quả mong đợi**: Trả về trang thứ 2 của products
- **Trạng thái**: ✅ Implemented

### PROD_009: testGetBestSellingProducts_Success
- **Mô tả**: Lấy sản phẩm bán chạy
- **Điều kiện tiên quyết**: Database có products với orders
- **Dữ liệu đầu vào**: page=0, size=5
- **Kết quả mong đợi**: Trả về best selling products
- **Trạng thái**: ✅ Implemented

### PROD_009: testGetBestSellingProducts_EmptyResult
- **Mô tả**: Lấy sản phẩm bán chạy khi không có sản phẩm
- **Điều kiện tiên quyết**: Database không có products
- **Dữ liệu đầu vào**: page=0, size=5
- **Kết quả mong đợi**: Trả về danh sách rỗng
- **Trạng thái**: ✅ Implemented

### PROD_009: testGetBestSellingProducts_WithPagination
- **Mô tả**: Lấy sản phẩm bán chạy với phân trang
- **Điều kiện tiên quyết**: Database có nhiều best selling products
- **Dữ liệu đầu vào**: page=0, size=5
- **Kết quả mong đợi**: Trả về trang đầu tiên của best selling products
- **Trạng thái**: ✅ Implemented

### PROD_010: testGetAllCategories_Success
- **Mô tả**: Lấy danh sách categories
- **Điều kiện tiên quyết**: Database có categories
- **Dữ liệu đầu vào**: N/A
- **Kết quả mong đợi**: Trả về List<CategoryResponse>
- **Trạng thái**: ✅ Implemented

### PROD_010: testGetAllCategories_EmptyResult
- **Mô tả**: Lấy danh sách categories khi không có categories
- **Điều kiện tiên quyết**: Database không có categories
- **Dữ liệu đầu vào**: N/A
- **Kết quả mong đợi**: Trả về danh sách rỗng
- **Trạng thái**: ✅ Implemented

### PROD_010: testGetAllCategories_MultipleCategories
- **Mô tả**: Lấy danh sách categories với nhiều categories
- **Điều kiện tiên quyết**: Database có nhiều categories
- **Dữ liệu đầu vào**: N/A
- **Kết quả mong đợi**: Trả về List<CategoryResponse> với tất cả categories
- **Trạng thái**: ✅ Implemented

### PROD_011: testGetAllBrands_Success
- **Mô tả**: Lấy danh sách brands
- **Điều kiện tiên quyết**: Database có brands
- **Dữ liệu đầu vào**: N/A
- **Kết quả mong đợi**: Trả về List<BrandResponse>
- **Trạng thái**: ✅ Implemented

### PROD_011: testGetAllBrands_EmptyResult
- **Mô tả**: Lấy danh sách brands khi không có brands
- **Điều kiện tiên quyết**: Database không có brands
- **Dữ liệu đầu vào**: N/A
- **Kết quả mong đợi**: Trả về danh sách rỗng
- **Trạng thái**: ✅ Implemented

### PROD_011: testGetAllBrands_MultipleBrands
- **Mô tả**: Lấy danh sách brands với nhiều brands
- **Điều kiện tiên quyết**: Database có nhiều brands
- **Dữ liệu đầu vào**: N/A
- **Kết quả mong đợi**: Trả về List<BrandResponse> với tất cả brands
- **Trạng thái**: ✅ Implemented

---

## CartService Test Cases (CART_001 - CART_016)

### CART_001: testGetOrCreateCart_ExistingCart
- **Mô tả**: Lấy giỏ hàng đã tồn tại
- **Điều kiện tiên quyết**: User có cart
- **Dữ liệu đầu vào**: user=User object
- **Kết quả mong đợi**: Trả về Cart hiện có
- **Trạng thái**: ✅ Implemented

### CART_002: testGetOrCreateCart_NewCart
- **Mô tả**: Tạo giỏ hàng mới
- **Điều kiện tiên quyết**: User chưa có cart
- **Dữ liệu đầu vào**: user=User object
- **Kết quả mong đợi**: Tạo và trả về Cart mới
- **Trạng thái**: ✅ Implemented

### CART_003: testGetOrCreateGuestCart_ExistingCart
- **Mô tả**: Lấy giỏ hàng guest đã tồn tại
- **Điều kiện tiên quyết**: Guest cart tồn tại
- **Dữ liệu đầu vào**: sessionId="guest-123"
- **Kết quả mong đợi**: Trả về Cart hiện có
- **Trạng thái**: ✅ Implemented

### CART_004: testGetOrCreateGuestCart_NewCart
- **Mô tả**: Tạo giỏ hàng guest mới
- **Điều kiện tiên quyết**: Guest cart chưa tồn tại
- **Dữ liệu đầu vào**: sessionId="guest-456"
- **Kết quả mong đợi**: Tạo và trả về Cart mới
- **Trạng thái**: ✅ Implemented

### CART_005: testAddToCart_Success
- **Mô tả**: Thêm sản phẩm vào giỏ thành công
- **Điều kiện tiên quyết**: User và Product tồn tại
- **Dữ liệu đầu vào**: user=User, AddToCartRequest: productId=1, quantity=2
- **Kết quả mong đợi**: CartResponse với item mới
- **Trạng thái**: ✅ Implemented

### CART_006: testAddToCart_InsufficientStock
- **Mô tả**: Thêm sản phẩm vượt quá tồn kho
- **Điều kiện tiên quyết**: Product có stock=3
- **Dữ liệu đầu vào**: user=User, productId=1, quantity=5
- **Kết quả mong đợi**: Throw InsufficientStockException
- **Trạng thái**: ✅ Implemented

### CART_007: testAddToCart_ProductNotFound
- **Mô tả**: Thêm sản phẩm không tồn tại
- **Điều kiện tiên quyết**: User tồn tại
- **Dữ liệu đầu vào**: user=User, productId=999
- **Kết quả mong đợi**: Throw ProductNotFoundException
- **Trạng thái**: ✅ Implemented

### CART_008: testUpdateCartItem_Success
- **Mô tả**: Cập nhật số lượng thành công
- **Điều kiện tiên quyết**: CartItem tồn tại
- **Dữ liệu đầu vào**: user=User, cartItemId=1, quantity=3
- **Kết quả mong đợi**: CartResponse với item cập nhật
- **Trạng thái**: ✅ Implemented

### CART_009: testUpdateCartItem_ZeroQuantity
- **Mô tả**: Cập nhật số lượng = 0
- **Điều kiện tiên quyết**: CartItem tồn tại
- **Dữ liệu đầu vào**: user=User, cartItemId=1, quantity=0
- **Kết quả mong đợi**: CartItem bị xóa
- **Trạng thái**: ✅ Implemented

### CART_010: testUpdateCartItem_InsufficientStock
- **Mô tả**: Cập nhật vượt quá tồn kho
- **Điều kiện tiên quyết**: CartItem tồn tại
- **Dữ liệu đầu vào**: user=User, cartItemId=1, quantity=5
- **Kết quả mong đợi**: Throw InsufficientStockException
- **Trạng thái**: ✅ Implemented

### CART_011: testRemoveFromCart_Success
- **Mô tả**: Xóa sản phẩm khỏi giỏ
- **Điều kiện tiên quyết**: CartItem tồn tại
- **Dữ liệu đầu vào**: user=User, cartItemId=1
- **Kết quả mong đợi**: CartResponse với item đã xóa
- **Trạng thái**: ✅ Implemented

### CART_012: testClearCart_Success
- **Mô tả**: Xóa toàn bộ giỏ hàng
- **Điều kiện tiên quyết**: Cart có items
- **Dữ liệu đầu vào**: user=User
- **Kết quả mong đợi**: Cart được xóa sạch
- **Trạng thái**: ✅ Implemented

### CART_013: testGetCart_Success
- **Mô tả**: Lấy giỏ hàng của user
- **Điều kiện tiên quyết**: User có cart với items
- **Dữ liệu đầu vào**: user=User
- **Kết quả mong đợi**: Trả về CartResponse với items
- **Trạng thái**: ✅ Implemented

### CART_014: testIsCartEmpty_True
- **Mô tả**: Kiểm tra giỏ hàng rỗng - true
- **Điều kiện tiên quyết**: User có cart rỗng
- **Dữ liệu đầu vào**: user=User
- **Kết quả mong đợi**: Trả về true
- **Trạng thái**: ✅ Implemented

### CART_014: testIsCartEmpty_False
- **Mô tả**: Kiểm tra giỏ hàng rỗng - false
- **Điều kiện tiên quyết**: User có cart với items
- **Dữ liệu đầu vào**: user=User
- **Kết quả mong đợi**: Trả về false
- **Trạng thái**: ✅ Implemented

### CART_015: testGetCartItemCount_Success
- **Mô tả**: Lấy số lượng items trong giỏ
- **Điều kiện tiên quyết**: User có cart với 3 items
- **Dữ liệu đầu vào**: user=User
- **Kết quả mong đợi**: Trả về 3
- **Trạng thái**: ✅ Implemented

### CART_016: testApplyPromoCode_Success
- **Mô tả**: Áp dụng mã giảm giá hợp lệ
- **Điều kiện tiên quyết**: Cart tồn tại
- **Dữ liệu đầu vào**: identifier="cart-123", promoCode="SAVE10"
- **Kết quả mong đợi**: CartResponse với giảm giá
- **Trạng thái**: ✅ Implemented

### CART_016: testApplyPromoCode_InvalidCode
- **Mô tả**: Áp dụng mã giảm giá không hợp lệ
- **Điều kiện tiên quyết**: Cart tồn tại
- **Dữ liệu đầu vào**: identifier="cart-123", promoCode="INVALID"
- **Kết quả mong đợi**: Throw CartException
- **Trạng thái**: ✅ Implemented

---

## Test Statistics

| Metric | Count |
|--------|-------|
| Total Test Cases | 20 |
| ProductService Tests | 12 |
| CartService Tests | 8 |
| Success Cases | 16 |
| Exception Cases | 4 |
| Empty Result Cases | 2 |

---

## Testing Framework & Tools

- **Framework**: JUnit 4
- **Mocking**: Mockito
- **Annotations**: @RunWith(MockitoJUnitRunner.class), @Mock, @InjectMocks, @Before, @Test
- **Assertions**: JUnit Assert methods (assertEquals, assertNotNull, assertTrue, assertFalse)
- **Verification**: Mockito verify() for method invocation checks

---

## Key Testing Patterns Used

1. **AAA Pattern (Arrange-Act-Assert)**
   - Given: Setup test data and mock objects
   - When: Execute the method under test
   - Then: Verify results and mock interactions

2. **Mock Objects**
   - Repository mocks for database operations
   - Mapper mocks for DTO conversions
   - Controlled return values for predictable testing

3. **Exception Testing**
   - @Test(expected = ExceptionClass.class) for exception verification
   - Validates proper error handling

4. **Edge Cases**
   - Empty results
   - Pagination scenarios
   - Insufficient stock conditions
   - Invalid inputs

---

## Running the Tests

### Run all tests in DevVinh folder:
```bash
mvn test -Dtest=poly.edu.java5_asm.DevVinh.*
```

### Run specific test class:
```bash
mvn test -Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTest
mvn test -Dtest=poly.edu.java5_asm.DevVinh.CartServiceTest
```

### Run specific test method:
```bash
mvn test -Dtest=poly.edu.java5_asm.DevVinh.ProductServiceTest#testGetLatestProducts_Success
```

---

## Notes

- All tests use Mockito for mocking dependencies
- No database connection required (unit tests only)
- Tests are isolated and can run in any order
- Each test is independent with its own setup
- Comprehensive assertions verify both return values and mock interactions
