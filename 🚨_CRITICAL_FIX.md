# 🚨 CRITICAL FIX - PostgreSQL Driver Issue

## ❌ Lỗi

```
Driver org.postgresql.Driver claims to not accept jdbcUrl
```

## 🔍 NGUYÊN NHÂN

PostgreSQL driver có thể bị conflict hoặc không load đúng cách.

---

## ✅ GIẢI PHÁP 1: Thêm biến JDBC URL Parameters

Thêm parameters vào URL để force PostgreSQL driver:

### Trên Render, sửa `SPRING_DATASOURCE_URL` thành:

```
jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm?sslmode=require&currentSchema=public
```

**Thêm:** `?sslmode=require&currentSchema=public`

---

## ✅ GIẢI PHÁP 2: Thêm biến HikariCP

Thêm các biến này vào Render Environment:

```
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE = 5
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE = 2
```

---

## ✅ GIẢI PHÁP 3: Disable JPA Auto-config (Tạm thời)

Thêm biến này để test:

```
SPRING_JPA_HIBERNATE_DDL_AUTO = validate
```

---

## 🎯 KHUYẾN NGHỊ: Làm theo thứ tự

### Bước 1: Thử Giải pháp 1
1. Sửa `SPRING_DATASOURCE_URL` thêm parameters
2. Save Changes
3. Đợi redeploy
4. Check logs

### Bước 2: Nếu vẫn lỗi, thêm Giải pháp 2
1. Thêm 2 biến HikariCP
2. Save Changes
3. Đợi redeploy

### Bước 3: Nếu vẫn lỗi, thử Giải pháp 3
1. Thêm biến DDL_AUTO
2. Save Changes
3. Đợi redeploy

---

## 📋 FULL ENVIRONMENT VARIABLES

Đảm bảo có đủ các biến này:

```env
# Database (BẮT BUỘC)
SPRING_DATASOURCE_URL=jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm?sslmode=require&currentSchema=public
SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
SPRING_JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# HikariCP (Khuyến nghị)
SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=5
SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=2

# JPA (Tạm thời)
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# Cloudinary (BẮT BUỘC)
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=[ĐIỀN VÀO]
CLOUDINARY_API_KEY=[ĐIỀN VÀO]
CLOUDINARY_API_SECRET=[ĐIỀN VÀO]

# JWT (BẮT BUỘC)
JWT_SECRET=fatc-grocery-store-super-secret-key-2024-minimum-32-chars
JWT_EXPIRATION=86400000

# Application (BẮT BUỘC)
SPRING_PROFILES_ACTIVE=prod
APP_BASE_URL=https://fatc-grocery-store.onrender.com
```

---

## 🔧 NẾU VẪN KHÔNG ĐƯỢC

### Kiểm tra PostgreSQL driver có trong build không:

Xem logs build, tìm dòng:
```
Downloaded from central: .../postgresql-XX.X.X.jar
```

Nếu KHÔNG thấy → PostgreSQL driver không được download!

### Giải pháp: Force rebuild

1. Vào Render Dashboard
2. Service → Settings
3. Scroll xuống **Danger Zone**
4. Click **"Clear build cache & deploy"**
5. Đợi rebuild từ đầu

---

## 📞 URL ĐÚNG CUỐI CÙNG

```
jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm?sslmode=require&currentSchema=public
```

**Thử URL này trước!** ✅
