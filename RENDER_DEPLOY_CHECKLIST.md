# ✅ CHECKLIST DEPLOY LÊN RENDER + CLOUDINARY

## 📋 Chuẩn bị trước khi deploy (15 phút)

### 1. Đăng ký Cloudinary
- [ ] Truy cập: https://cloudinary.com/users/register/free
- [ ] Đăng ký tài khoản (email + password)
- [ ] Verify email
- [ ] Vào Dashboard: https://console.cloudinary.com/
- [ ] Copy 3 giá trị:
  - Cloud name: `_______________`
  - API Key: `_______________`
  - API Secret: `_______________`

### 2. Update code local
- [ ] Mở file `.env`
- [ ] Set `USE_CLOUDINARY=true`
- [ ] Điền Cloudinary credentials vào `.env`
- [ ] Build project: `.\mvnw clean install`
- [ ] Test local: `.\mvnw spring-boot:run`
- [ ] Upload thử 1 ảnh sản phẩm
- [ ] Verify ảnh xuất hiện trên Cloudinary Dashboard

### 3. Push code lên GitHub
- [ ] `git add .`
- [ ] `git commit -m "Add Cloudinary integration"`
- [ ] `git push origin main`

---

## 🚀 Deploy lên Render (20 phút)

### Bước 1: Tạo Database

#### Option A: PostgreSQL (Khuyến nghị)

- [ ] Vào: https://dashboard.render.com
- [ ] Click **New** → **PostgreSQL**
- [ ] Điền thông tin:
  - Name: `fat-c-grocery-db`
  - Database: `java5_asm`
  - User: `java5_user`
  - Region: `Singapore`
  - Plan: **Free**
- [ ] Click **Create Database**
- [ ] Đợi database ready (~2 phút)
- [ ] Copy **Internal Database URL**
- [ ] Lưu URL vào notepad

**Lưu ý:** Cần update code để dùng PostgreSQL:

```xml
<!-- Thêm vào pom.xml -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

```yaml
# Update application.yml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
```

#### Option B: External MariaDB

- [ ] Đăng ký [Railway](https://railway.app) hoặc [PlanetScale](https://planetscale.com)
- [ ] Tạo MariaDB/MySQL database
- [ ] Copy connection string

### Bước 2: Deploy Application

- [ ] Vào: https://dashboard.render.com
- [ ] Click **New** → **Web Service**
- [ ] Click **Connect GitHub**
- [ ] Authorize Render
- [ ] Chọn repository: `your-repo-name`
- [ ] Điền thông tin:
  - Name: `fat-c-grocery-store`
  - Region: `Singapore`
  - Branch: `main`
  - Runtime: `Docker` (nếu dùng Dockerfile) hoặc `Java`
  - Build Command: `./mvnw clean package -DskipTests`
  - Start Command: `java -jar target/*.jar`
  - Plan: **Free**

### Bước 3: Set Environment Variables

Click **Environment** → **Add Environment Variable**, điền từng cặp:

```env
# Database
SPRING_DATASOURCE_URL=postgresql://java5_user:password@host:5432/java5_asm
SPRING_DATASOURCE_USERNAME=java5_user
SPRING_DATASOURCE_PASSWORD=your-password

# Cloudinary
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret

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

**Checklist:**
- [ ] Database URL đã điền
- [ ] Cloudinary credentials đã điền
- [ ] Google OAuth2 đã điền
- [ ] JWT secret đã điền (tối thiểu 32 ký tự)
- [ ] APP_BASE_URL đúng với URL Render

### Bước 4: Deploy

- [ ] Click **Create Web Service**
- [ ] Đợi build (~5-10 phút)
- [ ] Xem logs để check lỗi
- [ ] Truy cập URL: `https://fat-c-grocery-store.onrender.com`
- [ ] Kiểm tra app có chạy không

---

## 🔧 Post-Deploy Configuration (10 phút)

### 1. Import Database

**Nếu dùng PostgreSQL:**

```bash
# Connect vào database
psql "postgresql://java5_user:password@host:5432/java5_asm"

# Tạo bảng (dùng Hibernate hoặc import SQL)
```

**Cách nhanh:** Dùng Hibernate auto-create (chỉ lần đầu):

- [ ] Vào Render Dashboard → Service → Environment
- [ ] Thêm biến: `SPRING_JPA_HIBERNATE_DDL_AUTO=create`
- [ ] Redeploy
- [ ] Đợi app restart
- [ ] Xóa biến `SPRING_JPA_HIBERNATE_DDL_AUTO`
- [ ] Redeploy lại

### 2. Update Google OAuth2 Redirect URI

- [ ] Vào: https://console.cloud.google.com/apis/credentials
- [ ] Chọn OAuth 2.0 Client ID
- [ ] Thêm **Authorized redirect URIs**:
  ```
  https://fat-c-grocery-store.onrender.com/login/oauth2/code/google
  ```
- [ ] Click **Save**

### 3. Test Application

- [ ] Truy cập: `https://fat-c-grocery-store.onrender.com`
- [ ] Đăng ký tài khoản mới
- [ ] Đăng nhập
- [ ] Test Google OAuth2 login
- [ ] Đăng nhập admin
- [ ] Upload ảnh sản phẩm
- [ ] Verify ảnh hiển thị từ Cloudinary
- [ ] Test các chức năng chính:
  - [ ] Xem sản phẩm
  - [ ] Thêm vào giỏ hàng
  - [ ] Checkout
  - [ ] Xem đơn hàng

---

## 🐛 Troubleshooting

### App không start

**Kiểm tra:**
- [ ] Xem logs trên Render Dashboard
- [ ] Database URL có đúng không
- [ ] Environment variables đã set đủ chưa

### Upload ảnh bị lỗi

**Kiểm tra:**
- [ ] `USE_CLOUDINARY=true`
- [ ] Cloudinary credentials đúng chưa
- [ ] Xem logs: "Uploading to Cloudinary..."

### Google OAuth2 không hoạt động

**Kiểm tra:**
- [ ] Redirect URI đã thêm vào Google Console chưa
- [ ] `GOOGLE_CLIENT_ID` và `GOOGLE_CLIENT_SECRET` đúng chưa

### Database connection failed

**Kiểm tra:**
- [ ] Database đã running chưa
- [ ] Connection string đúng format chưa
- [ ] Username/password đúng chưa

---

## 📊 Monitoring

### Render Dashboard
- [ ] Bookmark: https://dashboard.render.com
- [ ] Check logs thường xuyên
- [ ] Monitor resource usage

### Cloudinary Dashboard
- [ ] Bookmark: https://console.cloudinary.com
- [ ] Check storage usage
- [ ] Check bandwidth usage

---

## 🎉 Hoàn thành!

Nếu tất cả checklist đều ✅, bạn đã deploy thành công:
- ✅ App chạy trên Render
- ✅ Database trên Render PostgreSQL
- ✅ Ảnh lưu trên Cloudinary
- ✅ Hoàn toàn miễn phí

**URL của bạn:** `https://fat-c-grocery-store.onrender.com`

**Lưu ý Free Tier:**
- App sẽ sleep sau 15 phút không dùng
- Cold start ~30-60 giây
- Upgrade lên Starter ($7/month) để không sleep

