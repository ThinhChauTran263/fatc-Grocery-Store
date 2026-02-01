# ⚡ TẠO ADMIN - SIÊU NHANH

## 🎯 Tài khoản admin mặc định

```
Username: admin
Email: admin@fatcgrocery.com  
Password: admin123
```

---

## 🚀 Cách 1: Thêm admin qua SQL (3 phút)

### Bước 1: Connect database
1. Vào: https://dashboard.render.com
2. Click database `fatc-grocery-db`
3. Tab **Connect** → Copy connection string
4. Dùng **DBeaver** hoặc **pgAdmin**

### Bước 2: Chạy SQL
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

### Bước 3: Đăng nhập
- URL: https://fatc-grocery-store.onrender.com/sign-in
- Username: `admin`
- Password: `admin123`

### Bước 4: ĐỔI MẬT KHẨU NGAY!

---

## 🔧 Cách 2: Nâng user thường lên admin (2 phút)

### Bước 1: Đăng ký tài khoản
Đăng ký tài khoản thường trên web

### Bước 2: Nâng cấp qua SQL
```sql
UPDATE users 
SET role = 'ADMIN' 
WHERE email = 'your-email@example.com';
```

### Bước 3: Đăng xuất và đăng nhập lại

---

## 🐛 Troubleshooting

### Không connect được database?
- Kiểm tra connection string
- Dùng **Internal Database URL** (không phải External)
- Kiểm tra firewall/VPN

### Không đăng nhập được?
```sql
-- Kiểm tra user có tồn tại không
SELECT * FROM users WHERE username = 'admin';

-- Reset password về admin123
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' 
WHERE username = 'admin';
```

---

## 📚 Chi tiết đầy đủ

Xem file: **[ADMIN_ACCOUNT_GUIDE.md](ADMIN_ACCOUNT_GUIDE.md)**

---

## ✅ Done!

Sau khi tạo admin, bạn có thể:
- Quản lý sản phẩm
- Upload ảnh lên Cloudinary
- Quản lý đơn hàng
- Xem thống kê
