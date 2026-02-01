# 🖼️ HƯỚNG DẪN SETUP CLOUDINARY

## Tại sao cần Cloudinary?

Khi deploy lên cloud platform (Render, Railway, Cloud Run), file upload vào local filesystem sẽ **mất** khi:
- Container restart
- Scale up/down  
- Redeploy

**Cloudinary** là giải pháp lưu trữ ảnh chuyên nghiệp:
- ✅ Miễn phí 25GB storage + 25GB bandwidth/tháng
- ✅ Không cần thẻ tín dụng
- ✅ CDN toàn cầu (load ảnh nhanh)
- ✅ Tự động optimize ảnh
- ✅ Resize, crop, transform on-the-fly

---

## Bước 1: Đăng ký Cloudinary (5 phút)

1. Truy cập: https://cloudinary.com/users/register/free
2. Điền thông tin:
   - Email
   - Password
   - Cloud name (tên duy nhất, VD: `fatc-grocery-store`)
3. Verify email
4. Chọn plan **Free** (không cần thẻ)

---

## Bước 2: Lấy API Credentials (2 phút)

1. Đăng nhập vào: https://console.cloudinary.com/
2. Vào **Dashboard** (trang chủ)
3. Tìm phần **Account Details**:
   ```
   Cloud name: your-cloud-name
   API Key: 123456789012345
   API Secret: abcdefghijklmnopqrstuvwxyz
   ```
4. Click **Copy** để copy từng giá trị

---

## Bước 3: Cấu hình Local (3 phút)

### Cập nhật file `.env`:

```env
# Cloudinary Configuration
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=123456789012345
CLOUDINARY_API_SECRET=abcdefghijklmnopqrstuvwxyz
```

**Lưu ý:** Thay `your-cloud-name`, `123456789012345`, `abcdefghijklmnopqrstuvwxyz` bằng giá trị thật từ Dashboard.

---

## Bước 4: Test Local (5 phút)

### 4.1. Build lại project:

```bash
# Windows
.\mvnw clean install

# Mac/Linux
./mvnw clean install
```

### 4.2. Chạy application:

