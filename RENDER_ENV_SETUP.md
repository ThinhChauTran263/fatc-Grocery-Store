# 🔧 HƯỚNG DẪN SETUP ENVIRONMENT VARIABLES TRÊN RENDER

## 📋 Danh sách Environment Variables cần thêm

Copy từng cặp key-value dưới đây vào Render Dashboard:

---

## 1️⃣ DATABASE (3 biến - BẮT BUỘC)

```
Key: SPRING_DATASOURCE_URL
Value: postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a/java5_asm
```

```
Key: SPRING_DATASOURCE_DRIVER
Value: org.postgresql.Driver
```

```
Key: SPRING_JPA_DIALECT
Value: org.hibernate.dialect.PostgreSQLDialect
```

---

## 2️⃣ CLOUDINARY (4 biến - BẮT BUỘC)

⚠️ **Lấy từ:** https://console.cloudinary.com/

```
Key: USE_CLOUDINARY
Value: true
```

```
Key: CLOUDINARY_CLOUD_NAME
Value: [điền cloud name của bạn]
```

```
Key: CLOUDINARY_API_KEY
Value: [điền api key của bạn]
```

```
Key: CLOUDINARY_API_SECRET
Value: [điền api secret của bạn]
```

---

## 3️⃣ JWT (2 biến - BẮT BUỘC)

```
Key: JWT_SECRET
Value: fatc-grocery-store-super-secret-key-2024-minimum-32-chars
```

```
Key: JWT_EXPIRATION
Value: 86400000
```

---

## 4️⃣ APPLICATION (2 biến - BẮT BUỘC)

```
Key: SPRING_PROFILES_ACTIVE
Value: prod
```

```
Key: APP_BASE_URL
Value: https://fatc-grocery-store.onrender.com
```

⚠️ **Lưu ý:** Thay `fatc-grocery-store` bằng tên service của bạn nếu khác

---

## 5️⃣ GOOGLE OAUTH2 (2 biến - TÙY CHỌN)

⚠️ **Lấy từ:** https://console.cloud.google.com/apis/credentials

```
Key: GOOGLE_CLIENT_ID
Value: [điền client id của bạn]
```

```
Key: GOOGLE_CLIENT_SECRET
Value: [điền client secret của bạn]
```

**Nhớ thêm Redirect URI:**
```
https://fatc-grocery-store.onrender.com/login/oauth2/code/google
```

---

## 6️⃣ EMAIL (3 biến - TÙY CHỌN)

⚠️ **Với Gmail, tạo App Password tại:** https://myaccount.google.com/apppasswords

```
Key: MAIL_USERNAME
Value: [email của bạn]
```

```
Key: MAIL_PASSWORD
Value: [app password 16 ký tự]
```

```
Key: MAIL_FROM
Value: Fat C Grocery Store <noreply@fatcgrocery.com>
```

---

## 🚀 CÁCH THÊM VÀO RENDER

### Bước 1: Vào Render Dashboard
1. Truy cập: https://dashboard.render.com
2. Click vào service `fatc-grocery-store`
3. Click tab **Environment**

### Bước 2: Thêm từng biến
1. Click **Add Environment Variable**
2. Nhập **Key** (ví dụ: `SPRING_DATASOURCE_URL`)
3. Nhập **Value** (copy từ trên)
4. Click **Save**
5. Lặp lại cho tất cả các biến

### Bước 3: Deploy
Sau khi thêm xong tất cả biến:
1. Click **Manual Deploy** → **Deploy latest commit**
2. Hoặc đợi auto-deploy (nếu đã bật)

---

## ✅ CHECKLIST

### Biến bắt buộc (11 biến):
- [ ] SPRING_DATASOURCE_URL
- [ ] SPRING_DATASOURCE_DRIVER
- [ ] SPRING_JPA_DIALECT
- [ ] USE_CLOUDINARY
- [ ] CLOUDINARY_CLOUD_NAME
- [ ] CLOUDINARY_API_KEY
- [ ] CLOUDINARY_API_SECRET
- [ ] JWT_SECRET
- [ ] JWT_EXPIRATION
- [ ] SPRING_PROFILES_ACTIVE
- [ ] APP_BASE_URL

### Biến tùy chọn:
- [ ] GOOGLE_CLIENT_ID (nếu dùng Google login)
- [ ] GOOGLE_CLIENT_SECRET (nếu dùng Google login)
- [ ] MAIL_USERNAME (nếu gửi email)
- [ ] MAIL_PASSWORD (nếu gửi email)
- [ ] MAIL_FROM (nếu gửi email)

---

## 🔄 KHỞI TẠO DATABASE (LẦN ĐẦU)

Sau khi thêm tất cả biến trên, thêm biến tạm thời:

```
Key: SPRING_JPA_HIBERNATE_DDL_AUTO
Value: create
```

**Sau khi app restart xong (2-3 phút):**
1. Vào lại **Environment**
2. **XÓA** biến `SPRING_JPA_HIBERNATE_DDL_AUTO`
3. Click **Save Changes**

⚠️ **Quan trọng:** Phải xóa biến này sau khi khởi tạo xong!

---

## 🐛 TROUBLESHOOTING

### App không start?
```bash
# Kiểm tra logs:
1. Vào Render Dashboard
2. Click service
3. Tab "Logs"
4. Tìm lỗi
```

### Database connection failed?
- Kiểm tra `SPRING_DATASOURCE_URL` có đúng không
- Kiểm tra database đã running chưa
- Kiểm tra `SPRING_DATASOURCE_DRIVER` = `org.postgresql.Driver`

### Cloudinary upload failed?
- Kiểm tra `USE_CLOUDINARY=true`
- Kiểm tra credentials Cloudinary
- Vào https://console.cloudinary.com/ verify

---

## 📊 VERIFY

Sau khi setup xong, kiểm tra:

1. **App running:**
   - Truy cập: https://fatc-grocery-store.onrender.com
   - Không có lỗi 500

2. **Database connected:**
   - Đăng ký tài khoản mới
   - Đăng nhập thành công

3. **Cloudinary working:**
   - Đăng nhập admin
   - Upload ảnh sản phẩm
   - Ảnh hiển thị từ Cloudinary

---

## 🎉 DONE!

Nếu tất cả checklist đều ✅, app của bạn đã sẵn sàng!

**URL:** https://fatc-grocery-store.onrender.com
