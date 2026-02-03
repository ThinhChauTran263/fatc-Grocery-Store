# 🎯 BƯỚC TIẾP THEO - DEPLOY LÊN RENDER

## ✅ Đã hoàn thành:
- ✅ Code đã sẵn sàng
- ✅ Database URL đã có: `dpg-d5vlucngi27c73cej9pg-a`
- ✅ Tài liệu đầy đủ
- ✅ Push lên GitHub

---

## 🚀 BẮT ĐẦU DEPLOY (20 phút)

### Bước 1: Đăng ký Cloudinary (5 phút)
1. Vào: https://cloudinary.com/users/register/free
2. Đăng ký và verify email
3. Vào Dashboard: https://console.cloudinary.com/
4. Copy 3 giá trị:
   - Cloud name
   - API Key
   - API Secret

### Bước 2: Deploy Web Service (5 phút)
1. Vào: https://dashboard.render.com
2. Click **New +** → **Web Service**
3. Connect repository: `ThinhChauTran263/fatc-Grocery-Store`
4. Settings:
   - Name: `fatc-grocery-store`
   - Branch: `ThinhDev`
   - Runtime: `Docker`
   - Region: `Singapore`
   - Plan: `Free`

### Bước 3: Thêm Environment Variables (10 phút)

**Mở file:** `env-variables-copy-paste.txt`

Copy từng cặp Key-Value vào Render:

#### BẮT BUỘC (11 biến):
1. `SPRING_DATASOURCE_URL` = `postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm`
2. `SPRING_DATASOURCE_DRIVER` = `org.postgresql.Driver`
3. `SPRING_JPA_DIALECT` = `org.hibernate.dialect.PostgreSQLDialect`
4. `USE_CLOUDINARY` = `true`
5. `CLOUDINARY_CLOUD_NAME` = [điền của bạn]
6. `CLOUDINARY_API_KEY` = [điền của bạn]
7. `CLOUDINARY_API_SECRET` = [điền của bạn]
8. `JWT_SECRET` = `fatc-grocery-store-super-secret-key-2024-minimum-32-chars`
9. `JWT_EXPIRATION` = `86400000`
10. `SPRING_PROFILES_ACTIVE` = `prod`
11. `APP_BASE_URL` = `https://fatc-grocery-store.onrender.com`

#### TÙY CHỌN (nếu cần):
- Google OAuth2 (2 biến)
- Email (3 biến)

### Bước 4: Deploy
1. Click **Create Web Service**
2. Đợi build (5-10 phút)
3. Xem logs

---

## 🗄️ KHỞI TẠO DATABASE (5 phút)

### Sau khi app deploy xong:

1. Vào **Environment**
2. Thêm biến tạm thời:
   ```
   SPRING_JPA_HIBERNATE_DDL_AUTO = create
   ```
3. Click **Save Changes** (app sẽ restart)
4. Đợi 2-3 phút
5. **XÓA** biến `SPRING_JPA_HIBERNATE_DDL_AUTO`
6. Click **Save Changes** lại

---

## 👤 TẠO ADMIN (3 phút)

### Cách 1: Qua SQL
1. Connect vào database (xem `ADMIN_QUICK_GUIDE.md`)
2. Chạy SQL:
```sql
INSERT INTO users (username, email, password, full_name, role, is_active, created_at, updated_at)
VALUES ('admin', 'admin@fatcgrocery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

### Cách 2: Nâng user thường
1. Đăng ký tài khoản trên web
2. Chạy SQL:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

---

## ✅ TEST (2 phút)

1. Truy cập: https://fatc-grocery-store.onrender.com
2. Đăng nhập admin: `admin` / `admin123`
3. **ĐỔI MẬT KHẨU NGAY!**
4. Test upload ảnh sản phẩm
5. Kiểm tra ảnh hiển thị từ Cloudinary

---

## 📚 TÀI LIỆU THAM KHẢO

### Deploy:
- **env-variables-copy-paste.txt** ⭐ - Copy paste nhanh
- **RENDER_ENV_SETUP.md** - Hướng dẫn chi tiết
- **DEPLOY_INSTRUCTIONS.md** - Hướng dẫn đầy đủ

### Admin:
- **ADMIN_QUICK_GUIDE.md** ⭐ - Tạo admin nhanh
- **ADMIN_ACCOUNT_GUIDE.md** - Hướng dẫn đầy đủ

### Tổng quan:
- **START_HERE.md** - Điểm bắt đầu
- **DEPLOYMENT_READY.md** - Tổng quan

---

## 🎉 HOÀN THÀNH!

Sau khi làm xong các bước trên, app của bạn sẽ live tại:

**https://fatc-grocery-store.onrender.com**

---

## 🐛 GẶP VẤN ĐỀ?

### App không start?
- Xem logs trên Render Dashboard
- Kiểm tra tất cả env variables đã điền đủ chưa

### Upload ảnh lỗi?
- Kiểm tra Cloudinary credentials
- Vào https://console.cloudinary.com/ verify

### Database lỗi?
- Kiểm tra database đã running
- Kiểm tra connection string

---

## 📞 LIÊN HỆ

Cần giúp đỡ? Xem troubleshooting trong:
- RENDER_ENV_SETUP.md
- ADMIN_ACCOUNT_GUIDE.md
- DEPLOY_INSTRUCTIONS.md

**Good luck! 🚀**
