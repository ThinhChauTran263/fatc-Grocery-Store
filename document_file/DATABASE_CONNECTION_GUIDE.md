# 🗄️ Hướng dẫn Kết nối Database

## ✅ Trạng thái hiện tại
- ✅ Docker container đã chạy: `coffee_shop_db`
- ✅ Database đã được khởi tạo: `java5_asm`
- ✅ Tất cả bảng đã được tạo (12 bảng)

## 📋 Thông tin kết nối

### Khi chạy Spring Boot từ IntelliJ/IDE:
```properties
Host: localhost
Port: 3307
Database: java5_asm
Username: java5_user
Password: java5_password
URL: jdbc:mariadb://localhost:3307/java5_asm
```

### Khi chạy Spring Boot từ Docker:
```properties
Host: mariadb
Port: 3306
Database: java5_asm
Username: java5_user
Password: java5_password
URL: jdbc:mariadb://mariadb:3306/java5_asm
```

## 🚀 Các lệnh Docker hữu ích

### Khởi động database:
```bash
docker-compose up -d
```

### Dừng database:
```bash
docker-compose down
```

### Xem logs:
```bash
docker logs coffee_shop_db
```

### Xem trạng thái:
```bash
docker ps
```

### Kết nối vào database:
```bash
docker exec -it coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm
```

### Kiểm tra các bảng:
```bash
docker exec coffee_shop_db mariadb -ujava5_user -pjava5_password java5_asm -e "SHOW TABLES;"
```

### Reset database (xóa dữ liệu):
```bash
docker-compose down -v
docker-compose up -d
```

## 🔧 Kết nối từ IntelliJ IDEA

### 1. Mở Database Tool (View → Tool Windows → Database)

### 2. Thêm Data Source mới:
- Click dấu `+` → Data Source → MariaDB
- Điền thông tin:
  - Host: `localhost`
  - Port: `3307`
  - Database: `java5_asm`
  - User: `java5_user`
  - Password: `java5_password`
- Click "Test Connection"
- Click "OK"

## 🔧 Kết nối từ DBeaver/MySQL Workbench

### DBeaver:
1. New Database Connection → MariaDB
2. Điền thông tin như trên
3. Test Connection → Finish

### MySQL Workbench:
1. New Connection
2. Connection Name: `Coffee Shop DB`
3. Hostname: `localhost`
4. Port: `3307`
5. Username: `java5_user`
6. Password: `java5_password`
7. Test Connection → OK

## ⚠️ Xử lý lỗi thường gặp

### Lỗi: "Can't connect to MySQL server"
**Giải pháp:**
```bash
# Kiểm tra Docker đang chạy
docker ps

# Nếu không thấy container, khởi động lại
docker-compose up -d

# Đợi 10-15 giây để database khởi động
```

### Lỗi: "Access denied for user"
**Giải pháp:**
- Kiểm tra username/password trong file `.env`
- Đảm bảo sử dụng đúng thông tin:
  - Username: `java5_user`
  - Password: `java5_password`

### Lỗi: "Unknown database 'java5_asm'"
**Giải pháp:**
```bash
# Reset database
docker-compose down -v
docker-compose up -d
```

### Lỗi: Port 3307 đã được sử dụng
**Giải pháp:**
```bash
# Tìm process đang dùng port 3307
netstat -ano | findstr :3307

# Hoặc đổi port trong docker-compose.yml
# Thay "3307:3306" thành "3308:3306"
```

## 📊 Danh sách các bảng trong database

1. `addresses` - Địa chỉ giao hàng
2. `brands` - Thương hiệu sản phẩm
3. `cart_items` - Chi tiết giỏ hàng
4. `carts` - Giỏ hàng
5. `categories` - Danh mục sản phẩm
6. `order_items` - Chi tiết đơn hàng
7. `orders` - Đơn hàng
8. `products` - Sản phẩm
9. `reviews` - Đánh giá sản phẩm
10. `user_activity_logs` - Lịch sử hoạt động
11. `users` - Người dùng
12. `wishlists` - Danh sách yêu thích

## 🎯 Chạy ứng dụng Spring Boot

### Từ IntelliJ:
1. Đảm bảo Docker đang chạy: `docker ps`
2. Mở file `Java5AsmApplication.java`
3. Click nút Run (hoặc Shift+F10)
4. Truy cập: http://localhost:8080

### Kiểm tra kết nối:
- Nếu ứng dụng khởi động thành công → Kết nối OK ✅
- Nếu có lỗi "Connection refused" → Kiểm tra Docker
- Nếu có lỗi "Access denied" → Kiểm tra username/password

## 📝 Ghi chú quan trọng

1. **Port mapping**: Docker expose port `3307` ra ngoài (không phải 3306)
2. **File .env**: Đã được tạo với cấu hình mặc định
3. **Data persistence**: Dữ liệu được lưu trong thư mục `./mariadb_data`
4. **Init scripts**: Các file SQL trong `./mariadb_init` sẽ tự động chạy khi khởi tạo database lần đầu

## 🔐 Thông tin Root (Admin)

```properties
Username: root
Password: root_password_123
```

**Chỉ dùng khi cần quyền admin để:**
- Tạo database mới
- Tạo user mới
- Thay đổi cấu hình database
