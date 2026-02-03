# Cập nhật Form Địa chỉ trong Checkout

## Tổng quan
Đã cập nhật form nhập địa chỉ mới trong trang checkout để giống với modal thêm địa chỉ ở trang `/addresses`.

## Thay đổi chính

### 1. Cấu trúc Form mới

#### A. Thông tin liên hệ
```
┌─────────────────────────────────────────┐
│ Họ và tên *        │ Số điện thoại *    │
└─────────────────────────────────────────┘
```

#### B. Địa chỉ (Dropdown cascading)
```
┌─────────────────────────────────────────┐
│ Tỉnh/TP * │ Quận/Huyện * │ Phường/Xã * │
├─────────────────────────────────────────┤
│ Địa chỉ cụ thể * (textarea)             │
└─────────────────────────────────────────┘
```

#### C. Cài đặt
```
┌─────────────────────────────────────────┐
│ Loại địa chỉ:                           │
│  🏠 Nhà riêng    🏢 Văn phòng           │
├─────────────────────────────────────────┤
│ ☐ Lưu địa chỉ này cho lần mua sau       │
└─────────────────────────────────────────┘
```

### 2. Tích hợp API Vietnam Provinces

**API sử dụng:** `https://provinces.open-api.vn/api`

#### Endpoints:
```javascript
// Load provinces
GET /api/p/

// Load districts by province
GET /api/p/{provinceCode}?depth=2

// Load wards by district
GET /api/d/{districtCode}?depth=2
```

#### Flow cascading:
```
1. User chọn Tỉnh/Thành phố
   → loadCheckoutDistricts()
   → Enable dropdown Quận/Huyện
   → Load danh sách quận/huyện

2. User chọn Quận/Huyện
   → loadCheckoutWards()
   → Enable dropdown Phường/Xã
   → Load danh sách phường/xã

3. User chọn Phường/Xã
   → Ready to submit
```

### 3. JavaScript Functions mới

#### `loadCheckoutProvinces()`
```javascript
// Load danh sách tỉnh/thành phố từ API
// Fallback to static list nếu API fail
// Populate dropdown #new-province
```

#### `loadCheckoutDistricts()`
```javascript
// Load danh sách quận/huyện theo tỉnh đã chọn
// Enable/disable dropdown #new-district
// Reset dropdown #new-ward
```

#### `loadCheckoutWards()`
```javascript
// Load danh sách phường/xã theo quận đã chọn
// Enable/disable dropdown #new-ward
```

#### `getCheckoutSelectedLocationNames()`
```javascript
// Lấy tên (text) của các location đã chọn
// Return: { province, district, ward }
// Dùng dataset.name để lấy tên đầy đủ
```

#### Updated: `validateNewAddress()`
```javascript
// Validate tất cả dropdown phải được chọn
// Validate phone format: 10-11 chữ số
// Combine location thành city field
// Format: "Phường/Xã, Quận/Huyện, Tỉnh/Thành phố"
// Return address object với format API
```

### 4. CSS mới

#### Form Sections
```css
.form-section          /* Container cho mỗi section */
.form-section__title   /* Tiêu đề section với border-bottom */
```

#### Form Layout
```css
.form-row              /* Grid 2 cột */
.form-row--three       /* Grid 3 cột cho Province/District/Ward */
```

#### Address Type Buttons
```css
.address-type-group    /* Container cho radio buttons */
.address-type-btn      /* Mỗi button (Nhà riêng/Văn phòng) */
.address-type-icon     /* Icon emoji */
```

**Features:**
- Hover effect
- Active state khi checked
- Icon scale animation
- Border color change

#### Custom Checkbox
```css
.checkbox-label        /* Container */
.checkbox-custom       /* Custom checkbox UI */
```

**Features:**
- Hidden native checkbox
- Custom styled box
- Checkmark (✓) khi checked
- Smooth transitions

#### Form Controls
```css
.form-input            /* Unified styling cho input/select/textarea */
.form-input:disabled   /* Disabled state cho dropdown */
```

**Features:**
- Focus state với border color + shadow
- Disabled state với gray background
- Placeholder styling
- Textarea resize vertical

### 5. Data Format

#### Address Object gửi lên API:
```javascript
{
  recipientName: "Nguyễn Văn A",
  phone: "0912345678",
  addressLine1: "123 Đường ABC, Tòa nhà XYZ",
  city: "Phường Dịch Vọng, Quận Cầu Giấy, Hà Nội",
  isDefault: false
}
```

**Lưu ý:**
- Field `city` chứa full address: Ward, District, Province
- Format chuẩn: "Phường/Xã, Quận/Huyện, Tỉnh/Thành phố"
- Giống format trong modal addresses.html

### 6. Validation Rules

#### Required fields:
- ✅ Họ và tên
- ✅ Số điện thoại (10-11 chữ số)
- ✅ Tỉnh/Thành phố
- ✅ Quận/Huyện
- ✅ Phường/Xã
- ✅ Địa chỉ cụ thể

#### Phone validation:
```javascript
const phoneRegex = /^(0|\+84)[0-9]{9,10}$/;
```
- Bắt đầu bằng 0 hoặc +84
- Theo sau là 9-10 chữ số
- Tổng: 10-11 chữ số

### 7. User Experience

