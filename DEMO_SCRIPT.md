# 🎬 KỊCH BẢN DEMO WEBSITE GROCERY STORE

> **Thời gian demo:** 15-20 phút  
> **Người thực hiện:** [Tên của bạn]  
> **Ngày:** [Ngày demo]  
> **Dự án:** Java5 Thymeleaf ASM - Grocery Store

---

## 📋 CHUẨN BỊ TRƯỚC KHI DEMO

### ✅ Checklist
- [ ] Khởi động ứng dụng: `./mvnw spring-boot:run`
- [ ] Kiểm tra database đã có dữ liệu mẫu
- [ ] Mở trình duyệt ở chế độ ẩn danh (Incognito)
- [ ] Chuẩn bị 2 tab: User và Admin
- [ ] Test tài khoản đăng nhập trước
- [ ] Đóng các ứng dụng không cần thiết
- [ ] Zoom trình duyệt 100% hoặc 110%

### 🔑 Tài khoản demo
```
Admin Account:
- Username: admin
- Password: admin123

User Account:
- Username: user
- Password: user123
```

### 🌐 URLs quan trọng
```
Trang chủ:        http://localhost:8080/
Admin Dashboard:  http://localhost:8080/admin/dashboard
User Profile:     http://localhost:8080/user/profile
```

---

## 🎯 PHẦN 1: GIỚI THIỆU TỔNG QUAN (2 phút)

### Script:

> "Xin chào mọi người! Hôm nay tôi xin giới thiệu dự án **Grocery Store** - 
> một website thương mại điện tử bán cafe và tạp hóa trực tuyến.
> 
> Dự án được xây dựng bằng **Spring Boot 4.0.1** và **Thymeleaf**, 
> với giao diện responsive, hiện đại và đầy đủ tính năng.
> 
> Trong 15 phút tới, tôi sẽ demo các tính năng chính của hệ thống 
> từ góc độ người dùng và quản trị viên."

### Điểm nhấn:
- ✅ Spring Boot 4.0.1 + Thymeleaf
- ✅ MariaDB/PostgreSQL database
- ✅ Spring Security authentication
- ✅ Responsive design (Mobile/Tablet/Desktop)
- ✅ Dark/Light mode
- ✅ RESTful API
- ✅ Email service
- ✅ Cloudinary image storage
- ✅ Payment integration (VNPay, Momo)

---

## 🛍️ PHẦN 2: TRẢI NGHIỆM NGƯỜI DÙNG (8 phút)

### 2.1. Trang chủ & Khám phá sản phẩm (1.5 phút)

**Action:**
1. Mở trang chủ: `http://localhost:8080/`
2. Scroll từ trên xuống dưới

**Script:**
> "Đây là trang chủ của website. Chúng ta có:
> - **Hero slideshow** với các sản phẩm nổi bật
> - **Danh mục sản phẩm** được hiển thị rõ ràng
> - **Sản phẩm mới nhất** với hình ảnh đẹp mắt
> - **Header** với thanh tìm kiếm, giỏ hàng, và menu người dùng
> - **Footer** với thông tin liên hệ và links hữu ích"

**Điểm nhấn:**
- ✨ Giao diện hiện đại, responsive
- ✨ Smooth animations
- ✨ Dark/Light mode toggle (demo nếu có)

---

### 2.2. Đăng ký tài khoản (1.5 phút)

**Action:**
1. Click "Đăng ký" hoặc vào: `http://localhost:8080/auth/sign-up`
2. Điền form đăng ký

**Script:**
> "Để sử dụng đầy đủ tính năng, người dùng cần đăng ký tài khoản.
> Form đăng ký có validation đầy đủ."

**Demo form:**
```
Username:     demo_user_[timestamp]
Email:        demo@example.com
Password:     Demo123456
Full Name:    Demo User
Phone:        0123456789
```

**Script tiếp:**
> "Sau khi đăng ký thành công, hệ thống sẽ gửi email chào mừng 
> và chuyển hướng đến trang đăng nhập."

**Điểm nhấn:**
- ✅ Form validation (username, email format, password strength)
- ✅ Error messages rõ ràng
- ✅ Email verification (nếu có)

