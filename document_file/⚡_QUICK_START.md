# ⚡ QUICK START - DEPLOY TRONG 10 PHÚT

## 🎯 Chuẩn bị (5 phút)

### 1. Đăng ký Cloudinary
- Vào: https://cloudinary.com/users/register/free
- Copy: **Cloud name**, **API Key**, **API Secret**

### 2. Điền vào file
- Mở: **render-import-TEMPLATE.env**
- Thay `[ĐIỀN VÀO ĐÂY]` bằng thông tin Cloudinary
- Lưu file

---

## 🚀 Deploy (5 phút)

### 1. Tạo Web Service
1. Vào: https://dashboard.render.com
2. **New +** → **Web Service**
3. Connect repo: `ThinhChauTran263/fatc-Grocery-Store`
4. Settings:
   - Name: `fatc-grocery-store`
   - Branch: `ThinhDev`
   - Runtime: `Docker`
   - Region: `Singapore`
   - Plan: `Free`

### 2. Import Environment Variables
1. Tab **Environment**
2. Click **"Add from .env"**
3. Copy **TOÀN BỘ** file **render-import-TEMPLATE.env** (đã điền Cloudinary)
4. Paste và click **"Add Variables"**

### 3. Deploy
1. Click **"Create Web Service"**
2. Đợi 5-10 phút
3. Xem logs

---

## 🗄️ Khởi tạo Database (2 phút)

1. Vào **Environment**
2. Thêm: `SPRING_JPA_HIBERNATE_DDL_AUTO` = `create`
3. Đợi restart (2 phút)
4. **XÓA** biến đó
5. Save

---

## 👤 Tạo Admin (1 phút)

### Cách 1: SQL
```sql
INSERT INTO users (username, email, password, full_name, role, is_active, created_at, updated_at)
VALUES ('admin', 'admin@fatcgrocery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

### Cách 2: Nâng user
1. Đăng ký tài khoản
2. SQL: `UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';`

---

## ✅ Test

1. Truy cập: https://fatc-grocery-store.onrender.com
2. Login: `admin` / `admin123`
3. **ĐỔI MẬT KHẨU!**
4. Upload ảnh test

---

## 🎉 DONE!

**Tổng thời gian:** ~10 phút

---

## 📚 Chi tiết hơn?

- **IMPORT_ENV_GUIDE.md** - Hướng dẫn import env
- **ADMIN_QUICK_GUIDE.md** - Hướng dẫn tạo admin
- **📖_DEPLOYMENT_INDEX.md** - Chỉ mục đầy đủ

---

## 🐛 Lỗi?

### App không start?
- Xem logs
- Kiểm tra Cloudinary credentials

### Database lỗi?
- Kiểm tra database đã running
- Verify connection string

### Upload ảnh lỗi?
- Vào https://console.cloudinary.com/ verify
- Kiểm tra `USE_CLOUDINARY=true`

---

**Good luck! 🚀**
