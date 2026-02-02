# 🔍 Kiểm Tra Email Không Gửi

## ✅ Các Bước Kiểm Tra

### Bước 1: Kiểm Tra Environment Variables trên Render

1. Vào Render Dashboard → Service của bạn
2. Vào tab **Environment**
3. Kiểm tra các biến sau:

```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-16-character-app-password
MAIL_FROM=Fat C Grocery Store <your-email@gmail.com>
```

**Quan trọng:**
- `MAIL_PASSWORD` phải là **App Password** (16 ký tự), KHÔNG phải mật khẩu Gmail thường
- Tạo App Password tại: https://myaccount.google.com/apppasswords

### Bước 2: Kiểm Tra Logs trên Render

1. Vào tab **Logs**
2. Đặt hàng 1 đơn test
3. Tìm các dòng log:

**Nếu email hoạt động:**
```
=== START sendOrderConfirmation === orderId=X, userId=Y
Found order ORD-XXX with 2 items for user test@example.com
=== END sendOrderConfirmation === email sent successfully for order ORD-XXX
```

**Nếu email chưa cấu hình:**
```
Email not configured properly. Skipping email send. fromEmail=noreply@grocerystore.com
```

**Nếu có lỗi:**
```
=== ERROR sendOrderConfirmation === orderId X: Authentication failed
```

### Bước 3: Test Email Configuration

Chạy lệnh này trong Render Shell (hoặc local):

```bash
curl -X POST http://localhost:8080/actuator/health/mail
```

Hoặc kiểm tra health endpoint:
```
https://your-app.onrender.com/actuator/health
```

---

## 🐛 Các Lỗi Thường Gặp

### Lỗi 1: "Email not configured properly"
**Nguyên nhân**: Chưa set environment variables

**Giải pháp**:
1. Vào Render → Environment
2. Thêm:
   ```
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-app-password
   MAIL_FROM=Fat C Grocery Store <your-email@gmail.com>
   ```
3. Restart service

### Lỗi 2: "Authentication failed"
**Nguyên nhân**: 
- Dùng mật khẩu Gmail thường thay vì App Password
- App Password sai
- Gmail chưa bật 2-Step Verification

**Giải pháp**:
1. Bật 2-Step Verification: https://myaccount.google.com/security
2. Tạo App Password mới: https://myaccount.google.com/apppasswords
3. Copy 16 ký tự (không có dấu cách)
4. Update `MAIL_PASSWORD` trong Render
5. Restart service

### Lỗi 3: "Connection timeout"
**Nguyên nhân**: 
- Port 587 bị block
- SMTP server không accessible

**Giải pháp**:
1. Kiểm tra firewall/network
2. Thử port 465 (SSL) thay vì 587 (TLS)
3. Update `application.yml`:
   ```yaml
   spring:
     mail:
       port: 465
       properties:
         mail:
           smtp:
             ssl:
               enable: true
   ```

### Lỗi 4: Email vào Spam
**Nguyên nhân**: Gmail đánh dấu email từ App Password là spam

**Giải pháp**:
1. Kiểm tra folder Spam
2. Đánh dấu "Not Spam"
3. Thêm sender vào Contacts
4. Hoặc dùng SendGrid/Mailgun cho production

---

## 🔧 Fix Code

**Commit mới nhất đã fix:**
- Enable `@Async` để gửi email không block request
- Thêm check email configuration trước khi gửi
- Log rõ ràng hơn để debug

**Deploy commit mới:**
1. Deploy latest commit lên Render
2. Restart service
3. Test lại

---

## 📧 Cách Tạo Gmail App Password

### Bước 1: Bật 2-Step Verification
1. Vào https://myaccount.google.com/security
2. Tìm "2-Step Verification"
3. Nhấn "Get Started" và làm theo hướng dẫn

### Bước 2: Tạo App Password
1. Vào https://myaccount.google.com/apppasswords
2. Chọn "Select app" → "Mail"
3. Chọn "Select device" → "Other (Custom name)"
4. Nhập tên: "Grocery Store App"
5. Nhấn "Generate"
6. Copy 16 ký tự (ví dụ: `abcd efgh ijkl mnop`)
7. Dùng password này (không có dấu cách): `abcdefghijklmnop`

### Bước 3: Update Render Environment
```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=abcdefghijklmnop
MAIL_FROM=Fat C Grocery Store <your-email@gmail.com>
```

---

## ✅ Test Email Sau Khi Fix

1. Deploy code mới
2. Restart service
3. Đặt 1 đơn hàng test
4. Kiểm tra logs:
   ```
   === START sendOrderConfirmation ===
   Found order ORD-XXX with 2 items for user test@example.com
   === END sendOrderConfirmation === email sent successfully
   ```
5. Kiểm tra email inbox (và spam folder)

---

## 🎯 Nếu Vẫn Không Hoạt Động

Gửi cho mình:
1. Screenshot Environment variables (che MAIL_PASSWORD)
2. Screenshot logs khi đặt hàng
3. Screenshot error message (nếu có)

Mình sẽ giúp debug tiếp! 🚀
