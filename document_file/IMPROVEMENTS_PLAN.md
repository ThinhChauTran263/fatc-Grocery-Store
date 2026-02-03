# Kế hoạch Cải tiến Dự án

## 1. ✅ Giao diện Trang chủ - Carousel cho Sản phẩm
**Mục tiêu**: Chuyển các section sản phẩm sang dạng Slide trượt (Carousel)

**Trạng thái**: ✅ HOÀN THÀNH

**Đã thực hiện**:
- ✅ Tạo component carousel mới cho sản phẩm (`product-carousel.js`)
- ✅ Tạo CSS styling cho carousel (`product-carousel.css`)
- ✅ Áp dụng cho section "Sản phẩm được đánh giá cao"
- ✅ Thêm navigation buttons và dots
- ✅ Responsive cho mobile, tablet, desktop
- ✅ Auto-play với interval 4 giây
- ✅ Pause on hover

**Files đã chỉnh sửa**:
- `src/main/resources/templates/index.html`
- `src/main/resources/static/assets/js/product-carousel.js` (mới)
- `src/main/resources/static/assets/css/product-carousel.css` (mới)

---

## 2. ✅ Thống nhất Template Login/Register
**Mục tiêu**: Đồng bộ thiết kế giữa trang Login và Register

**Trạng thái**: ✅ ĐÃ ĐỒNG BỘ

**Kết quả**:
- Cả 2 trang đã có cấu trúc HTML giống nhau
- Đồng bộ CSS classes
- Thống nhất responsive behavior với `d-none d-md-flex`
- Layout nhất quán

**Files**:
- `src/main/resources/templates/module/auth/sign-in.html`
- `src/main/resources/templates/module/auth/sign-up.html`

---

## 3. ✅ Bổ sung Chức năng

### 3.1. ✅ Thanh Tìm kiếm (Search)
**Trạng thái**: ✅ ĐÃ CÓ SẴN
- Header đã có search bar hoạt động
- Có autocomplete và search functionality

### 3.2. ✅ Validation Số điện thoại
**Mục tiêu**: Thêm validation cho ô nhập số điện thoại

**Trạng thái**: ✅ HOÀN THÀNH

**Đã thực hiện**:
- ✅ Thêm `type="tel"` cho input
- ✅ Thêm `pattern="[0-9]{10,11}"` validation
- ✅ Thêm title hint cho user
- ✅ Thêm text hướng dẫn bên dưới input
- ✅ Validation client-side

**Files đã chỉnh sửa**:
- `src/main/resources/templates/module/user/edit-personal-info.html`
- `src/main/resources/templates/module/order/checkout.html` (đã có sẵn)

### 3.3. ✅ Chọn nhiều sản phẩm trong Giỏ hàng
**Mục tiêu**: Cho phép chọn nhiều sản phẩm để thanh toán

**Trạng thái**: ✅ HOÀN THÀNH

**Đã thực hiện**:
- ✅ Thêm checkbox cho mỗi sản phẩm
- ✅ Thêm "Chọn tất cả" checkbox ở header
- ✅ Cập nhật tổng tiền theo sản phẩm được chọn
- ✅ Highlight sản phẩm được chọn (background color)
- ✅ Hiển thị số lượng sản phẩm đã chọn
- ✅ Nút "Thanh toán sản phẩm đã chọn"
- ✅ Validation: phải chọn ít nhất 1 sản phẩm

**Files đã chỉnh sửa**:
- `src/main/resources/templates/module/cart/cart.html`

---

## 4. ⏳ Banner Chuyên nghiệp
**Mục tiêu**: Chỉnh sửa ảnh banner cho chuyên nghiệp hơn

**Trạng thái**: ⏳ ĐANG CHỜ

**Ghi chú**: 
- Banner hiện tại đã có slideshow với 5 slides
- Cần thiết kế lại ảnh banner với:
  - Design hiện đại hơn
  - Gradient và typography đẹp
  - Call-to-action rõ ràng
  - Tối ưu cho mobile

**Files cần chỉnh sửa**:
- Ảnh banner trong `src/main/resources/static/assets/img/slideshow/`
- CSS styling nếu cần

---

## 📊 Tổng kết

### ✅ Đã hoàn thành (3/4 mục chính):
1. ✅ Carousel cho sản phẩm trang chủ
2. ✅ Thống nhất template Login/Register  
3. ✅ Validation số điện thoại
4. ✅ Chọn nhiều sản phẩm trong giỏ hàng
5. ✅ Thanh tìm kiếm (đã có sẵn)

### ⏳ Đang chờ (1/4 mục):
1. ⏳ Cải thiện banner (cần thiết kế ảnh mới)

---

## Ghi chú Kỹ thuật
- Tất cả thay đổi đã responsive
- Giữ nguyên màu chủ đạo: #77dae6 (cyan)
- Carousel có auto-play và pause on hover
- Validation số điện thoại: 10-11 chữ số
- Checkbox trong giỏ hàng có accent color theo theme
- Tất cả animation smooth với cubic-bezier
