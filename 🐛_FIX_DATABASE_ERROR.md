# 🐛 FIX DATABASE CONNECTION ERROR

## ❌ Lỗi gặp phải

```
UnsatisfiedDependencyException: Error creating bean with name 'customUserDetailsService'
Cannot resolve reference to bean 'jpaSharedEM_entityManagerFactory'
```

**Nguyên nhân:** App không thể kết nối database!

---

## ✅ GIẢI PHÁP NHANH

### Bước 1: Kiểm tra Environment Variables

Vào Render Dashboard → Service → **Environment**, đảm bảo có **ĐỦ 3 BIẾN**:

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_DRIVER
SPRING_JPA_DIALECT
```

**⚠️ QUAN TRỌNG:** Phải có cả 3 biến này!

---

### Bước 2: Kiểm tra Database URL

**Sai:** ❌
```
SPRING_DATASOURCE_URL = postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm
```

**Đúng:** ✅
```
SPRING_DATASOURCE_URL = postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com/java5_asm
```

**Lưu ý:** Cần thêm `.singapore-postgres.render.com` vào hostname!

---

### Bước 3: Lấy Connection String đúng

1. Vào Render Dashboard
2. Click vào database **fatc-grocery-db**
3. Tab **Info**
4. Copy **Internal Database URL** (không phải External!)

**Format đúng:**
```
postgresql://[user]:[password]@[hostname].singapore-postgres.render.com/[database]
```

---

### Bước 4: Cập nhật Environment Variables

#### Option A: Sửa từng biến

1. Vào Service → **Environment**
2. Tìm `SPRING_DATASOURCE_URL`
3. Click **Edit**
4. Paste connection string mới (có `.singapore-postgres.render.com`)
5. Click **Save Changes**

#### Option B: Xóa và thêm lại

1. Xóa 3 biến cũ:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_DRIVER`
   - `SPRING_JPA_DIALECT`

2. Click **"Add from .env"**

3. Paste nội dung này (thay `[CORRECT_URL]` bằng URL đúng):

```env
SPRING_DATASOURCE_URL=[CORRECT_URL]
SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
SPRING_JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

4. Click **"Add Variables"**

---

### Bước 5: Redeploy

1. Click **"Manual Deploy"** → **"Deploy latest commit"**
2. Hoặc đợi auto-deploy
3. Xem logs để verify

---

## 🔍 KIỂM TRA CHI TIẾT

### 1. Verify Database đang chạy

1. Vào Render Dashboard
2. Click database **fatc-grocery-db**
3. Kiểm tra **Status: Available** ✅

Nếu status không phải "Available":
- Đợi vài phút
- Database có thể đang khởi động

### 2. Verify Connection String Format

**Kiểm tra các phần:**

```
postgresql://[user]:[password]@[hostname]/[database]
          ↓        ↓           ↓           ↓
     java5_user  password   hostname   java5_asm
```

**Hostname phải có đuôi:**
- `.singapore-postgres.render.com` (Singapore region)
- `.oregon-postgres.render.com` (Oregon region)
- Hoặc region khác tùy bạn chọn

### 3. Test Connection (Optional)

Dùng tool như **DBeaver** hoặc **pgAdmin**:

1. Host: `dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com`
2. Port: `5432`
3. Database: `java5_asm`
4. User: `java5_user`
5. Password: `X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D`

Nếu connect được → URL đúng ✅

---

## 📋 CHECKLIST FIX

- [ ] Database status = "Available"
- [ ] `SPRING_DATASOURCE_URL` có đuôi `.singapore-postgres.render.com`
- [ ] `SPRING_DATASOURCE_DRIVER` = `org.postgresql.Driver`
- [ ] `SPRING_JPA_DIALECT` = `org.hibernate.dialect.PostgreSQLDialect`
- [ ] Đã save changes
- [ ] Đã redeploy
- [ ] Xem logs không còn lỗi

---

## 🎯 CONNECTION STRING MẪU

### Internal Database URL (Dùng cái này!)

```
postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com/java5_asm
```

### External Database URL (KHÔNG dùng!)

```
postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com:5432/java5_asm
```

**Lưu ý:** Internal URL không có port `:5432`

---

## 🔧 CÁCH LẤY URL ĐÚNG

### Từ Render Dashboard:

1. Vào: https://dashboard.render.com
2. Click database: **fatc-grocery-db**
3. Tab: **Info** hoặc **Connect**
4. Tìm: **Internal Database URL**
5. Click **Copy**
6. Paste vào `SPRING_DATASOURCE_URL`

**Screenshot vị trí:**
```
Dashboard → Databases → fatc-grocery-db → Info
                                           ↓
                              Internal Database URL
                                    [Copy button]
