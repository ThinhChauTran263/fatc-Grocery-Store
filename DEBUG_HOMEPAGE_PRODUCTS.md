# 🔍 Debug: Homepage Products Không Hiển Thị

## Vấn đề
Sau khi deploy commit 2384645, products vẫn không hiển thị trên homepage.

## Nguyên nhân có thể

### 1. ✅ Code đã đúng
- `index.html` có Thymeleaf loop: `th:each="product : ${featuredProducts}"`
- `HomeController` pass data: `model.addAttribute("featuredProducts", featuredProducts.getProducts())`
- `ProductListResponse` có getter `getProducts()`

### 2. ❌ Database không có featured products
Trên Render, products có thể có `is_featured = false` hoặc `null`

### 3. ❌ Cache chưa clear
Caffeine cache đang cache kết quả cũ

### 4. ❌ Service chưa restart
Render cần restart để load code mới

## Cách Fix

### Bước 1: Kiểm tra Database trên Render

1. Vào **Render Dashboard** → Chọn **PostgreSQL database**
2. Click **Connect** → Chọn **External Connection**
3. Copy connection string và connect bằng tool (DBeaver, pgAdmin)
4. Hoặc dùng **Shell** tab trong Render

Chạy SQL:
```sql
-- Kiểm tra có bao nhiêu featured products
SELECT COUNT(*) FROM products WHERE is_featured = true AND is_active = true;
```

**Nếu COUNT = 0**, chạy:
```sql
-- Set 5 products đầu tiên làm featured
UPDATE products 
SET is_featured = true 
WHERE id IN (1, 2, 3, 4, 5) AND is_active = true;
```

### Bước 2: Clear Cache và Restart

1. Vào **Render Dashboard** → Chọn **Web Service**
2. Click **Manual Deploy** → **Clear build cache & deploy**
3. Đợi deploy xong
4. Click **Restart** (nút 3 chấm bên phải)

### Bước 3: Test

Mở homepage và check:
```
https://your-app.onrender.com/
```

View page source (Ctrl+U) và search "product-card" - phải thấy HTML của products.

### Bước 4: Nếu vẫn không hiển thị

Check logs trong Render:
1. Click **Logs** tab
2. Search "Featured products" hoặc "getFeaturedProducts"
3. Xem có error gì không

## Script SQL Đầy Đủ

File: `CHECK_FEATURED_PRODUCTS.sql`

```sql
-- 1. Kiểm tra
SELECT COUNT(*) as featured_count 
FROM products 
WHERE is_featured = true AND is_active = true;

-- 2. Xem chi tiết
SELECT id, name, is_featured, is_active, price, image_url
FROM products 
WHERE is_featured = true AND is_active = true
LIMIT 10;

-- 3. Fix nếu cần
UPDATE products 
SET is_featured = true 
WHERE id IN (1, 2, 3, 4, 5) AND is_active = true;

-- 4. Verify
SELECT COUNT(*) FROM products WHERE is_featured = true AND is_active = true;
```

## Lưu ý

- **Thymeleaf cache**: Đã tắt trong dev (`cache: false`), nhưng production mặc định bật
- **Caffeine cache**: Featured products được cache với key `'page_0_size_8'`
- **Restart bắt buộc**: Sau khi update database, phải restart service để clear cache

## Test Local

Nếu muốn test local với PostgreSQL:

```bash
# Start PostgreSQL
docker-compose up -d postgres

# Run app với postgres profile
java -jar target/java5_asm-0.0.1-SNAPSHOT.jar --spring.profiles.active=postgres

# Hoặc set env var
set SPRING_PROFILES_ACTIVE=postgres
mvnw.cmd spring-boot:run
```

Mở http://localhost:8080 và check.
