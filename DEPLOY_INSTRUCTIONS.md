# 🚀 HƯỚNG DẪN DEPLOY - SIÊU ĐỠN GIẢN

## Bước 1: Đăng ký Cloudinary (5 phút)

1. Vào: https://cloudinary.com/users/register/free
2. Đăng ký bằng email
3. Vào Dashboard: https://console.cloudinary.com/
4. Copy 3 giá trị này (sẽ dùng ở bước 3):
   ```
   Cloud name: _______________
   API Key: _______________
   API Secret: _______________
   ```

## Bước 2: Đăng ký Render (3 phút)

1. Vào: https://dashboard.render.com
2. Đăng ký bằng GitHub account
3. Authorize Render truy cập GitHub

## Bước 3: Tạo Database (5 phút)

1. Trong Render Dashboard, click **New +** → **PostgreSQL**
2. Điền thông tin:
   - **Name:** `fatc-grocery-db`
   - **Database:** `java5_asm`
   - **User:** `java5_user`
   - **Region:** `Singapore`
   - **Plan:** `Free`
3. Click **Create Database**
4. Đợi 2 phút cho database khởi động
5. Click vào database vừa tạo
6. Copy **Internal Database URL** (dạng: `postgresql://java5_user:...`)
7. Lưu URL này vào notepad

## Bước 4: Deploy Application (10 phút)

1. Trong Render Dashboard, click **New +** → **Web Service**
2. Click **Connect a repository**
3. Chọn repository: `ThinhChauTran263/fatc-Grocery-Store`
4. Click **Connect**
5. Điền thông tin:
   - **Name:** `fatc-grocery-store`
   - **Region:** `Singapore`
   - **Branch:** `ThinhDev`
   - **Runtime:** `Docker`
   - **Plan:** `Free`
6. Click **Advanced** để thêm Environment Variables

## Bước 5: Thêm Environment Variables

Click **Add Environment Variable** và thêm từng cặp sau:

### Database (BẮT BUỘC)
```
SPRING_DATASOURCE_URL = [paste Internal Database URL từ bước 3]
SPRING_DATASOURCE_DRIVER = org.postgresql.Driver
SPRING_JPA_DIALECT = org.hibernate.dialect.PostgreSQLDialect
```

### Cloudinary (BẮT BUỘC)
```
USE_CLOUDINARY = true
CLOUDINARY_CLOUD_NAME = [paste từ bước 1]
CLOUDINARY_API_KEY = [paste từ bước 1]
CLOUDINARY_API_SECRET = [paste từ bước 1]
```

### JWT (BẮT BUỘC)
```
JWT_SECRET = fatc-grocery-store-super-secret-key-2024-minimum-32-chars
JWT_EXPIRATION = 86400000
```

### Application (BẮT BUỘC)
```
SPRING_PROFILES_ACTIVE = prod
APP_BASE_URL = https://fatc-grocery-store.onrender.com
```

### Google OAuth2 (TÙY CHỌN - nếu muốn login bằng Google)
```
GOOGLE_CLIENT_ID = your-google-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET = your-google-client-secret
```

### Email (TÙY CHỌN - nếu muốn gửi email)
```
MAIL_USERNAME = your-email@gmail.com
MAIL_PASSWORD = your-app-password
```

## Bước 6: Deploy!

1. Click **Create Web Service**
2. Đợi 5-10 phút để build
3. Xem logs để theo dõi quá trình build
4. Khi thấy "Started Java5AsmApplication" → Thành công!

## Bước 7: Khởi tạo Database (5 phút)

### Cách 1: Tự động (Khuyến nghị)

1. Vào Render Dashboard → Service `fatc-grocery-store`
2. Click **Environment**
3. Thêm biến tạm thời:
   ```
   SPRING_JPA_HIBERNATE_DDL_AUTO = create
   ```
4. Click **Save Changes** (app sẽ tự động redeploy)
5. Đợi app restart xong
6. **XÓA** biến `SPRING_JPA_HIBERNATE_DDL_AUTO`
7. Click **Save Changes** lại

### Cách 2: Import SQL thủ công

1. Vào Render Dashboard → Database `fatc-grocery-db`
2. Click **Connect** → Copy connection string
3. Dùng tool như DBeaver hoặc pgAdmin để connect
4. Import file SQL từ `mariadb_init/` (cần convert MariaDB → PostgreSQL)

## Bước 8: Test Application

1. Truy cập: `https://fatc-grocery-store.onrender.com`
2. Đăng ký tài khoản mới
3. Đăng nhập
4. Test upload ảnh sản phẩm
5. Kiểm tra ảnh có hiển thị không

## 🎉 HOÀN THÀNH!

App của bạn đã live tại: **https://fatc-grocery-store.onrender.com**

---

## ⚠️ LƯU Ý QUAN TRỌNG

### Free Tier Limitations:
- App sẽ **sleep** sau 15 phút không dùng
- **Cold start** mất 30-60 giây khi truy cập lại
- Database free có giới hạn 1GB storage
- Sau 90 ngày không dùng, database sẽ bị xóa

### Nếu muốn app không sleep:
- Upgrade lên **Starter Plan** ($7/month)
- Hoặc dùng cron job ping app mỗi 10 phút

---

## 🐛 Troubleshooting

### App không start?
- Xem logs trên Render Dashboard
- Kiểm tra database URL có đúng không
- Kiểm tra tất cả environment variables đã điền đủ chưa

### Upload ảnh bị lỗi?
- Kiểm tra Cloudinary credentials
- Xem logs: tìm "Uploading to Cloudinary"
- Vào Cloudinary Dashboard xem có ảnh không

### Database connection failed?
- Kiểm tra database đã running chưa
- Kiểm tra Internal Database URL có đúng không
- Kiểm tra driver: `org.postgresql.Driver`

---

## 📞 Cần giúp đỡ?

Xem thêm:
- [RENDER_DEPLOY_CHECKLIST.md](RENDER_DEPLOY_CHECKLIST.md) - Chi tiết hơn
- [CLOUDINARY_SETUP_GUIDE.md](document_file/CLOUDINARY_SETUP_GUIDE.md) - Hướng dẫn Cloudinary