```

---

## 🐛 CÁC LỖI THƯỜNG GẶP

### Lỗi 1: Thiếu hostname suffix
```
❌ dpg-d5vlucngi27c73cej9pg-a
✅ dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com
```

### Lỗi 2: Dùng External URL thay vì Internal
```
❌ External: có port :5432
✅ Internal: không có port
```

### Lỗi 3: Thiếu biến DRIVER hoặc DIALECT
```
Phải có đủ 3 biến:
✅ SPRING_DATASOURCE_URL
✅ SPRING_DATASOURCE_DRIVER
✅ SPRING_JPA_DIALECT
```

### Lỗi 4: Database chưa sẵn sàng
```
Kiểm tra: Database Status = "Available"
Nếu "Creating" → Đợi vài phút
```

---

## 📝 TEMPLATE ĐẦY ĐỦ

Copy và điền vào Render:

```env
# Database (BẮT BUỘC - 3 biến)
SPRING_DATASOURCE_URL=postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com/java5_asm
SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
SPRING_JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Cloudinary (BẮT BUỘC - 4 biến)
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=[ĐIỀN VÀO]
CLOUDINARY_API_KEY=[ĐIỀN VÀO]
CLOUDINARY_API_SECRET=[ĐIỀN VÀO]

# JWT (BẮT BUỘC - 2 biến)
JWT_SECRET=fatc-grocery-store-super-secret-key-2024-minimum-32-chars
JWT_EXPIRATION=86400000

# Application (BẮT BUỘC - 2 biến)
SPRING_PROFILES_ACTIVE=prod
APP_BASE_URL=https://fatc-grocery-store.onrender.com
```

**Tổng: 11 biến bắt buộc**

---

## ✅ SAU KHI FIX

### Logs thành công sẽ hiển thị:

```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Initialized JPA EntityManagerFactory
Started Java5AsmApplication in X.XXX seconds
```

### Nếu vẫn lỗi:

1. Xem logs chi tiết
2. Tìm dòng có "database" hoặc "connection"
3. Copy lỗi và check lại URL

---

## 🚀 QUICK FIX (1 phút)

```bash
1. Vào database → Copy Internal Database URL
2. Vào service → Environment
3. Edit SPRING_DATASOURCE_URL
4. Paste URL mới (có .singapore-postgres.render.com)
5. Save Changes
6. Đợi redeploy
7. Check logs → Success! ✅
```

---

## 📞 VẪN KHÔNG ĐƯỢC?

### Kiểm tra lại:

1. **Database URL có đúng format?**
   ```
   postgresql://user:pass@hostname.region.render.com/db
   ```

2. **Có đủ 3 biến database?**
   - SPRING_DATASOURCE_URL ✅
   - SPRING_DATASOURCE_DRIVER ✅
   - SPRING_JPA_DIALECT ✅

3. **Database status = Available?**
   - Vào database dashboard check

4. **Password có đúng?**
   - Copy lại từ database dashboard

5. **Region có đúng?**
   - Singapore: `.singapore-postgres.render.com`
   - Oregon: `.oregon-postgres.render.com`

---

## 🎉 DONE!

Sau khi fix, app sẽ start thành công và bạn có thể truy cập:

**https://fatc-grocery-store.onrender.com**

**Good luck! 🚀**