```bash
# Windows
.\mvnw spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

### 4.3. Test upload:

1. Mở trình duyệt: http://localhost:8080
2. Đăng nhập admin
3. Vào trang quản lý sản phẩm
4. Upload 1 ảnh sản phẩm
5. Kiểm tra log console:
   ```
   Uploading to Cloudinary...
   Image uploaded successfully: https://res.cloudinary.com/...
   ```

### 4.4. Verify trên Cloudinary:

1. Vào: https://console.cloudinary.com/console/media_library
2. Mở folder **products**
3. Thấy ảnh vừa upload ✅

---

## Bước 5: Deploy lên Render (10 phút)

### 5.1. Tạo Database trên Render:

**Option A: PostgreSQL (Khuyến nghị)**

1. Vào: https://dashboard.render.com
2. Click **New** → **PostgreSQL**
3. Điền:
   - Name: `fat-c-grocery-db`
   - Database: `java5_asm`
   - User: `java5_user`
   - Region: `Singapore` (gần VN)
   - Plan: **Free**
4. Click **Create Database**
5. Đợi 2-3 phút
6. Copy **Internal Database URL**:
   ```
   postgresql://java5_user:password@host:5432/java5_asm
   ```

**Lưu ý:** Cần đổi driver sang PostgreSQL:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

```yaml
# application.yml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
```

**Option B: External MariaDB**

Dùng [PlanetScale](https://planetscale.com) hoặc [Railway](https://railway.app) cho MariaDB.

### 5.2. Deploy Application:

1. Push code lên GitHub (nếu chưa)
2. Vào: https://dashboard.render.com
3. Click **New** → **Web Service**
4. Connect GitHub repository
5. Điền:
   - Name: `fat-c-grocery-store`
   - Region: `Singapore`
   - Branch: `main`
   - Build Command: `./mvnw clean package -DskipTests`
   - Start Command: `java -jar target/*.jar`
   - Plan: **Free**

### 5.3. Set Environment Variables:

Click **Environment** → **Add Environment Variable**:

```env
# Database (PostgreSQL)
SPRING_DATASOURCE_URL=postgresql://java5_user:password@host:5432/java5_asm
SPRING_DATASOURCE_USERNAME=java5_user
SPRING_DATASOURCE_PASSWORD=your-password

# Cloudinary
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=123456789012345
CLOUDINARY_API_SECRET=abcdefghijklmnopqrstuvwxyz

# Google OAuth2
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# JWT
JWT_SECRET=your-jwt-secret-minimum-32-characters
JWT_EXPIRATION=86400000

# Application
APP_BASE_URL=https://fat-c-grocery-store.onrender.com
SPRING_PROFILES_ACTIVE=prod

# Email (optional)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

### 5.4. Deploy:

1. Click **Create Web Service**
2. Đợi 5-10 phút build
3. Xem logs để check lỗi
4. Truy cập: `https://fat-c-grocery-store.onrender.com`

---

## Bước 6: Import Database (5 phút)

### 6.1. Connect vào PostgreSQL:

```bash
# Install psql (nếu chưa có)
# Windows: https://www.postgresql.org/download/windows/
# Mac: brew install postgresql

# Connect
psql "postgresql://java5_user:password@host:5432/java5_asm"
```

### 6.2. Import schema và data:

**Lưu ý:** File SQL hiện tại dùng MariaDB syntax, cần convert sang PostgreSQL:

```sql
-- Thay vì chạy file .sql, tạo bảng thủ công hoặc dùng Hibernate auto-create
-- Hoặc convert SQL syntax từ MariaDB sang PostgreSQL
```

**Cách nhanh:** Dùng Hibernate auto-create:

```yaml
# application-prod.yml (tạm thời)
spring:
  jpa:
    hibernate:
      ddl-auto: create  # Chỉ dùng lần đầu, sau đó đổi về 'none'
```

Sau khi tạo bảng xong, insert data thủ công qua pgAdmin hoặc SQL.

---

## Bước 7: Update Google OAuth2 Redirect URI

1. Vào: https://console.cloud.google.com/apis/credentials
2. Chọn OAuth 2.0 Client ID
3. Thêm **Authorized redirect URIs**:
   ```
   https://fat-c-grocery-store.onrender.com/login/oauth2/code/google
   ```
4. Save

---

## Troubleshooting

### Lỗi: "Cloudinary credentials not found"

**Nguyên nhân:** Chưa set environment variables trên Render.

**Giải pháp:** 
1. Vào Render Dashboard → Service → Environment
2. Kiểm tra lại `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`
3. Redeploy

### Lỗi: "Upload failed: 401 Unauthorized"

**Nguyên nhân:** API credentials sai.

**Giải pháp:**
1. Vào Cloudinary Dashboard
2. Copy lại API Key và API Secret
3. Update environment variables
4. Redeploy

### Lỗi: "Database connection failed"

**Nguyên nhân:** Database URL sai hoặc database chưa ready.

**Giải pháp:**
1. Kiểm tra `SPRING_DATASOURCE_URL` có đúng format không
2. Kiểm tra database đã running chưa
3. Test connection bằng psql

### Render Free Tier Sleep

**Vấn đề:** App sleep sau 15 phút không dùng, cold start ~30-60s.

**Giải pháp:**
- Upgrade lên Starter plan ($7/month) để không sleep
- Hoặc dùng [UptimeRobot](https://uptimerobot.com/) để ping app 5 phút/lần (giữ app awake)

---

## Chi phí dự kiến

| Service | Plan | Chi phí |
|---------|------|---------|
| Cloudinary | Free | $0 (25GB storage) |
| Render Web Service | Free | $0 (sleep sau 15p) |
| Render PostgreSQL | Free | $0 (1GB storage) |
| **Tổng** | | **$0/tháng** |

**Nếu cần production:**
- Render Starter: $7/month (no sleep)
- Render PostgreSQL Starter: $7/month (10GB)
- **Tổng: $14/month**

---

## Kết luận

Setup **Render + Cloudinary** cho phép bạn:
- ✅ Deploy miễn phí hoàn toàn
- ✅ Lưu ảnh bền vững
- ✅ Không cần thẻ tín dụng
- ✅ Đủ professional cho portfolio/demo

Nếu cần production thật sự, nâng cấp lên paid plan hoặc chuyển sang **Google Cloud Run**.

