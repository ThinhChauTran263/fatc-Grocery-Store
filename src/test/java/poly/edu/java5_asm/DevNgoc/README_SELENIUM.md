# Selenium Test - Wishlist (Thiên Ngọc)

## Test Cases
- **WISH_001**: testAddToWishlist_Success - Thêm sản phẩm vào wishlist
- **WISH_003**: testRemoveFromWishlist_Success - Xóa sản phẩm khỏi wishlist

## Yêu cầu

### 1. ChromeDriver
- Tải ChromeDriver tương ứng với phiên bản Chrome: https://chromedriver.chromium.org/
- Thêm ChromeDriver vào PATH hoặc đặt trong thư mục project

### 2. Application đang chạy
```bash
# Chạy ứng dụng trước khi test
mvnw.cmd spring-boot:run

# Hoặc
mvnw spring-boot:run
```

Application phải chạy ở: `http://localhost:8080`

### 3. Database
- Đảm bảo database đã được setup với dữ liệu test
- User test: `admin` / `password123` phải tồn tại trong database

## Chạy Test

### Chạy tất cả Selenium tests
```bash
mvn test -Dtest=SeleniumWishlistTest
```

### Chạy từng test case riêng lẻ
```bash
# Test thêm vào wishlist
mvn test -Dtest=SeleniumWishlistTest#testAddToWishlist_Success

# Test xóa khỏi wishlist
mvn test -Dtest=SeleniumWishlistTest#testRemoveFromWishlist_Success
```

## Lưu ý

1. **Headless Mode**: Mặc định test chạy với browser hiển thị. Để chạy headless, uncomment dòng sau trong code:
   ```java
   // options.addArguments("--headless");
   ```

2. **Timeout**: Test có timeout 10 giây cho mỗi thao tác. Nếu mạng chậm, có thể tăng timeout:
   ```java
   wait = new WebDriverWait(driver, Duration.ofSeconds(20));
   ```

3. **Test Data**: Test sẽ tự động thêm sản phẩm vào wishlist nếu wishlist rỗng khi test remove.

4. **Cleanup**: Test không tự động cleanup dữ liệu. Wishlist sẽ giữ nguyên sau khi test.

## Kết quả mong đợi

### WISH_001 - testAddToWishlist_Success
```
✅ Login successful
✅ Products page loaded
✓ Found product: [Product Name]
✓ Clicked wishlist button
✓ Button is now active (red heart)
✓ Wishlist count: 0 → 1
✓ Found 1 items in wishlist
✅ TEST PASSED
```

### WISH_003 - testRemoveFromWishlist_Success
```
✅ Login successful
✅ Wishlist has X items
✓ Removing product: [Product Name]
✓ Clicked remove button
✓ Items count: X → X-1
✓ Wishlist count: X → X-1
✅ TEST PASSED
```

## Troubleshooting

### ChromeDriver not found
```
Error: ChromeDriver not found
Solution: Tải và cài đặt ChromeDriver từ https://chromedriver.chromium.org/
```

### Application not running
```
Error: Connection refused
Solution: Chạy application trước: mvnw.cmd spring-boot:run
```

### Login failed
```
Error: Login failed - still on sign-in page
Solution: Kiểm tra username/password trong database
```

### Element not found
```
Error: NoSuchElementException
Solution: Đợi page load hoàn toàn hoặc tăng timeout
```
