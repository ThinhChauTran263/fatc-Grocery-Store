# Cải Tiến Bảo Mật Đổi Mật Khẩu

## Tổng Quan
Đã nâng cấp tính năng đổi mật khẩu tại trang `/edit-personal-info` với các biện pháp bảo mật tăng cường.

## Các Thay Đổi Chính

### 1. Thêm Field Mật Khẩu Hiện Tại
- **Vị trí**: Trang `/edit-personal-info` - Section "Đổi mật khẩu"
- **Mục đích**: Xác thực người dùng trước khi cho phép thay đổi mật khẩu
- **Bắt buộc**: Chỉ khi người dùng muốn đổi mật khẩu

### 2. Các Biện Pháp Bảo Mật Mới

#### Backend Validation (UserServiceImpl.java)
1. **Xác thực mật khẩu hiện tại**
   - Kiểm tra mật khẩu hiện tại phải được nhập khi đổi mật khẩu
   - Xác minh mật khẩu hiện tại có đúng không bằng `passwordEncoder.matches()`
   - Thông báo lỗi: "Mật khẩu hiện tại không chính xác"

2. **Kiểm tra mật khẩu mới không trùng mật khẩu cũ**
   - Ngăn người dùng đặt lại mật khẩu giống với mật khẩu hiện tại
   - Thông báo lỗi: "Mật khẩu mới không được trùng với mật khẩu hiện tại"

3. **Validation độ dài mật khẩu**
   - Mật khẩu mới phải từ 6-100 ký tự
   - Thông báo lỗi: "Mật khẩu phải từ 6-100 ký tự"

4. **Xác nhận mật khẩu**
   - Kiểm tra mật khẩu mới và xác nhận mật khẩu phải khớp
   - Thông báo lỗi: "Password xác nhận không khớp"

#### Frontend Enhancement (edit-personal-info.html)
1. **Visual Feedback**
   - Highlight field mật khẩu hiện tại (màu vàng) khi người dùng bắt đầu nhập mật khẩu mới mà chưa nhập mật khẩu hiện tại
   - Tự động xóa highlight khi người dùng nhập mật khẩu hiện tại

2. **Client-side Validation**
   - Kiểm tra mật khẩu hiện tại phải được nhập trước khi submit
   - Kiểm tra mật khẩu mới và xác nhận mật khẩu phải khớp
   - Kiểm tra độ dài mật khẩu tối thiểu 6 ký tự
   - Hiển thị thông báo lỗi rõ ràng qua NotificationModal

3. **UI/UX Improvements**
   - Thêm hint text: "Bắt buộc nhập khi thay đổi mật khẩu"
   - Placeholder rõ ràng cho từng field
   - Icon khóa cho tất cả các field mật khẩu

### 3. Cấu Trúc Form Mới

```
Section: Đổi mật khẩu
├── Mật khẩu hiện tại (*) [Full width]
│   └── Hint: "Bắt buộc nhập khi thay đổi mật khẩu"
├── Mật khẩu mới [50% width]
│   └── Placeholder: "Nhập mật khẩu mới (tối thiểu 6 ký tự)"
└── Xác nhận mật khẩu [50% width]
    └── Placeholder: "Nhập lại mật khẩu mới"
```

## Files Đã Thay Đổi

### 1. Backend
- `ProfileUpdateRequest.java`: Thêm field `currentPassword`
- `UserServiceImpl.java`: Thêm logic validation bảo mật tăng cường

### 2. Frontend
- `edit-personal-info.html`: 
  - Thêm field mật khẩu hiện tại
  - Thêm JavaScript validation và visual feedback
- `edit-personal-info.css`: Thêm style cho hint text

## Luồng Xử Lý

### Khi Người Dùng Không Đổi Mật Khẩu
1. Để trống tất cả các field mật khẩu
2. Chỉ cập nhật thông tin cơ bản (họ tên, email, số điện thoại)

### Khi Người Dùng Đổi Mật Khẩu
1. **Nhập mật khẩu hiện tại** (bắt buộc)
2. **Nhập mật khẩu mới** (tối thiểu 6 ký tự)
3. **Nhập xác nhận mật khẩu** (phải khớp với mật khẩu mới)
4. **Submit form**
5. **Backend validation**:
   - Kiểm tra mật khẩu hiện tại có được nhập không
   - Xác thực mật khẩu hiện tại có đúng không
   - Kiểm tra mật khẩu mới không trùng mật khẩu cũ
   - Kiểm tra độ dài mật khẩu mới
   - Kiểm tra mật khẩu mới và xác nhận có khớp không
6. **Cập nhật mật khẩu** nếu tất cả validation pass

## Thông Báo Lỗi

| Tình Huống | Thông Báo |
|-----------|-----------|
| Không nhập mật khẩu hiện tại | "Vui lòng nhập mật khẩu hiện tại để đổi mật khẩu" |
| Mật khẩu hiện tại sai | "Mật khẩu hiện tại không chính xác" |
| Mật khẩu mới trùng mật khẩu cũ | "Mật khẩu mới không được trùng với mật khẩu hiện tại" |
| Mật khẩu mới quá ngắn/dài | "Mật khẩu phải từ 6-100 ký tự" |
| Xác nhận mật khẩu không khớp | "Password xác nhận không khớp" |

## Lợi Ích Bảo Mật

1. **Ngăn chặn truy cập trái phép**: Yêu cầu xác thực mật khẩu hiện tại
2. **Tránh lỗi người dùng**: Validation phía client giúp phát hiện lỗi sớm
3. **Bảo vệ tài khoản**: Không cho phép đặt lại mật khẩu giống cũ
4. **Trải nghiệm tốt hơn**: Visual feedback và thông báo lỗi rõ ràng

## Testing

### Test Cases
1. ✅ Cập nhật thông tin cơ bản mà không đổi mật khẩu
2. ✅ Đổi mật khẩu mà không nhập mật khẩu hiện tại → Lỗi
3. ✅ Đổi mật khẩu với mật khẩu hiện tại sai → Lỗi
4. ✅ Đổi mật khẩu mới trùng với mật khẩu cũ → Lỗi
5. ✅ Đổi mật khẩu mới quá ngắn (< 6 ký tự) → Lỗi
6. ✅ Xác nhận mật khẩu không khớp → Lỗi
7. ✅ Đổi mật khẩu thành công với tất cả thông tin hợp lệ

### Cách Test
```bash
# 1. Truy cập trang
http://localhost:8080/edit-personal-info

# 2. Test các trường hợp trên
# 3. Kiểm tra thông báo lỗi hiển thị đúng
# 4. Kiểm tra visual feedback (highlight màu vàng)
# 5. Verify mật khẩu mới hoạt động sau khi đổi thành công
```

## Tương Thích
- ✅ Desktop browsers
- ✅ Mobile browsers
- ✅ Dark mode
- ✅ Responsive design

## Notes
- Mật khẩu được mã hóa bằng BCrypt trước khi lưu vào database
- Session không bị invalidate sau khi đổi mật khẩu (người dùng không cần đăng nhập lại)
- Có thể mở rộng thêm: gửi email thông báo khi đổi mật khẩu thành công
