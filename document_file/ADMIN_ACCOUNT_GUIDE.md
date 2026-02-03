# 🔐 HƯỚNG DẪN TẠO TÀI KHOẢN ADMIN

## 📋 Tài khoản Admin mặc định

Sau khi deploy và khởi tạo database, bạn sẽ có sẵn tài khoản admin:

```
Username: admin
Email: admin@fatcgrocery.com
Password: admin123
```

**⚠️ LƯU Ý:** Đổi mật khẩu ngay sau khi đăng nhập lần đầu!

---

## 🚀 Cách 1: Sử dụng tài khoản admin có sẵn (Khuyến nghị)

### Bước 1: Khởi tạo database với admin
Khi bạn chạy `SPRING_JPA_HIBERNATE_DDL_AUTO=create`, database sẽ tự động tạo bảng.

### Bước 2: Thêm admin user
Có 2 cách:

#### Option A: Tự động (đã có trong postgresql-schema.sql)
File `mariadb_init/postgresql-schema.sql` đã có sẵn admin user. Nếu bạn import file này:

```sql
-- Password: admin123 (bcrypt hashed)
INSERT INTO users (username, email, password, full_name, role, is_active)
VALUES ('admin', 'admin@fatcgrocery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 'ADMIN', TRUE);
```

#### Option B: Thêm thủ công qua Render Dashboard

1. Vào Render Dashboard: https://dashboard.render.com
2. Click vào database `fatc-grocery-db`
3. Click tab **Connect** → Copy connection string
4. Dùng tool như **DBeaver** hoặc **pgAdmin** để connect
5. Chạy SQL sau:

```sql
INSERT INTO users (username, email, password, full_name, role, is_active, created_at, updated_at)
VALUES (
    'admin',
    'admin@fatcgrocery.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Administrator',
    'ADMIN',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
```

**Password:** `admin123`

### Bước 3: Đăng nhập
1. Truy cập: https://fatc-grocery-store.onrender.com/sign-in
2. Nhập:
   - Username: `admin`
   - Password: `admin123`
3. Click **Đăng nhập**

### Bước 4: Đổi mật khẩu (QUAN TRỌNG!)
1. Vào **Profile** → **Đổi mật khẩu**
2. Đổi sang mật khẩu mạnh hơn

---

## 🔧 Cách 2: Tạo admin mới từ user thường

### Bước 1: Đăng ký tài khoản thường
1. Truy cập: https://fatc-grocery-store.onrender.com/sign-up
2. Đăng ký tài khoản mới

### Bước 2: Nâng cấp lên admin qua database
1. Connect vào PostgreSQL database (xem Cách 1, Option B)
2. Chạy SQL:

```sql
-- Tìm user ID
SELECT id, username, email, role FROM users WHERE email = 'your-email@example.com';

-- Nâng cấp lên ADMIN
UPDATE users 
SET role = 'ADMIN' 
WHERE email = 'your-email@example.com';

-- Verify
SELECT id, username, email, role FROM users WHERE email = 'your-email@example.com';
```

### Bước 3: Đăng xuất và đăng nhập lại
Để role mới có hiệu lực.

---

## 🛠️ Cách 3: Tạo script tự động (Advanced)

Tạo file `create-admin.sql`:

```sql
-- Create admin user if not exists
INSERT INTO users (username, email, password, full_name, role, is_active, created_at, updated_at)
SELECT 
    'admin',
    'admin@fatcgrocery.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Administrator',
    'ADMIN',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'admin'
);
```

Chạy script này sau khi khởi tạo database.

---

## 🔐 Tạo mật khẩu mới (nếu cần)

Nếu bạn muốn tạo mật khẩu khác `admin123`:

### Cách 1: Dùng online tool
1. Vào: https://bcrypt-generator.com/
2. Nhập mật khẩu mới
3. Rounds: `10`
4. Click **Generate**
5. Copy hash và thay vào SQL

### Cách 2: Dùng Java code
```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "your-new-password";
        String hash = encoder.encode(password);
        System.out.println("Hash: " + hash);
    }
}
```

---

## 📊 Kiểm tra admin đã tạo thành công

### Qua SQL:
```sql
SELECT id, username, email, role, is_active, created_at 
FROM users 
WHERE role = 'ADMIN';
```

### Qua Web:
1. Đăng nhập với tài khoản admin
2. Kiểm tra có menu **Admin Dashboard** không
3. Truy cập: https://fatc-grocery-store.onrender.com/admin

---

## 🎯 Quyền của Admin

Sau khi đăng nhập với tài khoản admin, bạn có thể:

- ✅ Quản lý sản phẩm (thêm, sửa, xóa)
- ✅ Quản lý danh mục
- ✅ Quản lý thương hiệu
- ✅ Quản lý đơn hàng
- ✅ Quản lý người dùng
- ✅ Xem thống kê
- ✅ Upload ảnh lên Cloudinary

---

## ⚠️ BẢO MẬT

### Sau khi tạo admin:
1. ✅ Đổi mật khẩu mặc định ngay lập tức
2. ✅ Sử dụng mật khẩu mạnh (ít nhất 12 ký tự)
3. ✅ Không chia sẻ thông tin đăng nhập
4. ✅ Đổi email admin nếu cần
5. ✅ Xóa tài khoản admin test sau khi tạo admin thật

### Đổi email admin:
```sql
UPDATE users 
SET email = 'your-real-email@example.com' 
WHERE username = 'admin';
```

---

## 🐛 Troubleshooting

### Không đăng nhập được?
- Kiểm tra username/password có đúng không
- Kiểm tra `is_active = TRUE` trong database
- Xem logs trên Render Dashboard

### Không có quyền admin?
- Kiểm tra `role = 'ADMIN'` trong database
- Đăng xuất và đăng nhập lại
- Clear browser cache

### Quên mật khẩu admin?
Reset qua database:
```sql
-- Reset về admin123
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' 
WHERE username = 'admin';
```

---

## 📞 Tóm tắt nhanh

**Cách nhanh nhất:**

1. Sau khi deploy xong
2. Connect vào PostgreSQL database
3. Chạy SQL:
```sql
INSERT INTO users (username, email, password, full_name, role, is_active, created_at, updated_at)
VALUES ('admin', 'admin@fatcgrocery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```
4. Đăng nhập với `admin` / `admin123`
5. Đổi mật khẩu ngay!

**Done! 🎉**
