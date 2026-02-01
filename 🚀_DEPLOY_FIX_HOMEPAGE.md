# 🚀 Deploy Fix: Homepage Products

## Vấn đề đã fix
- ✅ Thêm explicit `@Query` cho featured products
- ✅ Database đã có 5 featured products
- ✅ Tất cả data types đều đúng (text/varchar)

## Commit mới nhất
```
aec7014 - Fix: Remove LOWER() function from search query to fix PostgreSQL bytea error
```

## Các bước deploy trên Render

### Bước 1: Trigger Deploy
1. Vào **Render Dashboard**: https://dashboard.render.com
2. Chọn **Web Service** của bạn
3. Click **Manual Deploy** → **Deploy latest commit**
4. Đợi build xong (khoảng 3-5 phút)

### Bước 2: Clear Cache & Restart
Sau khi deploy xong:

1. Click nút **3 chấm** (⋮) bên phải
2. Chọn **Restart Service**
3. Đợi service restart (khoảng 30 giây)

### Bước 3: Verify
Mở homepage:
```
https://your-app.onrender.com/
```

Kiểm tra:
- ✅ Có 5 products hiển thị trong "Featured Products"
- ✅ Mỗi product có: ảnh, tên, giá
- ✅ Không có error trong browser console (F12)

### Bước 4: Check Logs (nếu vẫn lỗi)
1. Click tab **Logs** trong Render
2. Search "Featured products" hoặc "getFeaturedProducts"
3. Xem có error gì không

## Nếu vẫn không hiển thị

### Option 1: Clear Build Cache
1. Click **Manual Deploy**
2. Chọn **Clear build cache & deploy**
3. Đợi build lại từ đầu

### Option 2: Check Environment Variables
Đảm bảo có các biến:
```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=postgresql://...
USE_CLOUDINARY=true
```

### Option 3: Check Database
Chạy lại trong Render PostgreSQL Shell:
```sql
SELECT COUNT(*) FROM products 
WHERE is_featured = true AND is_active = true;
```

Phải trả về >= 5

## Test Local (Optional)

Nếu muốn test local trước:

```bash
# Pull code mới
git pull origin ThinhDev

# Build
mvnw.cmd clean package -DskipTests

# Run với PostgreSQL
set SPRING_PROFILES_ACTIVE=postgres
java -jar target/java5_asm-0.0.1-SNAPSHOT.jar
```

Mở http://localhost:8080 và check.

## Lưu ý

- **Caffeine cache**: Featured products được cache 1 giờ
- **Restart bắt buộc**: Phải restart để clear cache
- **Database OK**: Đã verify tất cả data types đúng
- **Query đơn giản**: Không dùng LIKE, chỉ WHERE boolean

## Kết quả mong đợi

Homepage sẽ hiển thị:
1. Coffee Beans - Espresso Arabica - 1,175,000 VND
2. Lavazza Italian Espresso - 1,325,000 VND
3. Starbucks Pike Place Roast - 800,000 VND
4. Trung Nguyen Creative 3 - 1,125,000 VND
5. Nescafe Gold Instant - 600,000 VND

Tất cả có ảnh từ Unsplash.
