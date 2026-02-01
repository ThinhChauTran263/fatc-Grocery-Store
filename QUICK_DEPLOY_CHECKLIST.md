# ✅ CHECKLIST DEPLOY NHANH - 30 PHÚT

## ☑️ Bước 1: Cloudinary (5 phút)
- [ ] Vào: https://cloudinary.com/users/register/free
- [ ] Đăng ký tài khoản
- [ ] Vào Dashboard: https://console.cloudinary.com/
- [ ] Copy 3 giá trị:
  - Cloud name: `_______________`
  - API Key: `_______________`
  - API Secret: `_______________`

## ☑️ Bước 2: Render Account (2 phút)
- [ ] Vào: https://dashboard.render.com
- [ ] Sign up with GitHub
- [ ] Authorize Render

## ☑️ Bước 3: Tạo Database (5 phút)
- [ ] Click **New +** → **PostgreSQL**
- [ ] Name: `fatc-grocery-db`
- [ ] Database: `java5_asm`
- [ ] User: `java5_user`
- [ ] Region: `Singapore`
- [ ] Plan: `Free`
- [ ] Click **Create Database**
- [ ] Copy **Internal Database URL**: `_______________`

## ☑️ Bước 4: Deploy App (10 phút)
- [ ] Click **New +** → **Web Service**
- [ ] Connect repository: `ThinhChauTran263/fatc-Grocery-Store`
- [ ] Name: `fatc-grocery-store`
- [ ] Branch: `ThinhDev`
- [ ] Region: `Singapore`
- [ ] Runtime: `Docker`
- [ ] Plan: `Free`

## ☑️ Bước 5: Environment Variables (5 phút)

Copy từ file `.env.render` và điền vào Render:

### Database (3 biến)
```
SPRING_DATASOURCE_URL = [paste Internal Database URL]
SPRING_DATASOURCE_DRIVER = org.postgresql.Driver
SPRING_JPA_DIALECT = org.hibernate.dialect.PostgreSQLDialect
```

### Cloudinary (4 biến)
```
USE_CLOUDINARY = true
CLOUDINARY_CLOUD_NAME = [paste từ bước 1]
CLOUDINARY_API_KEY = [paste từ bước 1]
CLOUDINARY_API_SECRET = [paste từ bước 1]
```

### JWT (2 biến)
```
JWT_SECRET = fatc-grocery-store-super-secret-key-2024-minimum-32-chars
JWT_EXPIRATION = 86400000
```

### App (2 biến)
```
SPRING_PROFILES_ACTIVE = prod
APP_BASE_URL = https://fatc-grocery-store.onrender.com
```

- [ ] Tất cả 11 biến đã điền xong

## ☑️ Bước 6: Deploy (3 phút)
- [ ] Click **Create Web Service**
- [ ] Đợi build (5-10 phút)
- [ ] Xem logs
- [ ] Thấy "Started Java5AsmApplication" → Success!

## ☑️ Bước 7: Khởi tạo Database (5 phút)
- [ ] Vào Service → Environment
- [ ] Thêm: `SPRING_JPA_HIBERNATE_DDL_AUTO = create`
- [ ] Save (app sẽ redeploy)
- [ ] Đợi restart xong
- [ ] **XÓA** biến `SPRING_JPA_HIBERNATE_DDL_AUTO`
- [ ] Save lại

## ☑️ Bước 8: Test (2 phút)
- [ ] Truy cập: https://fatc-grocery-store.onrender.com
- [ ] Đăng nhập admin: `admin` / `admin123`
- [ ] **ĐỔI MẬT KHẨU NGAY!**
- [ ] Test upload ảnh sản phẩm
- [ ] Kiểm tra ảnh hiển thị

## ☑️ Bước 9: Tạo Admin (nếu chưa có)
- [ ] Connect vào PostgreSQL database
- [ ] Chạy SQL tạo admin (xem ADMIN_ACCOUNT_GUIDE.md)
- [ ] Hoặc nâng cấp user thường lên admin

## 🎉 HOÀN THÀNH!

**URL của bạn:** https://fatc-grocery-store.onrender.com

---

## 📞 Gặp vấn đề?

### App không start?
→ Xem logs trên Render Dashboard
→ Kiểm tra database URL
→ Kiểm tra tất cả env variables

### Upload ảnh lỗi?
→ Kiểm tra Cloudinary credentials
→ Xem logs: tìm "Cloudinary"

### Database lỗi?
→ Kiểm tra database đã running
→ Kiểm tra driver: `org.postgresql.Driver`

---

## 📚 Tài liệu chi tiết

- [DEPLOY_INSTRUCTIONS.md](DEPLOY_INSTRUCTIONS.md) - Hướng dẫn từng bước
- [RENDER_DEPLOY_CHECKLIST.md](RENDER_DEPLOY_CHECKLIST.md) - Checklist đầy đủ
- [.env.render](.env.render) - Template environment variables