---

### 2.3. Đăng nhập (1 phút)

**Action:**
1. Vào trang đăng nhập: `http://localhost:8080/auth/sign-in`
2. Đăng nhập với tài khoản user

**Script:**
> "Đăng nhập với tài khoản vừa tạo hoặc tài khoản có sẵn.
> Hệ thống sử dụng Spring Security để bảo mật."

**Login:**
```
Username: user
Password: user123
```

**Script tiếp:**
> "Sau khi đăng nhập thành công, chúng ta có thể thấy tên người dùng 
> xuất hiện ở góc phải header, và có thể truy cập các tính năng cá nhân."

**Điểm nhấn:**
- 🔒 Spring Security authentication
- 🔒 Session management
- 🔒 Remember me (nếu có)

---

### 2.4. Tìm kiếm & Lọc sản phẩm (1.5 phút)

**Action:**
1. Click vào "Sản phẩm" hoặc một danh mục
2. Vào: `http://localhost:8080/products`

**Script:**
> "Trang sản phẩm có đầy đủ tính năng tìm kiếm và lọc."

**Demo các tính năng:**
1. **Tìm kiếm:** Gõ "coffee" vào thanh search
2. **Lọc theo danh mục:** Chọn "Coffee"
3. **Lọc theo thương hiệu:** Chọn một brand
4. **Lọc theo giá:** Chọn khoảng giá
5. **Sắp xếp:** Chọn "Giá thấp đến cao"

**Script tiếp:**
> "Kết quả được cập nhật real-time với AJAX, không cần reload trang.
> Có pagination để dễ dàng xem nhiều sản phẩm."

**Điểm nhấn:**
- 🔍 Full-text search
- 🔍 Multiple filters (category, brand, price)
- 🔍 Sort options
- 🔍 Pagination
- ⚡ AJAX loading

---

### 2.5. Chi tiết sản phẩm & Thêm vào giỏ (1.5 phút)

**Action:**
1. Click vào một sản phẩm
2. Xem chi tiết

**Script:**
> "Trang chi tiết sản phẩm hiển thị đầy đủ thông tin."

**Demo:**
1. Xem hình ảnh sản phẩm (có thể zoom nếu có)
2. Đọc mô tả chi tiết
3. Xem giá và tình trạng còn hàng
4. Chọn số lượng (tăng/giảm)
5. Click "Thêm vào giỏ hàng"

**Script tiếp:**
> "Khi thêm vào giỏ, có notification hiển thị và số lượng 
> trên icon giỏ hàng được cập nhật ngay lập tức."

**Demo thêm:**
6. Click icon "Yêu thích" (Wishlist)
7. Scroll xuống xem **Reviews/Đánh giá**
8. Xem **Sản phẩm liên quan**

**Điểm nhấn:**
- 🛒 Add to cart với AJAX
- ❤️ Add to wishlist
- ⭐ Product reviews & ratings
- 📦 Stock availability
- 🔗 Related products

---

### 2.6. Giỏ hàng (1 phút)

**Action:**
1. Click icon giỏ hàng hoặc vào: `http://localhost:8080/cart`

**Script:**
> "Giỏ hàng hiển thị tất cả sản phẩm đã thêm."

**Demo:**
1. Xem danh sách sản phẩm
2. Cập nhật số lượng (+/-)
3. Xóa một sản phẩm
4. Xem tổng tiền tự động cập nhật
5. Nhập mã giảm giá (nếu có): `SUMMER2024`

**Script tiếp:**
> "Giỏ hàng được lưu trong session, và cập nhật real-time 
> khi thay đổi số lượng. Có thể áp dụng mã giảm giá."

**Điểm nhấn:**
- 🛒 Real-time cart updates
- 💰 Auto-calculate total
- 🎟️ Promo code support
- 🗑️ Remove items
- 💾 Session persistence

---

### 2.7. Thanh toán (1.5 phút)

**Action:**
1. Click "Thanh toán" từ giỏ hàng
2. Vào: `http://localhost:8080/checkout`

**Script:**
> "Trang thanh toán có quy trình rõ ràng và dễ sử dụng."