#### Cascading Dropdowns:
```
Tỉnh/TP: Enabled (default)
   ↓ (user chọn)
Quận/Huyện: Enabled
   ↓ (user chọn)
Phường/Xã: Enabled
```

#### Disabled State:
- Quận/Huyện disabled cho đến khi chọn Tỉnh/TP
- Phường/Xã disabled cho đến khi chọn Quận/Huyện
- Visual feedback: gray background, cursor not-allowed

#### Error Messages:
- Specific cho từng field
- Focus vào field lỗi
- Notification modal warning

### 8. Responsive Design

#### Desktop (> 768px):
- Họ tên + SĐT: 2 cột
- Tỉnh/Quận/Phường: 3 cột
- Address type buttons: horizontal

#### Mobile (≤ 768px):
- Tất cả fields: 1 cột (stack vertical)
- Address type buttons: vertical stack
- Textarea full width

### 9. Fallback Strategy

#### Nếu API fail:
```javascript
loadStaticCheckoutProvinces()
```

**Static list bao gồm:**
- 63 tỉnh/thành phố Việt Nam
- Sorted alphabetically
- Fallback graceful, không crash

### 10. Integration với placeOrder()

#### Flow khi submit:
```javascript
1. Check address type (saved/new)

2. If new:
   a. validateNewAddress()
      → Return null nếu invalid
      → Return address object nếu valid
   
   b. Check "save checkbox"
      → If checked: POST /api/addresses
      → If not: Save to localStorage as temp
   
   c. Get address ID
   
3. Save to localStorage:
   - checkoutAddressId
   - selectedPaymentMethod
   
4. Redirect to /payment
```

## So sánh với Modal cũ

### Giống nhau:
✅ Cấu trúc form sections
✅ Dropdown cascading (Province → District → Ward)
✅ API Vietnam Provinces
✅ Address type buttons (Nhà riêng/Văn phòng)
✅ Custom checkbox styling
✅ Validation rules
✅ Data format gửi API

### Khác biệt:
- Modal: Có nút "Hủy" và "Lưu địa chỉ"
- Checkout: Tích hợp trong flow checkout, submit cùng order
- Modal: Luôn lưu vào database
- Checkout: Option lưu hoặc không lưu

## Testing Checklist

### Load Provinces
- [ ] Page load → provinces dropdown populated
- [ ] Fallback nếu API fail → static list
- [ ] Dropdown có 63 tỉnh/thành phố

### Cascading Dropdowns
- [ ] Chọn tỉnh → districts dropdown enabled
- [ ] Chọn quận → wards dropdown enabled
- [ ] Đổi tỉnh → reset quận và phường
- [ ] Đổi quận → reset phường

### Validation
- [ ] Submit không chọn tỉnh → warning
- [ ] Submit không chọn quận → warning
- [ ] Submit không chọn phường → warning
- [ ] Submit SĐT sai format → warning
- [ ] Submit thiếu họ tên → warning
- [ ] Submit thiếu địa chỉ cụ thể → warning

### Address Type
- [ ] Radio buttons hoạt động
- [ ] Visual feedback khi chọn
- [ ] Icon scale animation
- [ ] Default: Nhà riêng

### Save Checkbox
- [ ] Check → lưu vào database
- [ ] Uncheck → lưu localStorage temp
- [ ] Địa chỉ xuất hiện đúng trong /addresses

### Responsive
- [ ] Desktop: 3 cột cho province/district/ward
- [ ] Mobile: 1 cột stack vertical
- [ ] Address type buttons responsive
- [ ] Form readable trên mọi màn hình

### Integration
- [ ] Submit form → validate thành công
- [ ] Redirect to /payment
- [ ] Address ID saved to localStorage
- [ ] Payment page nhận đúng address

## Files Changed

```
src/main/resources/templates/module/order/checkout.html
  - Cập nhật HTML form structure
  - Thêm dropdown cascading
  - Thêm address type buttons
  - Thêm custom checkbox
  - Thêm CSS cho form sections
  - Thêm JavaScript load provinces/districts/wards
  - Cập nhật validateNewAddress()
  - Gộp DOMContentLoaded listeners
```

## API Dependencies

### External API:
```
https://provinces.open-api.vn/api
```

**Endpoints used:**
- `/p/` - Get all provinces
- `/p/{code}?depth=2` - Get districts by province
- `/d/{code}?depth=2` - Get wards by district

**Fallback:**
- Static list of 63 provinces
- Manual input for district/ward (nếu cần)

### Internal API:
```
POST /api/addresses
```

**Body:**
```json
{
  "recipientName": "string",
  "phone": "string",
  "addressLine1": "string",
  "city": "string",
  "isDefault": boolean
}
```

## Kết luận

Form địa chỉ trong checkout giờ đây:
- ✅ Giống 100% với modal thêm địa chỉ
- ✅ Sử dụng API Vietnam Provinces
- ✅ Dropdown cascading Province → District → Ward
- ✅ Validation đầy đủ
- ✅ UX tốt với disabled states
- ✅ Responsive design
- ✅ Fallback strategy
- ✅ Tích hợp hoàn chỉnh với checkout flow

Người dùng có trải nghiệm nhất quán khi nhập địa chỉ ở bất kỳ đâu trong hệ thống.
