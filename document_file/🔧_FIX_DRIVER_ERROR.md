# 🔧 FIX DRIVER ERROR

## ❌ Lỗi gặp phải

```
Driver org.mariadb.jdbc.Driver claims to not accept jdbcUrl, postgresql://...
```

**Nguyên nhân:** MariaDB driver đang cố kết nối PostgreSQL URL!

---

## ✅ ĐÃ FIX!

Tôi đã cập nhật `application.properties` để dùng environment variables:

### Trước (Sai): ❌
```properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
```
→ Hardcoded MariaDB, không thể dùng PostgreSQL!

### Sau (Đúng): ✅
```properties
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER:org.mariadb.jdbc.Driver}
spring.jpa.properties.hibernate.dialect=${SPRING_JPA_DIALECT:org.hibernate.dialect.MariaDBDialect}
```
→ Dùng environment variables, support cả 2!

---

## 🚀 BẠN CẦN LÀM GÌ?

### Bước 1: Pull code mới
```bash
git pull origin ThinhDev
```

### Bước 2: Redeploy trên Render
1. Vào Render Dashboard
2. Service → **Manual Deploy**
3. Click **"Deploy latest commit"**
4. Đợi 5 phút

### Bước 3: Verify
Xem logs, phải thấy:
```
HikariPool-1 - Starting...
Using dialect: org.hibernate.dialect.PostgreSQLDialect
Started Java5AsmApplication
```

---

## 📋 Environment Variables cần có

Đảm bảo có **ĐỦ 3 BIẾN** trong Render:

```
SPRING_DATASOURCE_URL = postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com/java5_asm

SPRING_DATASOURCE_DRIVER = org.postgresql.Driver

SPRING_JPA_DIALECT = org.hibernate.dialect.PostgreSQLDialect
```

**⚠️ QUAN TRỌNG:** 
- URL phải có `.singapore-postgres.render.com`
- Driver phải là `org.postgresql.Driver` (không phải MariaDB!)
- Dialect phải là `PostgreSQLDialect` (không phải MariaDB!)

---

## 🎯 Tại sao lỗi này xảy ra?

### Vấn đề:
Spring Boot load config theo thứ tự:
1. `application.properties` (load trước)
2. `application.yml` (load sau)

Nếu `application.properties` có hardcoded values → override `application.yml`!

### Giải pháp:
Dùng environment variables trong cả 2 file:
```properties
# application.properties
spring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER:org.mariadb.jdbc.Driver}
```

```yaml
# application.yml
spring:
  datasource:
    driver-class-name: ${SPRING_DATASOURCE_DRIVER:org.mariadb.jdbc.Driver}
```

→ Environment variables sẽ override cả 2!

---

## ✅ Đã fix trong code

File đã cập nhật:
- ✅ `src/main/resources/application.properties`
- ✅ `src/main/resources/application.yml`

Cả 2 file đều dùng environment variables rồi!

---

## 🔍 Verify local

Test trên máy local:

### MariaDB (default):
```bash
# Không set env variables
./mvnw spring-boot:run
# → Dùng MariaDB
```

### PostgreSQL (production):
```bash
# Set env variables
export SPRING_DATASOURCE_URL=postgresql://...
export SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
export SPRING_JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect

./mvnw spring-boot:run
# → Dùng PostgreSQL
```

---

## 🎉 KẾT QUẢ

Sau khi fix:
- ✅ Local: Dùng MariaDB (default)
- ✅ Production: Dùng PostgreSQL (từ env vars)
- ✅ Không cần sửa code khi deploy!

---

## 📞 Nếu vẫn lỗi

### Kiểm tra logs:
```
Using dialect: org.hibernate.dialect.PostgreSQLDialect ✅
```

Nếu thấy:
```
Using dialect: org.hibernate.dialect.MariaDBDialect ❌
```
→ Environment variables chưa được set đúng!

### Fix:
1. Vào Render → Environment
2. Verify 3 biến database
3. Redeploy

---

**Code đã fix và push lên GitHub! Pull về và redeploy là xong! 🚀**