**Demo:**
1. **Chọn địa chỉ giao hàng:**
   - Chọn địa chỉ có sẵn
   - Hoặc thêm địa chỉ mới

2. **Chọn phương thức vận chuyển:**
   - Standard (3-5 ngày)
   - Express (1-2 ngày)

3. **Chọn phương thức thanh toán:**
   - COD (Thanh toán khi nhận hàng)
   - VNPay
   - Momo
   - Thẻ tín dụng

4. **Xem tóm tắt đơn hàng:**
   - Sản phẩm
   - Phí vận chuyển
   - Giảm giá
   - Tổng cộng

5. Click "Đặt hàng"

**Script tiếp:**
> "Sau khi đặt hàng thành công, hệ thống gửi email xác nhận 
> và chuyển đến trang theo dõi đơn hàng."

**Điểm nhấn:**
- 📍 Multiple shipping addresses
- 🚚 Shipping methods
- 💳 Multiple payment methods
- 📧 Order confirmation email
- 🧾 Order summary

---

### 2.8. Trang cá nhân (1 phút)

**Action:**
1. Click vào tên user ở header
2. Chọn "Trang cá nhân" hoặc vào: `http://localhost:8080/user/profile`

**Script:**
> "Trang cá nhân cho phép người dùng quản lý thông tin và đơn hàng."

**Demo nhanh các tab:**
1. **Thông tin cá nhân:**
   - Xem/Chỉnh sửa thông tin
   - Upload avatar (Cloudinary)
   - Đổi mật khẩu

2. **Địa chỉ giao hàng:**
   - Danh sách địa chỉ
   - Thêm/Sửa/Xóa địa chỉ
   - Đặt địa chỉ mặc định

3. **Lịch sử đơn hàng:**
   - Xem tất cả đơn hàng
   - Theo dõi trạng thái
   - Xem chi tiết đơn hàng

4. **Danh sách yêu thích:**
   - Sản phẩm đã lưu
   - Thêm vào giỏ nhanh

**Điểm nhấn:**
- 👤 Profile management
- 📍 Address management (CRUD)
- 📦 Order history & tracking
- ❤️ Wishlist
- 🔒 Change password
- 📸 Avatar upload (Cloudinary)

---

## 👨‍💼 PHẦN 3: QUẢN TRỊ VIÊN (5 phút)

### 3.1. Đăng nhập Admin (0.5 phút)

**Action:**
1. Đăng xuất tài khoản user
2. Đăng nhập với tài khoản admin

**Login:**
```
Username: admin
Password: admin123
```

**Script:**
> "Bây giờ chúng ta sẽ xem các tính năng dành cho quản trị viên."

---

### 3.2. Admin Dashboard (1 phút)

**Action:**
1. Vào: `http://localhost:8080/admin/dashboard`

**Script:**
> "Dashboard hiển thị tổng quan về hoạt động của hệ thống."

**Demo:**
1. **Thống kê tổng quan:**
   - Tổng doanh thu
   - Số đơn hàng
   - Số người dùng
   - Số sản phẩm

2. **Biểu đồ:**
   - Doanh thu theo tháng
   - Đơn hàng theo trạng thái
   - Top sản phẩm bán chạy

3. **Hoạt động gần đây:**
   - Đơn hàng mới
   - Người dùng mới
   - Reviews mới

**Điểm nhấn:**
- 📊 Real-time statistics
- 📈 Charts & graphs
- 📋 Recent activities
- 💰 Revenue tracking

---

### 3.3. Quản lý sản phẩm (1.5 phút)

**Action:**
1. Click "Sản phẩm" trong menu admin
2. Vào: `http://localhost:8080/admin/products`

**Script:**
> "Quản trị viên có thể quản lý toàn bộ sản phẩm."

**Demo:**
1. **Xem danh sách sản phẩm:**
   - Bảng hiển thị đầy đủ thông tin
   - Search & filter
   - Pagination

2. **Thêm sản phẩm mới:**
   - Click "Thêm sản phẩm"
   - Điền form (tên, giá, mô tả, category, brand)
   - Upload hình ảnh (Cloudinary)
   - Lưu

