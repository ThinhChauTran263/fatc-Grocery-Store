# Java5 Thymeleaf ASM - Grocery Store

🛒 **Website bán cafe/grocery store** được xây dựng bằng Spring Boot + Thymeleaf

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📚 TÀI LIỆU DỰ ÁN

> **🎯 BẮT ĐẦU TẠI ĐÂY:**
> - **[📖 START_HERE.md](START_HERE.md)** - Hướng dẫn bắt đầu nhanh ⭐
> - **[📖 PROJECT_DOCUMENTATION.md](document_file/PROJECT_DOCUMENTATION.md)** - Tài liệu tổng hợp đầy đủ
> - **[🚀 DEPLOYMENT_GUIDE.md](document_file/DEPLOYMENT_GUIDE.md)** - Hướng dẫn deploy lên Render

---

## 📑 Mục lục

- [Mô tả dự án](#-mô-tả-dự-án)
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Tính năng](#-tính-năng)
- [Deploy lên Render](#-deploy-lên-render-30-phút)
- [Cài đặt và chạy LOCAL](#-cài-đặt-và-chạy-local)
- [Cấu trúc dự án](#-cấu-trúc-dự-án)
- [Database Schema](#️-database-schema)
- [API Endpoints](#-api-endpoints)
- [SCSS Structure](#-scss-structure)
- [Screenshots](#-screenshots)
- [Testing](#-testing)
- [Security Features](#-security-features)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

---

## 📋 Mô tả dự án

Dự án Java 5 Assignment - Website thương mại điện tử bán cafe và grocery với giao diện hiện đại, responsive và đầy đủ tính năng. Hệ thống được xây dựng theo kiến trúc modular, dễ bảo trì và mở rộng.

## 🚀 Công nghệ sử dụng

### Backend
- **Spring Boot 4.0.1** - Framework chính
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM và database access
- **Thymeleaf** - Template engine
- **MariaDB / PostgreSQL** - Database (support cả 2)
- **Cloudinary** - Cloud storage cho images
- **JavaMail** - Email service
- **Lombok** - Giảm boilerplate code
- **Maven** - Build tool

### Frontend
- **HTML5 + CSS3** - Markup và styling
- **JavaScript (Vanilla)** - Interactive features
- **SCSS** - CSS preprocessor
- **Bootstrap Icons** - Icon library
- **Responsive Design** - Mobile-first approach

### DevOps & Tools
- **Docker & Docker Compose** - Containerization
- **Git** - Version control
- **Render** - Cloud deployment platform

## 📁 Cấu trúc dự án

```
java5_asm/
├── src/main/
│   ├── java/poly/edu/java5_asm/
│   │   ├── common/              # Common utilities
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── security/        # Security config
│   │   │   └── util/            # Utility classes
│   │   │
│   │   ├── module/              # Feature modules
│   │   │   ├── address/         # Address management
│   │   │   ├── admin/           # Admin panel
│   │   │   ├── auth/            # Authentication
│   │   │   ├── brand/           # Brand management
│   │   │   ├── caffeine/        # Caffeine cache
│   │   │   ├── cart/            # Shopping cart
│   │   │   ├── category/        # Category management
│   │   │   ├── email/           # Email service
│   │   │   ├── order/           # Order management
│   │   │   ├── payment/         # Payment processing
│   │   │   ├── product/         # Product management
│   │   │   ├── review/          # Product reviews
│   │   │   ├── user/            # User management
│   │   │   └── wishlist/        # Wishlist feature
│   │   │
│   │   └── Java5AsmApplication.java
│   │
│   └── resources/
│       ├── static/assets/
│       │   ├── css/             # Compiled CSS
│       │   ├── js/              # JavaScript files
│       │   ├── img/             # Images
│       │   ├── icon/            # SVG icons
│       │   ├── fonts/           # Web fonts
│       │   └── favicon/         # Favicon files
│       │
│       ├── templates/
│       │   ├── module/          # Module templates
│       │   │   ├── address/
│       │   │   ├── auth/
│       │   │   ├── cart/
│       │   │   ├── order/
│       │   │   ├── payment/
│       │   │   ├── product/
│       │   │   ├── user/
│       │   │   └── wishlist/
│       │   │
│       │   ├── shared/          # Shared templates
│       │   │   ├── admin/
│       │   │   ├── email/
│       │   │   └── fragments/
│       │   │
│       │   └── index.html
│       │
│       ├── scss/                # SCSS source files
│       ├── application.yml      # Main config
│       └── application-prod.yml # Production config
│
├── mariadb_init/                # Database init scripts
├── document_file/               # Project documentation
├── docker-compose.yml           # Docker configuration
├── Dockerfile                   # Docker image
├── pom.xml                      # Maven configuration
└── README.md
```

## ✨ Tính năng

### 🔐 Authentication & Security
- [x] Đăng ký tài khoản với email verification
- [x] Đăng nhập với Spring Security
- [x] Quên mật khẩu & reset password
- [x] Đổi mật khẩu an toàn
- [x] Session management
- [x] Role-based access control (USER/ADMIN)
- [x] CSRF protection

### 🛍️ Shopping Experience
- [x] Xem danh sách sản phẩm với pagination
- [x] Xem chi tiết sản phẩm
- [x] Tìm kiếm sản phẩm (full-text search)
- [x] Lọc theo danh mục, thương hiệu, giá
- [x] Sắp xếp sản phẩm (giá, tên, mới nhất)
- [x] Giỏ hàng với AJAX
- [x] Thanh toán đơn hàng
- [x] Áp dụng mã giảm giá (promo code)
- [x] Danh sách yêu thích
- [x] Đánh giá & review sản phẩm

### 👤 User Management
- [x] Xem & chỉnh sửa thông tin cá nhân
- [x] Upload avatar (Cloudinary)
- [x] Quản lý địa chỉ giao hàng (CRUD)
- [x] Quản lý phương thức thanh toán
- [x] Lịch sử đơn hàng
- [x] Theo dõi trạng thái đơn hàng
- [x] User activity logs

### 👨‍💼 Admin Panel
- [x] Dashboard với thống kê
- [x] Quản lý sản phẩm (CRUD)
- [x] Quản lý danh mục
- [x] Quản lý thương hiệu
- [x] Quản lý đơn hàng
- [x] Quản lý người dùng
- [x] Quản lý mã giảm giá
- [x] Xem báo cáo doanh thu

### 🎨 UI/UX
- [x] Responsive design (Mobile, Tablet, Desktop)
- [x] Dark mode / Light mode toggle
- [x] Smooth animations & transitions
- [x] Interactive dropdowns
- [x] Image slideshow
- [x] Product carousel
- [x] Toast notifications
- [x] Loading states
- [x] Form validation

### 📧 Email Service
- [x] Welcome email
- [x] Order confirmation
- [x] Password reset email
- [x] HTML email templates

### ⚡ Performance
- [x] Caffeine cache cho products
- [x] Lazy loading images
- [x] Optimized database queries
- [x] Connection pooling

## 🚀 DEPLOY LÊN RENDER (30 phút)

> **📖 Xem hướng dẫn chi tiết:** [DEPLOYMENT_GUIDE.md](document_file/DEPLOYMENT_GUIDE.md)

**Quick Start:**
1. Đăng ký Cloudinary: https://cloudinary.com/users/register/free
2. Đăng ký Render: https://dashboard.render.com
3. Tạo PostgreSQL database trên Render
4. Deploy web service từ GitHub repository
5. Thêm environment variables (xem file `.env.example`)
6. Chờ build & deploy hoàn tất
7. Done! 🎉

**Tài liệu liên quan:**
- [CLOUDINARY_SETUP_GUIDE.md](document_file/CLOUDINARY_SETUP_GUIDE.md) - Hướng dẫn setup Cloudinary
- [EMAIL_SETUP_GUIDE.md](document_file/EMAIL_SETUP_GUIDE.md) - Hướng dẫn setup email service

---

## 🔧 Cài đặt và chạy LOCAL

### Yêu cầu
- Java 21+
- Maven 3.8+
- MariaDB 10.6+
- Docker (optional)

### 1. Clone repository
```bash
git clone https://github.com/zika167/Java5_Thymeleaf_ASM.git
cd Java5_Thymeleaf_ASM
```

### 2. Cấu hình database

#### Option A: Sử dụng Docker (Khuyến nghị)
```bash
# Start MariaDB container với init scripts
docker-compose up -d

# Kiểm tra logs
docker-compose logs -f mariadb
```

Database sẽ tự động được khởi tạo với:
- Database: `java5_asm`
- Username: `root`
- Password: `123456`
- Port: `3306`
- Init scripts từ folder `mariadb_init/`

#### Option B: Cài đặt MariaDB thủ công
1. Cài đặt MariaDB 10.6+
2. Tạo database:
```sql
CREATE DATABASE java5_asm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Import database schema và data:
```bash
mysql -u root -p java5_asm < mariadb_init/01-schema.sql
mysql -u root -p java5_asm < mariadb_init/02-data.sql
mysql -u root -p java5_asm < mariadb_init/03-add-promo-code.sql
mysql -u root -p java5_asm < mariadb_init/04-add-order-discount.sql
```

4. Cập nhật `src/main/resources/application.yml` nếu cần:
```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/java5_asm
    username: root
    password: your_password
```

### 2.1. Cấu hình Cloudinary (Optional cho local)
Tạo file `.env` từ `.env.example`:
```bash
cp .env.example .env
```

Cập nhật thông tin Cloudinary trong `.env`:
```properties
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

### 2.2. Cấu hình Email (Optional cho local)
Cập nhật trong `application.yml`:
```yaml
spring:
  mail:
    username: your_email@gmail.com
    password: your_app_password
```

### 3. Build và chạy
```bash
# Build project
./mvnw clean install

# Run application
./mvnw spring-boot:run
```

### 4. Truy cập ứng dụng
Mở trình duyệt và truy cập: `http://localhost:8080`

**Tài khoản mặc định:**
- Admin: `admin` / `admin123`
- User: `user` / `user123`

### 5. Compile SCSS (Optional)
Nếu muốn chỉnh sửa styles:
```bash
cd src/main/resources/scss
npm install
npm run build
```

## 📸 Screenshots

### Trang chủ
- Hero slideshow với sản phẩm nổi bật
- Danh mục sản phẩm
- Sản phẩm mới nhất

### Trang sản phẩm
- Lọc theo danh mục, thương hiệu, giá
- Sắp xếp sản phẩm
- Pagination
- Add to cart & wishlist

### Chi tiết sản phẩm
- Thông tin chi tiết sản phẩm
- Đánh giá & reviews
- Sản phẩm liên quan

### Giỏ hàng & Thanh toán
- Quản lý giỏ hàng
- Áp dụng mã giảm giá
- Chọn địa chỉ giao hàng
- Chọn phương thức thanh toán

### Trang cá nhân
- Thông tin cá nhân
- Quản lý địa chỉ
- Quản lý thẻ thanh toán
- Lịch sử đơn hàng
- Danh sách yêu thích

### Admin Panel
- Dashboard với thống kê
- Quản lý sản phẩm, danh mục, thương hiệu
- Quản lý đơn hàng
- Quản lý người dùng

## 🗂️ Database Schema

Dự án sử dụng 13 bảng chính:

### Core Tables
- **users** - Thông tin người dùng
- **products** - Sản phẩm
- **categories** - Danh mục sản phẩm
- **brands** - Thương hiệu

### Shopping Tables
- **carts** - Giỏ hàng
- **cart_items** - Chi tiết giỏ hàng
- **orders** - Đơn hàng
- **order_items** - Chi tiết đơn hàng
- **wishlists** - Danh sách yêu thích

### User Related Tables
- **addresses** - Địa chỉ giao hàng
- **reviews** - Đánh giá sản phẩm
- **user_activity_logs** - Lịch sử hoạt động

### Database Scripts
Xem chi tiết schema tại:
- `mariadb_init/01-schema.sql` - Database schema
- `mariadb_init/02-data.sql` - Sample data
- `mariadb_init/03-add-promo-code.sql` - Promo code feature
- `mariadb_init/04-add-order-discount.sql` - Order discount feature

**ERD Diagram:** Xem file `document_file/PROJECT_DOCUMENTATION.md` để xem ERD chi tiết

## 📝 API Endpoints

### Public Endpoints
- `GET /` - Trang chủ
- `GET /products` - Danh sách sản phẩm
- `GET /products/{id}` - Chi tiết sản phẩm
- `GET /auth/sign-in` - Đăng nhập
- `GET /auth/sign-up` - Đăng ký
- `GET /auth/forgot-password` - Quên mật khẩu

### User Endpoints (Requires Authentication)
- `GET /user/profile` - Thông tin cá nhân
- `GET /user/addresses` - Quản lý địa chỉ
- `GET /user/orders` - Lịch sử đơn hàng
- `GET /cart` - Giỏ hàng
- `GET /checkout` - Thanh toán
- `GET /wishlist` - Danh sách yêu thích

### Admin Endpoints (Requires ADMIN role)
- `GET /admin/dashboard` - Dashboard
- `GET /admin/products` - Quản lý sản phẩm
- `GET /admin/categories` - Quản lý danh mục
- `GET /admin/brands` - Quản lý thương hiệu
- `GET /admin/orders` - Quản lý đơn hàng
- `GET /admin/users` - Quản lý người dùng
- `GET /admin/statistics` - Thống kê

### REST API Endpoints
- `GET /api/products` - Lấy danh sách sản phẩm (JSON)
- `POST /api/cart/add` - Thêm vào giỏ hàng
- `PUT /api/cart/update` - Cập nhật giỏ hàng
- `DELETE /api/cart/remove/{id}` - Xóa khỏi giỏ hàng
- `POST /api/wishlist/toggle` - Toggle wishlist

**Chi tiết API:** Xem file `document_file/API_ENDPOINTS_REPORT.md`

## 🎨 SCSS Structure

```
scss/
├── abstracts/          # Variables, mixins, functions
│   ├── _mixins.scss
│   └── _index.scss
├── base/              # Reset, base styles, typography
│   ├── _reset.scss
│   ├── _base.scss
│   ├── _grid.scss
│   ├── _animation.scss
│   ├── _utils.scss
│   └── _index.scss
├── components/        # Reusable components
│   ├── _buttons.scss
│   ├── _forms.scss
│   ├── _product-card.scss
│   ├── _modal.scss
│   ├── _dropdown.scss
│   └── _index.scss
├── layout/            # Layout components
│   ├── _header.scss
│   ├── _footer.scss
│   ├── _sidebar.scss
│   └── _index.scss
├── pages/             # Page-specific styles
│   ├── _home.scss
│   ├── _auth.scss
│   ├── _product-detail.scss
│   ├── _checkout.scss
│   ├── _profile.scss
│   └── _index.scss
├── _theme.scss        # Theme variables (light/dark)
└── main.scss          # Main entry point

# Build command
npm run build          # Compile SCSS to CSS
npm run watch          # Watch for changes
```

## 🔄 Git Workflow

```bash
# Tạo branch mới
git checkout -b feature/ten-tinh-nang

# Commit changes
git add .
git commit -m "Add: mô tả thay đổi"

# Push lên GitHub
git push origin feature/ten-tinh-nang

# Tạo Pull Request trên GitHub
```

## 📚 Tài liệu tham khảo

### Documentation
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [MariaDB Documentation](https://mariadb.org/documentation/)

### Project Documentation
- [PROJECT_DOCUMENTATION.md](document_file/PROJECT_DOCUMENTATION.md) - Tài liệu tổng hợp
- [API_ENDPOINTS_REPORT.md](document_file/API_ENDPOINTS_REPORT.md) - Danh sách API endpoints
- [CART_API_GUIDE.md](document_file/CART_API_GUIDE.md) - Hướng dẫn Cart API
- [PROMO_CODE_GUIDE.md](document_file/PROMO_CODE_GUIDE.md) - Hướng dẫn Promo Code
- [ADMIN_STATISTICS_API.md](document_file/ADMIN_STATISTICS_API.md) - Admin Statistics API
- [TEST_SCENARIO_FULL_PROJECT.md](document_file/TEST_SCENARIO_FULL_PROJECT.md) - Test scenarios

## 🧪 Testing

### Manual Testing
Xem chi tiết test scenarios tại: `document_file/TEST_SCENARIO_FULL_PROJECT.md`

### Postman Collection
Import Postman collection để test API:
- Xem hướng dẫn: `document_file/POSTMAN_API_TESTING.md`

### Test Accounts
```
Admin:
- Username: admin
- Password: admin123

User:
- Username: user
- Password: user123
```

## 🔒 Security Features

- **Password Encryption**: BCrypt hashing
- **CSRF Protection**: Enabled for all POST requests
- **Session Management**: Secure session handling
- **Role-based Access Control**: USER/ADMIN roles
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **XSS Protection**: Thymeleaf auto-escaping
- **Secure Password Reset**: Token-based reset flow

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Kiểm tra MariaDB đang chạy
docker-compose ps

# Xem logs
docker-compose logs mariadb

# Restart database
docker-compose restart mariadb
```

### Port Already in Use
```bash
# Tìm process đang dùng port 8080
netstat -ano | findstr :8080

# Kill process (Windows)
taskkill /PID <PID> /F
```

### SCSS Compilation Issues
```bash
cd src/main/resources/scss
npm install
npm run build
```

## 📈 Performance Optimization

- **Caffeine Cache**: Cache products để giảm database queries
- **Connection Pooling**: HikariCP connection pool
- **Lazy Loading**: JPA lazy loading cho relationships
- **Image Optimization**: Cloudinary auto-optimization
- **Static Resources**: Cached static assets
- **Database Indexing**: Indexed foreign keys và search columns

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 👥 Tác giả

- **GitHub:** [@zika167](https://github.com/zika167)
- **Project:** Java5 Thymeleaf ASM - Grocery Store

## 🌟 Acknowledgments

- Frontend template inspiration: F8 Project 08
- Icons: Custom SVG icons
- Fonts: Gordita font family
- Spring Boot community
- Thymeleaf community

## 📞 Support

Nếu bạn gặp vấn đề hoặc có câu hỏi:
1. Kiểm tra [Issues](https://github.com/zika167/Java5_Thymeleaf_ASM/issues)
2. Đọc tài liệu trong folder `document_file/`
3. Tạo issue mới nếu cần

## 🔗 Links

- **Repository:** https://github.com/zika167/Java5_Thymeleaf_ASM
- **Documentation:** [document_file/](document_file/)
- **License:** [MIT License](LICENSE)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Frontend template: F8 Project 08
- Icons: Custom SVG icons
- Fonts: Gordita font family

---

⭐ **Nếu bạn thấy project hữu ích, hãy cho một star nhé!** ⭐
