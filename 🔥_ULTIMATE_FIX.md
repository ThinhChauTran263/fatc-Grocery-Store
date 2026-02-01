# 🔥 ULTIMATE FIX - PostgreSQL Driver Issue

## 🎯 VẤN ĐỀ GỐC RỄ

Cả MariaDB và PostgreSQL driver đều có trong classpath → HikariCP bị confused!

---

## ✅ GIẢI PHÁP CUỐI CÙNG

### Thêm biến này vào Render Environment:

```
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
```

**Lưu ý:** Tên biến là `DRIVER_CLASS_NAME` (không phải `DRIVER`)

---

## 📋 FULL ENVIRONMENT VARIABLES (11 biến)

```env
# Database (4 biến - thêm DRIVER_CLASS_NAME)
SPRING_DATASOURCE_URL=jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Cloudinary (4 biến)
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=[ĐIỀN VÀO]
CLOUDINARY_API_KEY=[ĐIỀN VÀO]
CLOUDINARY_API_SECRET=[ĐIỀN VÀO]

# JWT (2 biến)
JWT_SECRET=fatc-grocery-store-super-secret-key-2024-minimum-32-chars
JWT_EXPIRATION=86400000

# Application (2 biến)
SPRING_PROFILES_ACTIVE=prod
APP_BASE_URL=https://fatc-grocery-store.onrender.com
```

**Tổng: 12 biến**

---

## 🚀 CÁCH FIX (2 phút)

### Bước 1: Xóa biến cũ (nếu có)
Xóa biến `SPRING_DATASOURCE_DRIVER` (không có `_CLASS_NAME`)

### Bước 2: Thêm biến mới
```
Key: SPRING_DATASOURCE_DRIVER_CLASS_NAME
Value: org.postgresql.Driver
```

### Bước 3: Thêm biến dialect (double check)
```
Key: SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT
Value: org.hibernate.dialect.PostgreSQLDialect
```

### Bước 4: Save và Redeploy
- Click **Save Changes**
- Đợi redeploy (5 phút)
- Check logs

---

## 🎯 TẠI SAO PHẢI LÀM VẬY?

### Vấn đề:
```
pom.xml có:
- org.mariadb.jdbc.Driver (cho local)
- org.postgresql.Driver (cho production)
```

HikariCP auto-detect driver từ URL, nhưng khi có 2 drivers → confused!

### Giải pháp:
Force chỉ định driver bằng `driver-class-name` → HikariCP biết chính xác dùng driver nào!

---

## 📝 SO SÁNH

### Trước (Sai):
```
SPRING_DATASOURCE_DRIVER=org.postgresql.Driver  ❌
```
→ Spring Boot không hiểu biến này!

### Sau (Đúng):
```
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver  ✅
```
→ Spring Boot hiểu và force dùng PostgreSQL driver!

---

## 🔍 VERIFY

Sau khi fix, logs sẽ hiển thị:

```
HikariPool-1 - configuration:
...
driverClassName...................org.postgresql.Driver  ✅
jdbcUrl...........................jdbc:postgresql://...  ✅
...
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Using dialect: org.hibernate.dialect.PostgreSQLDialect  ✅
```

---

## ⚠️ NẾU VẪN KHÔNG ĐƯỢC

### Last Resort: Exclude MariaDB driver trong production

Thêm biến:
```
SPRING_AUTOCONFIGURE_EXCLUDE=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

Rồi thêm lại:
```
SPRING_DATASOURCE_TYPE=com.zaxxer.hikari.HikariDataSource
```

**Nhưng thử cách trên trước!**

---

## 🎉 KẾT QUẢ

Sau khi thêm `SPRING_DATASOURCE_DRIVER_CLASS_NAME`:
- ✅ HikariCP biết chính xác dùng PostgreSQL driver
- ✅ Không còn conflict với MariaDB driver
- ✅ App start thành công!

**Thử ngay!** 🚀
