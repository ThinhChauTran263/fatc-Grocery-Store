# 🐘 Chạy Project với PostgreSQL Local

## Mục đích
Test project với PostgreSQL giống như trên Render, không cần đợi deploy lâu.

## ✅ Bước 1: Start PostgreSQL Container (ĐÃ XONG)
```bash
docker-compose up -d postgres
```

PostgreSQL đang chạy trên port **5433** ✅
- Container: `coffee_shop_postgres`
- Status: Up and healthy
- Data: 10 products, 9 categories, 5 brands

## 🚀 Bước 2: Chạy Spring Boot với Profile PostgreSQL

### ⭐ Cách 1: Từ IntelliJ IDEA (KHUYẾN NGHỊ)
1. Mở **Run/Debug Configurations** (góc trên bên phải)
2. Chọn configuration của project (Java5AsmApplication)
3. Trong **Environment variables**, thêm:
   ```
   SPRING_PROFILES_ACTIVE=postgres
   ```
4. Click **Apply** và **Run** (Shift+F10)

### Cách 2: Từ Command Line
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Cách 3: Từ Maven (Windows)
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Bước 3: Kiểm tra
1. Mở browser: http://localhost:8080
2. Kiểm tra:
   - Trang chủ có hiển thị sản phẩm không?
   - Categories có hiển thị không?
   - Admin form có dropdown categories/brands không?

## Database Info
- **Host**: localhost
- **Port**: 5433
- **Database**: java5_asm
- **Username**: java5_user
- **Password**: java5_password

## Kết nối Database (Optional)
Dùng DBeaver, pgAdmin, hoặc tool khác:
```
jdbc:postgresql://localhost:5433/java5_asm
```

## Dừng PostgreSQL
```bash
docker-compose down postgres
```

## Xóa dữ liệu và reset
```bash
docker-compose down postgres
docker volume rm fatc-grocery-store_postgres_data
docker-compose up -d postgres
```

## Lưu ý
- PostgreSQL container tự động import schema và data từ:
  - `mariadb_init/postgresql-schema.sql`
  - `mariadb_init/postgresql-data.sql`
- Data mẫu bao gồm:
  - 4 users (admin/admin123)
  - 9 categories
  - 5 brands
  - 10 products
