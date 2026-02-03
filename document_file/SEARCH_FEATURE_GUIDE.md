# Hướng dẫn Chức năng Tìm kiếm Sản phẩm

## Tổng quan
Đã thêm chức năng tìm kiếm sản phẩm đầy đủ vào navbar và trang category.

## Các thay đổi đã thực hiện

### 1. Frontend - Navbar (Header)

#### File: `src/main/resources/templates/shared/fragments/header.html`
- Thêm thanh search hiển thị trên desktop (màn hình lớn)
- Ẩn trên mobile (sử dụng search trong navbar khi mở menu)

#### File: `src/main/resources/templates/shared/fragments/header/navbar.html`
- Thêm thanh search trong navbar cho mobile
- Ẩn các menu không dùng được (Departments, Grocery, Beauty)
- Thêm link "Tất cả sản phẩm" ở đầu navbar
- Đổi icon Caffeine Calculator

#### File: `src/main/resources/scss/layout/_header.scss`
- Thêm CSS cho `.header-search` (desktop search bar)
- Thêm CSS cho `.navbar__search` (mobile search bar)
- Responsive design cho các kích thước màn hình

### 2. Backend - Controller

#### File: `src/main/java/poly/edu/java5_asm/common/controller/HomeController.java`

**Thêm parameters cho method `category()`:**
```java
@GetMapping("/category")
public String category(
    Model model, 
    @AuthenticationPrincipal CustomUserDetails userDetails,
    @RequestParam(required = false) String keyword,           // Từ khóa tìm kiếm
    @RequestParam(required = false) Long categoryId,          // Lọc theo danh mục
    @RequestParam(required = false) Long brandId,             // Lọc theo thương hiệu
    @RequestParam(required = false) BigDecimal minPrice,      // Giá tối thiểu
    @RequestParam(required = false) BigDecimal maxPrice,      // Giá tối đa
    @RequestParam(defaultValue = "0") int page,               // Trang hiện tại
    @RequestParam(defaultValue = "12") int size,              // Số sản phẩm/trang
    @RequestParam(defaultValue = "createdAt") String sortBy,  // Sắp xếp theo
    @RequestParam(defaultValue = "DESC") String sortDirection // Hướng sắp xếp
)
```

**Logic xử lý:**
- Build `ProductSearchRequest` từ các parameters
- Gọi `productService.searchAndFilterProducts(searchRequest)`
- Trả về kết quả tìm kiếm với pagination

### 3. Frontend - Category Page

#### File: `src/main/resources/templates/module/product/category.html`

**Cập nhật hiển thị:**
- Filter bar hiển thị số kết quả tìm kiếm
- Empty state thông minh:
  - Nếu có keyword: "Không tìm thấy sản phẩm với từ khóa..."
  - Nếu không có keyword: "Chưa có sản phẩm"
- Pagination giữ lại tất cả filter parameters khi chuyển trang

## Cách sử dụng

### 1. Tìm kiếm cơ bản
```
URL: /category?keyword=coffee
```
Tìm tất cả sản phẩm có chứa từ "coffee" trong tên hoặc mô tả.

### 2. Tìm kiếm với filter
```
URL: /category?keyword=coffee&categoryId=1&minPrice=100000&maxPrice=500000
```
Tìm sản phẩm "coffee" trong category ID 1, giá từ 100k-500k.

### 3. Tìm kiếm với pagination
```
URL: /category?keyword=coffee&page=1&size=12
```
Tìm sản phẩm "coffee", trang 2, mỗi trang 12 sản phẩm.

### 4. Tìm kiếm với sắp xếp
```
URL: /category?keyword=coffee&sortBy=price&sortDirection=ASC
```
Tìm sản phẩm "coffee", sắp xếp theo giá tăng dần.

## Các tính năng

### ✅ Đã hoàn thành
- [x] Thanh search trên navbar (desktop)
- [x] Thanh search trong navbar mobile
- [x] Tìm kiếm theo keyword
- [x] Lọc theo category
- [x] Lọc theo brand
- [x] Lọc theo khoảng giá
- [x] Pagination giữ filter
- [x] Hiển thị số kết quả
- [x] Empty state thông minh
- [x] Responsive design

### 🔄 Service Layer (Đã có sẵn)
- ProductService.searchAndFilterProducts()
- ProductRepository với JPA Specification
- Full-text search trong database

## Testing

### Test tìm kiếm cơ bản:
1. Mở trang chủ
2. Nhập "coffee" vào thanh search
3. Nhấn Enter hoặc click nút search
4. Kiểm tra kết quả hiển thị

### Test pagination:
1. Tìm kiếm một từ khóa có nhiều kết quả
2. Click vào các số trang
3. Kiểm tra URL có giữ keyword không

### Test empty state:
1. Tìm kiếm từ khóa không tồn tại: "xyz123abc"
2. Kiểm tra hiển thị thông báo "Không tìm thấy sản phẩm"

## Lưu ý kỹ thuật

### Performance
- Service layer sử dụng JPA Specification cho query động
- Pagination giúp giảm tải database
- Index trên các cột tìm kiếm (name, description)

### Security
- Tất cả parameters đều validated
- SQL injection được prevent bởi JPA
- XSS được prevent bởi Thymeleaf escaping

### UX
- Search bar luôn hiển thị trên desktop
- Mobile có search trong navbar
- Giữ lại keyword sau khi search
- Pagination giữ tất cả filters
- Empty state rõ ràng và hữu ích

## Mở rộng trong tương lai

### Có thể thêm:
- [ ] Autocomplete suggestions
- [ ] Search history
- [ ] Popular searches
- [ ] Advanced filters (rating, stock status)
- [ ] Sort by relevance
- [ ] Faceted search
- [ ] Search analytics

## API Endpoints

### GET /category
**Parameters:**
- `keyword` (optional): Từ khóa tìm kiếm
- `categoryId` (optional): ID danh mục
- `brandId` (optional): ID thương hiệu
- `minPrice` (optional): Giá tối thiểu
- `maxPrice` (optional): Giá tối đa
- `page` (default: 0): Trang hiện tại
- `size` (default: 12): Số sản phẩm/trang
- `sortBy` (default: "createdAt"): Trường sắp xếp
- `sortDirection` (default: "DESC"): Hướng sắp xếp

**Response:**
- HTML page với danh sách sản phẩm
- Model attributes:
  - `products`: List<ProductResponse>
  - `totalPages`: Integer
  - `currentPage`: Integer
  - `keyword`: String
  - `selectedCategoryId`: Long
  - `selectedBrandId`: Long
  - `minPrice`: BigDecimal
  - `maxPrice`: BigDecimal

## Troubleshooting

### Search không hoạt động?
1. Kiểm tra ProductService.searchAndFilterProducts() đã implement chưa
2. Kiểm tra ProductRepository có method search chưa
3. Xem log để debug query

### Pagination mất keyword?
1. Kiểm tra pagination links có include keyword parameter không
2. Xem file category.html, phần pagination

### CSS không áp dụng?
1. Compile SCSS: `npm run sass` trong thư mục scss
2. Clear browser cache
3. Hard refresh (Ctrl+F5)

## Kết luận

Chức năng tìm kiếm đã được tích hợp hoàn chỉnh với:
- UI/UX tốt trên cả desktop và mobile
- Backend xử lý đầy đủ các filter
- Pagination và sorting
- Empty states và error handling
- Responsive design

Người dùng có thể dễ dàng tìm kiếm sản phẩm từ navbar hoặc trang category với nhiều tùy chọn lọc và sắp xếp.
