# Cải tiến Trang Checkout và Menu User

## Tổng quan
Đã thực hiện 4 cải tiến chính cho trải nghiệm người dùng:

## 1. ✅ Thêm "Lịch sử đơn hàng" vào menu popup avatar

### File: `src/main/resources/templates/shared/fragments/header/user-actions.html`

**Thay đổi:**
- Thêm menu item "Lịch sử đơn hàng" với icon document
- Link đến `/my-orders`
- Vị trí: Giữa "Hồ sơ cá nhân" và "Danh sách yêu thích"

**Menu structure mới:**
```
📋 Menu User
├── 🛠️ Quản trị hệ thống (chỉ Admin)
├── 👤 Hồ sơ cá nhân
├── 📄 Lịch sử đơn hàng ← MỚI
├── ❤️ Danh sách yêu thích
├── ✏️ Cài đặt tài khoản
└── ➡️ Đăng xuất
```

---

## 2. ✅ Chỉnh tiếng Việt cho menu popup avatar

### File: `src/main/resources/templates/shared/fragments/header/user-actions.html`

**Các thay đổi:**
- ~~Profile~~ → **Hồ sơ cá nhân**
- ~~Favourite list~~ → **Danh sách yêu thích**
- ~~Settings~~ → **Cài đặt tài khoản**
- ~~Logout~~ → **Đăng xuất**
- ~~Admin Dashboard~~ → **Quản trị hệ thống**

**Thêm icons cho mỗi menu item:**
- 🛡️ Quản trị hệ thống: `shield.svg`
- 👤 Hồ sơ cá nhân: `profile.svg`
- 📄 Lịch sử đơn hàng: `document.svg`
- ❤️ Danh sách yêu thích: `heart.svg`
- ✏️ Cài đặt tài khoản: `edit.svg`
- ➡️ Đăng xuất: `arrow-right.svg`

---

## 3. ✅ Bỏ mô tả dưới checkbox thanh toán

### File: `src/main/resources/templates/module/order/checkout.html`

**Thay đổi:**
Đã xóa các dòng `<p class="payment-method__desc">` cho tất cả phương thức thanh toán:

**Trước:**
```html
<h4 class="payment-method__title">Thanh toán khi nhận hàng (COD)</h4>
<p class="payment-method__desc">Thanh toán bằng tiền mặt khi nhận hàng</p>
```

**Sau:**
```html
<h4 class="payment-method__title">Thanh toán khi nhận hàng (COD)</h4>
```

Áp dụng cho:
- ✅ COD
- ✅ VNPay
- ✅ MoMo

**Lợi ích:**
- Giao diện gọn gàng hơn
- Giảm clutter
- Tiêu đề đã đủ rõ ràng

---

## 4. ✅ Thêm form nhập địa chỉ trực tiếp trong checkout

### File: `src/main/resources/templates/module/order/checkout.html`

### A. Giao diện mới

**Thêm 2 options:**
1. **Chọn từ địa chỉ đã lưu** (mặc định)
   - Dropdown select địa chỉ có sẵn
   - Link "Quản lý địa chỉ"

2. **Nhập địa chỉ mới** (radio button)
   - Form nhập đầy đủ thông tin
   - Option lưu địa chỉ cho lần sau

### B. Form nhập địa chỉ mới

**Các trường:**
```
┌─────────────────────────────────────────┐
│ Họ và tên *        │ Số điện thoại *    │
├─────────────────────────────────────────┤
│ Địa chỉ chi tiết *                      │
├─────────────────────────────────────────┤
│ Tỉnh/Thành phố *   │ Quận/Huyện *       │
├─────────────────────────────────────────┤
│ Phường/Xã                               │
├─────────────────────────────────────────┤
│ ☐ Lưu địa chỉ này cho lần mua sau       │
└─────────────────────────────────────────┘
```

**Trường bắt buộc (*):**
- Họ và tên
- Số điện thoại
- Địa chỉ chi tiết
- Tỉnh/Thành phố
- Quận/Huyện

**Trường tùy chọn:**
- Phường/Xã

### C. CSS mới

**Thêm styles:**
```css
.address-options      /* Radio buttons chọn loại địa chỉ */
.address-option       /* Mỗi option */
.address-form         /* Container form */
.form-row             /* Grid 2 cột */
.form-group           /* Mỗi field */
.form-input           /* Input styling */
.required             /* Dấu * màu đỏ */
```

**Responsive:**
- Desktop: 2 cột cho form-row
- Mobile: 1 cột (stack vertical)

### D. JavaScript mới

#### Function: `toggleAddressForm()`
```javascript
// Toggle giữa saved address và new address form
// Được gọi khi user click radio button
```

#### Function: `validateNewAddress()`
```javascript
// Validate tất cả trường bắt buộc
// Kiểm tra format số điện thoại
// Return address object hoặc null nếu invalid
```

**Validation rules:**
- Họ tên: không được rỗng
- Số điện thoại: format `0xxxxxxxxx` hoặc `+84xxxxxxxxx`
- Địa chỉ, City, District: không được rỗng

#### Function: `saveNewAddress(addressData)`
```javascript
// POST /api/addresses
// Lưu địa chỉ mới vào database
// Return address ID
```

#### Updated: `placeOrder()`
```javascript
// Logic mới:
1. Check address type (saved/new)
2. If saved: get from dropdown
3. If new:
   - Validate form
   - If "save checkbox" checked:
     → Save to server via API
   - Else:
     → Save to localStorage as temp
4. Proceed to payment
```