3. **Chỉnh sửa sản phẩm:**
   - Click "Sửa" trên một sản phẩm
   - Cập nhật thông tin
   - Lưu

4. **Xóa sản phẩm:**
   - Click "Xóa"
   - Confirm

**Điểm nhấn:**
- ✏️ CRUD operations
- 📸 Image upload (Cloudinary)
- 🔍 Search & filter
- 📄 Pagination
- ✅ Form validation

---

### 3.4. Quản lý đơn hàng (1 phút)

**Action:**
1. Click "Đơn hàng" trong menu admin
2. Vào: `http://localhost:8080/admin/orders`

**Script:**
> "Quản lý đơn hàng giúp admin theo dõi và xử lý đơn hàng."

**Demo:**
1. Xem danh sách đơn hàng
2. Filter theo trạng thái (Pending, Processing, Shipped, Delivered)
3. Click vào một đơn hàng để xem chi tiết
4. Cập nhật trạng thái đơn hàng
5. Xem thông tin khách hàng và sản phẩm

**Điểm nhấn:**
- 📦 Order management
- 🔄 Status updates
- 📧 Email notifications
- 👤 Customer information
- 📊 Order details

---

### 3.5. Quản lý người dùng (0.5 phút)

**Action:**
1. Click "Người dùng" trong menu admin
2. Vào: `http://localhost:8080/admin/users`

**Script:**
> "Admin có thể xem và quản lý tất cả người dùng."

**Demo nhanh:**
1. Xem danh sách users
2. Search user
3. Xem chi tiết user
4. Khóa/Mở khóa tài khoản (nếu có)
5. Xem lịch sử hoạt động

**Điểm nhấn:**
- 👥 User management
- 🔍 Search users
- 🔒 Account status control
- 📊 User activity logs

---

### 3.6. Quản lý danh mục & thương hiệu (0.5 phút)

**Action:**
1. Vào "Danh mục" và "Thương hiệu"

**Script:**
> "Admin có thể quản lý categories và brands."

**Demo nhanh:**
1. Xem danh sách categories
2. Thêm/Sửa/Xóa category
3. Xem danh sách brands
4. Thêm/Sửa/Xóa brand

**Điểm nhấn:**
- 📁 Category management
- 🏷️ Brand management
- ✏️ CRUD operations

---

## 🎨 PHẦN 4: TÍNH NĂNG NỔI BẬT (2 phút)

### 4.1. Responsive Design

**Action:**
1. Resize trình duyệt hoặc mở DevTools (F12)
2. Toggle device toolbar (Ctrl+Shift+M)
3. Test trên Mobile, Tablet, Desktop

**Script:**
> "Website được thiết kế responsive, hoạt động tốt trên mọi thiết bị."

**Demo:**
- Mobile view (375px)
- Tablet view (768px)
- Desktop view (1920px)

---

### 4.2. Dark/Light Mode (nếu có)

**Action:**
1. Click icon theme toggle

**Script:**
> "Người dùng có thể chuyển đổi giữa chế độ sáng và tối."

---

### 4.3. Performance & Caching

**Script:**
> "Hệ thống sử dụng Caffeine cache để tối ưu hiệu suất.
> Các sản phẩm được cache, giảm tải cho database."

**Demo (nếu có time):**
1. Vào: `http://localhost:8080/caffeine/cc-doctor`
2. Xem cache statistics

---

### 4.4. Email Service

**Script:**
> "Hệ thống tự động gửi email cho các sự kiện quan trọng:
> - Email chào mừng khi đăng ký
> - Email xác nhận đơn hàng
> - Email reset password
> - Email thông báo trạng thái đơn hàng"

---

## 🎯 PHẦN 5: KẾT LUẬN (1 phút)

