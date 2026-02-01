# ⚡ COPY & PASTE VÀO RENDER

## 🎯 HƯỚNG DẪN

1. Vào Render Dashboard → Service → **Environment**
2. **XÓA** biến cũ: `SPRING_DATASOURCE_DRIVER` (nếu có)
3. Click **"Add from .env"**
4. Copy **TOÀN BỘ** phần dưới đây
5. Paste vào ô text
6. Click **"Add Variables"**
7. Done! ✅

---

## 📋 COPY TOÀN BỘ NÀY (12 biến)

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=dnzarfrkx
CLOUDINARY_API_KEY=851223995676784
CLOUDINARY_API_SECRET=qXW2xbHK99ofUcp-0Jx-L0mL_qQ
JWT_SECRET=fatc-grocery-store-super-secret-key-2024-minimum-32-chars
JWT_EXPIRATION=86400000
SPRING_PROFILES_ACTIVE=prod
APP_BASE_URL=https://fatc-grocery-store.onrender.com
```

---

## ⚠️ QUAN TRỌNG

**Đã thay đổi:**
- ❌ `SPRING_DATASOURCE_DRIVER` (cũ - không dùng nữa)
- ✅ `SPRING_DATASOURCE_DRIVER_CLASS_NAME` (mới - dùng cái này)

**Lý do:** HikariCP cần `driver-class-name` để phân biệt giữa MariaDB và PostgreSQL driver!

---

## 🚀 SAU KHI PASTE

1. Click **"Add Variables"**
2. App sẽ tự động **redeploy**
3. Đợi **5-10 phút**
4. Check logs → Thấy "Started Java5AsmApplication" → **THÀNH CÔNG!** 🎉

---

## 📞 NẾU VẪN LỖI

Xem logs, tìm dòng:
```
driverClassName...................org.postgresql.Driver  ✅
```

Nếu thấy dòng này → Driver đã đúng!

Nếu không thấy → Environment variables chưa được load, redeploy lại!

---

**Code đã fix và push lên GitHub! Chỉ cần paste env variables là xong!** 🚀