### E. Flow hoạt động

#### Scenario 1: Chọn địa chỉ đã lưu
```
1. User chọn radio "Chọn từ địa chỉ đã lưu"
2. Chọn địa chỉ từ dropdown
3. Click "Đặt hàng ngay"
4. → Redirect to /payment
```

#### Scenario 2: Nhập địa chỉ mới (Lưu)
```
1. User chọn radio "Nhập địa chỉ mới"
2. Form hiển thị
3. Nhập đầy đủ thông tin
4. Check ☑ "Lưu địa chỉ này cho lần mua sau"
5. Click "Đặt hàng ngay"
6. → Validate form
7. → POST /api/addresses (save to DB)
8. → Redirect to /payment
```

#### Scenario 3: Nhập địa chỉ mới (Không lưu)
```
1. User chọn radio "Nhập địa chỉ mới"
2. Form hiển thị
3. Nhập đầy đủ thông tin
4. Không check "Lưu địa chỉ..."
5. Click "Đặt hàng ngay"
6. → Validate form
7. → Save to localStorage as temp
8. → Redirect to /payment
```

### F. API Integration

**Endpoint sử dụng:**
```
POST /api/addresses
Content-Type: application/json

Body:
{
  "fullName": "Nguyễn Văn A",
  "phoneNumber": "0912345678",
  "addressLine": "123 Đường ABC",
  "city": "Hà Nội",
  "district": "Cầu Giấy",
  "ward": "Dịch Vọng",
  "isDefault": false
}

Response:
{
  "id": 123,
  "fullName": "Nguyễn Văn A",
  ...
}
```

### G. LocalStorage Keys

**Dữ liệu lưu:**
```javascript
// Address ID (saved or temp)
localStorage.setItem('checkoutAddressId', addressId);

// Temp address (if not saved to DB)
localStorage.setItem('tempCheckoutAddress', JSON.stringify({
  id: 'temp_1234567890',
  fullName: "...",
  phoneNumber: "...",
  ...
}));

// Payment method
localStorage.setItem('selectedPaymentMethod', 'COD');
```

---

## Lợi ích của các cải tiến

### 1. Menu User
- ✅ Dễ truy cập lịch sử đơn hàng
- ✅ Tiếng Việt dễ hiểu
- ✅ Icons trực quan

### 2. Payment Methods
- ✅ Giao diện gọn gàng
- ✅ Giảm text không cần thiết
- ✅ Focus vào action

### 3. Address Form
- ✅ Không cần rời trang checkout
- ✅ Linh hoạt: lưu hoặc không lưu
- ✅ Validation đầy đủ
- ✅ UX mượt mà

---

## Testing Checklist

### Menu User
- [ ] Click avatar → menu hiển thị
- [ ] Click "Lịch sử đơn hàng" → redirect /my-orders
- [ ] Tất cả text đều tiếng Việt
- [ ] Icons hiển thị đúng

### Payment Methods
- [ ] Checkbox COD không có mô tả
- [ ] Checkbox VNPay không có mô tả
- [ ] Checkbox MoMo không có mô tả
- [ ] Layout vẫn đẹp

### Address Form - Saved Address
- [ ] Radio "Chọn từ địa chỉ đã lưu" checked mặc định
- [ ] Dropdown hiển thị danh sách địa chỉ
- [ ] Chọn địa chỉ → đặt hàng thành công

### Address Form - New Address (Save)
- [ ] Radio "Nhập địa chỉ mới" → form hiển thị
- [ ] Validate: bỏ trống trường bắt buộc → warning
- [ ] Validate: số điện thoại sai format → warning
- [ ] Check "Lưu địa chỉ" → POST /api/addresses
- [ ] Đặt hàng thành công
- [ ] Địa chỉ xuất hiện trong /addresses

### Address Form - New Address (No Save)
- [ ] Radio "Nhập địa chỉ mới" → form hiển thị
- [ ] Không check "Lưu địa chỉ"
- [ ] Đặt hàng thành công
- [ ] Địa chỉ KHÔNG xuất hiện trong /addresses
- [ ] Địa chỉ lưu trong localStorage

### Responsive
- [ ] Desktop: form 2 cột
- [ ] Mobile: form 1 cột
- [ ] Tất cả elements responsive

---

## Files Changed

```
src/main/resources/templates/shared/fragments/header/user-actions.html
  - Thêm menu "Lịch sử đơn hàng"
  - Chỉnh tiếng Việt
  - Thêm icons

src/main/resources/templates/module/order/checkout.html
  - Bỏ payment method descriptions
  - Thêm address form
  - Thêm CSS cho form
  - Thêm JavaScript validation
  - Update placeOrder() function
```

---

## Kết luận

Tất cả 4 yêu cầu đã được hoàn thành:
1. ✅ Lịch sử đơn hàng trong menu avatar
2. ✅ Tiếng Việt cho menu popup
3. ✅ Bỏ mô tả checkbox thanh toán
4. ✅ Form nhập địa chỉ trong checkout

Người dùng giờ có thể:
- Truy cập nhanh lịch sử đơn hàng
- Hiểu rõ menu bằng tiếng Việt
- Nhìn giao diện thanh toán gọn gàng
- Nhập địa chỉ ngay tại checkout mà không cần rời trang