### Script:
> "Vậy là chúng ta đã đi qua các tính năng chính của hệ thống Grocery Store.
> 
> **Tóm tắt những gì đã demo:**
> 
> **Phía người dùng:**
> ✅ Đăng ký/Đăng nhập với Spring Security
> ✅ Tìm kiếm & lọc sản phẩm với nhiều tiêu chí
> ✅ Giỏ hàng với AJAX real-time
> ✅ Thanh toán với nhiều phương thức
> ✅ Quản lý thông tin cá nhân, địa chỉ, đơn hàng
> ✅ Wishlist & Reviews
> 
> **Phía admin:**
> ✅ Dashboard với thống kê chi tiết
> ✅ Quản lý sản phẩm, danh mục, thương hiệu
> ✅ Quản lý đơn hàng & người dùng
> ✅ Upload hình ảnh lên Cloudinary
> 
> **Công nghệ:**
> ✅ Spring Boot 4.0.1 + Thymeleaf
> ✅ Spring Security
> ✅ MariaDB/PostgreSQL
> ✅ RESTful API
> ✅ Responsive design
> ✅ Email service
> ✅ Caffeine cache
> 
> Cảm ơn mọi người đã theo dõi! Có câu hỏi nào không ạ?"

---

## 💡 TIPS CHO NGƯỜI DEMO

### Trước khi demo:
1. ✅ Test tất cả tính năng trước
2. ✅ Chuẩn bị dữ liệu mẫu đẹp
3. ✅ Clear browser cache
4. ✅ Đóng các tab không cần thiết
5. ✅ Tắt notifications
6. ✅ Zoom trình duyệt phù hợp
7. ✅ Chuẩn bị backup plan nếu có lỗi

### Trong khi demo:
1. 🗣️ Nói rõ ràng, không quá nhanh
2. 👁️ Nhìn vào camera/khán giả
3. 🖱️ Di chuyển chuột chậm rãi
4. ⏸️ Pause để người xem hiểu
5. 💬 Giải thích "tại sao" không chỉ "cái gì"
6. 😊 Tự tin và nhiệt tình
7. 🐛 Nếu có lỗi, giải thích và tiếp tục

### Sau khi demo:
1. 📝 Ghi nhận feedback
2. 🔧 Fix bugs nếu phát hiện
3. 📊 Cải thiện dựa trên câu hỏi

---

## ❓ CÂU HỎI THƯỜNG GẶP

### Q1: "Làm sao để reset password?"
**A:** Có tính năng "Quên mật khẩu" ở trang đăng nhập. Hệ thống sẽ gửi email với link reset.

### Q2: "Website có hỗ trợ thanh toán online không?"
**A:** Có, hỗ trợ VNPay và Momo. Trong demo này dùng sandbox/test mode.

### Q3: "Làm sao để deploy lên production?"
**A:** Có hướng dẫn chi tiết trong file DEPLOYMENT_GUIDE.md. Có thể deploy lên Render, AWS, hoặc VPS.

### Q4: "Database dùng gì?"
**A:** Support cả MariaDB và PostgreSQL. Local dùng MariaDB, production có thể dùng PostgreSQL.

### Q5: "Có API documentation không?"
**A:** Có, xem file API_ENDPOINTS_REPORT.md trong folder document_file.

### Q6: "Làm sao để thêm sản phẩm mới?"
**A:** Đăng nhập với tài khoản admin, vào Admin > Products > Thêm sản phẩm.

### Q7: "Website có responsive không?"
**A:** Có, hoạt động tốt trên Mobile, Tablet, Desktop.

### Q8: "Có tính năng gì nổi bật?"
**A:** 
- Spring Security authentication
- Real-time cart updates với AJAX
- Cloudinary image storage
- Email notifications
- Caffeine cache
- Multiple payment methods
- Admin dashboard với statistics

---

## 📚 TÀI LIỆU THAM KHẢO

- [README.md](README.md) - Tổng quan dự án
- [PROJECT_DOCUMENTATION.md](document_file/PROJECT_DOCUMENTATION.md) - Tài liệu chi tiết
- [DEPLOYMENT_GUIDE.md](document_file/DEPLOYMENT_GUIDE.md) - Hướng dẫn deploy
- [API_ENDPOINTS_REPORT.md](document_file/API_ENDPOINTS_REPORT.md) - API documentation
- [TEST_SCENARIO_FULL_PROJECT.md](document_file/TEST_SCENARIO_FULL_PROJECT.md) - Test scenarios

---

**🎬 CHÚC BẠN DEMO THÀNH CÔNG! 🎬**
