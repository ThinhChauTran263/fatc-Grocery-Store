# 🔐 Hướng dẫn Cấu hình Google OAuth2

## ❌ Lỗi hiện tại
```
The OAuth client was not found.
Lỗi 401: invalid_client
```

**Nguyên nhân:** Google Client ID và Client Secret chưa được cấu hình đúng trong file `.env`

## 📋 Các bước cấu hình

### Bước 1: Tạo Google OAuth2 Credentials

1. **Truy cập Google Cloud Console:**
   - Mở: https://console.cloud.google.com/

2. **Tạo hoặc chọn Project:**
   - Click vào dropdown project ở góc trên bên trái
   - Click "NEW PROJECT"
   - Đặt tên: `Coffee Shop` (hoặc tên bạn muốn)
   - Click "CREATE"

3. **Bật Google+ API:**
   - Vào menu ☰ → "APIs & Services" → "Library"
   - Tìm "Google+ API"
   - Click "ENABLE"

4. **Tạo OAuth Consent Screen:**
   - Vào menu ☰ → "APIs & Services" → "OAuth consent screen"
   - Chọn "External" → Click "CREATE"
   - Điền thông tin:
     - **App name:** `Coffee Shop`
     - **User support email:** Email của bạn
     - **Developer contact email:** Email của bạn
   - Click "SAVE AND CONTINUE"
   - Scopes: Click "SAVE AND CONTINUE" (giữ mặc định)
   - Test users: Click "SAVE AND CONTINUE"
   - Click "BACK TO DASHBOARD"

5. **Tạo OAuth 2.0 Client ID:**
   - Vào menu ☰ → "APIs & Services" → "Credentials"
   - Click "CREATE CREDENTIALS" → "OAuth client ID"
   - Application type: Chọn "Web application"
   - Name: `Coffee Shop Web Client`
   - **Authorized JavaScript origins:**
     ```
     http://localhost:8080
     ```
   - **Authorized redirect URIs:**
     ```
     http://localhost:8080/login/oauth2/code/google
     ```
   - Click "CREATE"

6. **Lưu thông tin:**
   - Một popup sẽ hiện ra với:
     - **Client ID:** `xxxxx.apps.googleusercontent.com`
     - **Client Secret:** `GOCSPX-xxxxx`
   - **QUAN TRỌNG:** Copy 2 giá trị này!

### Bước 2: Cập nhật file .env

Mở file `.env` và cập nhật:

```env
# ============================================
# GOOGLE OAUTH2 CONFIGURATION
# ============================================
GOOGLE_CLIENT_ID=your-actual-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-your-actual-client-secret
```

**Thay thế:**
- `your-actual-client-id` → Client ID bạn vừa copy
- `GOCSPX-your-actual-client-secret` → Client Secret bạn vừa copy

### Bước 3: Khởi động lại ứng dụng

1. **Dừng ứng dụng** (nếu đang chạy)
2. **Khởi động lại** từ IntelliJ
3. **Truy cập:** http://localhost:8080/sign-in
4. **Click nút "Sign in with Google"**

## ✅ Kiểm tra cấu hình

### Kiểm tra trong Google Cloud Console:

1. Vào "APIs & Services" → "Credentials"
2. Click vào OAuth 2.0 Client ID bạn vừa tạo
3. Đảm bảo có:
   - **Authorized JavaScript origins:** `http://localhost:8080`
   - **Authorized redirect URIs:** `http://localhost:8080/login/oauth2/code/google`

### Kiểm tra file .env:

```bash
# Xem nội dung file .env (Windows)
type .env | findstr GOOGLE
```

Kết quả phải có dạng:
```
GOOGLE_CLIENT_ID=123456789-abcdefg.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-abcdefghijklmnop
```

## 🔧 Xử lý lỗi thường gặp

### Lỗi: "The OAuth client was not found"
**Nguyên nhân:**
- Client ID hoặc Client Secret sai
- Chưa cập nhật file `.env`
- Chưa khởi động lại ứng dụng

**Giải pháp:**
1. Kiểm tra lại Client ID và Client Secret trong Google Cloud Console
2. Copy chính xác vào file `.env`
3. Khởi động lại ứng dụng

