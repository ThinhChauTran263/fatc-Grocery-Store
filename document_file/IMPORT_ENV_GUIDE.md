# ⚡ HƯỚNG DẪN IMPORT ENVIRONMENT VARIABLES NHANH

## 🎯 Cách nhanh nhất (2 phút)

### Bước 1: Chuẩn bị file
1. Mở file: **render-import.env**
2. Thay thế các giá trị Cloudinary:
   ```
   CLOUDINARY_CLOUD_NAME=your-cloud-name-here  → Thay bằng cloud name thật
   CLOUDINARY_API_KEY=your-api-key-here        → Thay bằng api key thật
   CLOUDINARY_API_SECRET=your-api-secret-here  → Thay bằng api secret thật
   ```
3. Lưu file

### Bước 2: Import vào Render
1. Vào: https://dashboard.render.com
2. Click vào service **fatc-grocery-store**
3. Click tab **Environment**
4. Click nút **"Add from .env"** (góc phải)
5. Copy **TOÀN BỘ** nội dung file **render-import.env**
6. Paste vào ô text
7. Click **"Add Variables"**
8. Done! ✅

---

## 📋 Nội dung file render-import.env

File này đã có sẵn:
- ✅ Database URL (đã điền sẵn)
- ✅ PostgreSQL driver và dialect
- ✅ JWT secret
- ✅ Application settings
- ⚠️ Cloudinary (cần điền)
- 💡 Google OAuth2 (tùy chọn, đang comment)
- 💡 Email (tùy chọn, đang comment)

---

## 🔧 Sau khi import

### 1. Verify các biến đã import
Kiểm tra trong tab **Environment** có đủ 11 biến:
- [x] SPRING_DATASOURCE_URL
- [x] SPRING_DATASOURCE_DRIVER
- [x] SPRING_JPA_DIALECT
- [x] USE_CLOUDINARY
- [x] CLOUDINARY_CLOUD_NAME
- [x] CLOUDINARY_API_KEY
- [x] CLOUDINARY_API_SECRET
- [x] JWT_SECRET
- [x] JWT_EXPIRATION
- [x] SPRING_PROFILES_ACTIVE
- [x] APP_BASE_URL

### 2. Deploy
- App sẽ tự động redeploy sau khi thêm biến
- Hoặc click **"Manual Deploy"** → **"Deploy latest commit"**

### 3. Khởi tạo database (lần đầu)
Thêm biến tạm thời:
```
SPRING_JPA_HIBERNATE_DDL_AUTO=create
```
Đợi app restart, rồi **XÓA** biến này đi.

---

## 🎁 Bonus: Thêm Google OAuth2 và Email

Nếu muốn dùng Google login hoặc gửi email:

### Bước 1: Uncomment trong file
Mở **render-import.env**, bỏ dấu `#` ở các dòng:
```env
GOOGLE_CLIENT_ID=your-google-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your-google-client-secret
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password-16-chars
MAIL_FROM=Fat C Grocery Store <noreply@fatcgrocery.com>
```

### Bước 2: Điền thông tin thật
- Google: Lấy từ https://console.cloud.google.com/apis/credentials
- Email: Tạo App Password tại https://myaccount.google.com/apppasswords

### Bước 3: Import lại
Hoặc thêm thủ công từng biến trong Render Dashboard.

---

## 🐛 Troubleshooting

### Không thấy nút "Add from .env"?
- Kiểm tra bạn đang ở tab **Environment**
- Nút ở góc phải, gần nút "Add Environment Variable"

### Import bị lỗi?
- Kiểm tra format file (mỗi dòng: `KEY=VALUE`)
- Không có khoảng trắng thừa
- Không có dòng trống giữa các biến

### Một số biến không import được?
- Các dòng bắt đầu bằng `#` sẽ bị bỏ qua (comment)
- Thêm thủ công các biến bị thiếu

---

## ⚡ So sánh các cách

| Cách | Thời gian | Độ khó |
|------|-----------|--------|
| Import file .env | 2 phút | ⭐ Dễ nhất |
| Copy paste từng biến | 10 phút | ⭐⭐ Trung bình |
| Thêm thủ công | 15 phút | ⭐⭐⭐ Khó nhất |

**Khuyến nghị:** Dùng **Import file .env** (file này!)

---

## 📞 Tóm tắt

1. Mở **render-import.env**
2. Thay Cloudinary credentials
3. Copy toàn bộ file
4. Vào Render → Environment → "Add from .env"
5. Paste và click "Add Variables"
6. Done! 🎉

**Thời gian:** ~2 phút

---

## 🎯 File liên quan

- **render-import.env** ⭐⭐⭐ - File để import
- **env-variables-copy-paste.txt** - Copy paste từng biến
- **RENDER_ENV_SETUP.md** - Hướng dẫn chi tiết

---

**Chúc bạn thành công! 🚀**