### Lỗi: "redirect_uri_mismatch"
**Nguyên nhân:**
- Redirect URI không khớp với cấu hình trong Google Cloud Console

**Giải pháp:**
1. Vào Google Cloud Console → Credentials
2. Thêm chính xác: `http://localhost:8080/login/oauth2/code/google`
3. Lưu lại và thử lại

### Lỗi: "Access blocked: This app's request is invalid"
**Nguyên nhân:**
- OAuth Consent Screen chưa được cấu hình

**Giải pháp:**
1. Vào "OAuth consent screen"
2. Hoàn thành tất cả các bước
3. Thêm email của bạn vào "Test users" (nếu app ở chế độ Testing)

### Lỗi: "This app isn't verified"
**Nguyên nhân:**
- App đang ở chế độ Testing

**Giải pháp:**
- Click "Advanced" → "Go to Coffee Shop (unsafe)"
- Hoặc thêm email test vào "Test users" trong OAuth consent screen

## 📝 Ví dụ file .env hoàn chỉnh

```env
# ============================================
# DATABASE CONFIGURATION
# ============================================
DB_HOST=mariadb
DB_PORT=3306
DB_NAME=java5_asm
DB_USERNAME=java5_user
DB_PASSWORD=java5_password
DB_ROOT_PASSWORD=root_password_123

# ============================================
# SPRING DATASOURCE (Khi chạy từ IntelliJ)
# ============================================
SPRING_DATASOURCE_URL=jdbc:mariadb://localhost:3307/java5_asm
SPRING_DATASOURCE_USERNAME=java5_user
SPRING_DATASOURCE_PASSWORD=java5_password

# ============================================
# GOOGLE OAUTH2 CONFIGURATION
# ============================================
# Thay thế bằng giá trị thực từ Google Cloud Console
GOOGLE_CLIENT_ID=123456789-abcdefghijklmnop.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=GOCSPX-abcdefghijklmnopqrstuvwxyz

# ============================================
# JWT CONFIGURATION
# ============================================
JWT_SECRET=my-super-secret-jwt-key-minimum-32-characters-required-here
JWT_EXPIRATION=86400000

# ============================================
# APPLICATION CONFIGURATION
# ============================================
APP_PORT=8080
APP_BASE_URL=http://localhost:8080

# ============================================
# EMAIL CONFIGURATION (Optional)
# ============================================
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=BigC GroceryStore <noreply@bigcgrocery.com>
```

## 🎯 Luồng hoạt động OAuth2

1. User click "Sign in with Google"
2. Redirect đến Google login page
3. User đăng nhập và cho phép quyền
4. Google redirect về: `http://localhost:8080/login/oauth2/code/google?code=xxx`
5. Spring Security xử lý code và lấy thông tin user
6. Tạo JWT token và lưu vào cookie
7. Redirect về trang chủ

## 🔗 Tài liệu tham khảo

- Google Cloud Console: https://console.cloud.google.com/
- OAuth 2.0 Playground: https://developers.google.com/oauthplayground/
- Spring Security OAuth2: https://docs.spring.io/spring-security/reference/servlet/oauth2/login/core.html

## 💡 Lưu ý quan trọng

1. **Không commit file .env lên Git** - File này chứa thông tin nhạy cảm
2. **Client Secret phải giữ bí mật** - Không chia sẻ với ai
3. **Redirect URI phải khớp chính xác** - Bao gồm cả http/https và port
4. **Test users** - Nếu app ở chế độ Testing, chỉ email trong danh sách Test users mới đăng nhập được
5. **Production** - Khi deploy lên production, cần:
   - Tạo OAuth Client ID mới với domain thật
   - Cập nhật Authorized origins và Redirect URIs
   - Publish OAuth Consent Screen

## 🚀 Bước tiếp theo

Sau khi cấu hình xong:
1. Khởi động lại ứng dụng
2. Truy cập: http://localhost:8080/sign-in
3. Click "Sign in with Google"
4. Đăng nhập bằng tài khoản Google
5. Cho phép quyền truy cập
6. Bạn sẽ được redirect về trang chủ và đã đăng nhập ✅
