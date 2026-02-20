# 📱 Hướng dẫn Mobile-First Layout

## 📋 Tổng quan

Layout mới được thiết kế theo phong cách **Mobile-First** với các tính năng:

- ✅ **Topbar đơn giản** với nút Hamburger menu
- ✅ **Sidebar ẩn (Drawer)** cho navigation
- ✅ **Responsive** tự động với Flexbox và CSS Grid
- ✅ **Tối ưu hiệu suất** cho mobile
- ✅ **Tương thích ngược** với desktop layout hiện tại

---

## 🎯 Cấu trúc Files

### 1. Template Files

```
src/main/resources/templates/shared/fragments/
├── mobile-header.html          # Mobile header với Topbar + Sidebar
├── responsive-header.html      # Wrapper tự động chuyển đổi mobile/desktop
└── header.html                 # Desktop header (giữ nguyên)
```

### 2. CSS Files

```
src/main/resources/static/assets/css/
└── mobile-layout.css           # CSS cho mobile layout
```

### 3. JavaScript Files

```
src/main/resources/static/assets/js/
└── mobile-layout.js            # Logic xử lý mobile interactions
```

---

## 🚀 Cách sử dụng

### Option 1: Sử dụng Responsive Header (Khuyến nghị)

Thay thế header hiện tại bằng responsive header trong template:

```html
<!-- Thay vì -->
<header th:replace="~{shared/fragments/header :: header}"></header>

<!-- Sử dụng -->
<div th:replace="~{shared/fragments/responsive-header :: responsiveHeader}"></div>
```

**Lợi ích:**
- Tự động hiển thị mobile header trên màn hình < 1024px
- Tự động hiển thị desktop header trên màn hình >= 1024px
- Không cần thay đổi code logic

### Option 2: Chỉ sử dụng Mobile Header

Nếu muốn chỉ dùng mobile layout:

```html
<div th:replace="~{shared/fragments/mobile-header :: mobileHeader}"></div>
```

---

## 🎨 Cấu trúc Mobile Layout

### 1. Topbar (Thanh trên cùng)

```
┌─────────────────────────────────────┐
│ ☰  [Logo]           🔍  🛒         │
└─────────────────────────────────────┘
```

**Thành phần:**
- **Hamburger Button** (trái): Mở/đóng sidebar
- **Logo** (giữa): Link về trang chủ
- **Search Button** (phải): Toggle thanh tìm kiếm
- **Cart Button** (phải): Link đến giỏ hàng + badge số lượng

**Đặc điểm:**
- Sticky position (luôn hiển thị khi scroll)
- Height: 56px
- Shadow để tách biệt với content

### 2. Search Bar (Expandable)

```
┌─────────────────────────────────────┐
│ [Tìm kiếm sản phẩm...]        🔍   │
└─────────────────────────────────────┘
```

**Đặc điểm:**
- Ẩn mặc định, hiện khi nhấn search button
- Smooth animation (slide down)
- Auto-focus vào input khi mở

### 3. Sidebar Drawer

```
┌──────────────────┐
│  [Avatar]        │ ← Header
│  User Name       │
│  user@email.com  │
├──────────────────┤
│  🏠 Trang chủ    │
│  🛍️ Sản phẩm     │ ← Menu
│  🧮 Caffeine     │
├──────────────────┤
│  👤 Hồ sơ        │
│  📦 Đơn hàng     │ ← User Menu
│  ❤️ Yêu thích    │
│  ⚙️ Cài đặt      │
├──────────────────┤
│  🚪 Đăng xuất    │ ← Footer
└──────────────────┘
```

**Đặc điểm:**
- Slide-in từ trái
- Width: 280px (max 85vw)
- Overlay tối phía sau
- Smooth animation với cubic-bezier
- Đóng khi:
  - Click overlay
  - Click close button
  - Press ESC key
  - Click vào menu item

---

## 💻 CSS Classes & Utilities

### Container Classes

```css
.mobile-main-content    /* Main content wrapper */
.mobile-container       /* Container với padding */
```

### Grid System

```css
.mobile-grid            /* Base grid */
.mobile-grid--1         /* 1 column */
.mobile-grid--2         /* 2 columns */
.mobile-grid--auto      /* Auto-fit responsive */
```

### Flexbox Utilities

```css
.mobile-flex                /* display: flex */
.mobile-flex--column        /* flex-direction: column */
.mobile-flex--center        /* center items */
.mobile-flex--between       /* space-between */
.mobile-flex--wrap          /* flex-wrap */
.mobile-flex--gap-8         /* gap: 8px */
.mobile-flex--gap-16        /* gap: 16px */
```

### CSS Variables

```css
:root {
    --mobile-topbar-height: 56px;
    --mobile-sidebar-width: 280px;
    --mobile-primary-color: #77dae6;
    --mobile-text-color: #1a162e;
    --mobile-bg-color: #ffffff;
    --mobile-border-color: #e8e8e8;
    --mobile-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    --mobile-transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
```

---

## 📱 Responsive Breakpoints

```css
/* Small phones */
@media (max-width: 374px) {
    /* Giảm padding, sidebar width */
}

/* Tablets */
@media (min-width: 768px) {
    /* Tăng padding, spacing */
}

/* Desktop */
@media (min-width: 1024px) {
    /* Ẩn mobile header, hiện desktop header */
}
```

---

## 🔧 JavaScript API

### Global Functions

```javascript
// Mở sidebar
window.openMobileSidebar();

// Đóng sidebar
window.closeMobileSidebar();
```

### Custom Events

```javascript
// Lắng nghe cart update
document.addEventListener('cartUpdated', function(e) {
    console.log('Cart count:', e.detail.count);
});

// Lắng nghe wishlist update
document.addEventListener('wishlistUpdated', function(e) {
    console.log('Wishlist count:', e.detail.count);
});
```

---

## 🎯 Tích hợp vào Project

### Bước 1: CSS đã được thêm vào `head.html`

```html
<link rel="stylesheet" th:href="@{/assets/css/mobile-layout.css}"/>
```

### Bước 2: JavaScript đã được thêm vào `head.html`

```html
<script th:src="@{/assets/js/mobile-layout.js}"></script>
```

### Bước 3: Cập nhật template

**Trong `index.html` hoặc template khác:**

```html
<!-- Thay thế dòng này -->
<header th:replace="~{shared/fragments/header :: header}"></header>

<!-- Bằng dòng này -->
<div th:replace="~{shared/fragments/responsive-header :: responsiveHeader}"></div>
```

### Bước 4: Test

1. Mở trình duyệt và truy cập: `http://localhost:8080/mobile-demo.html`
2. Resize trình duyệt để test responsive
3. Test trên mobile device hoặc Chrome DevTools

---

## 🎨 Customization

### Thay đổi màu sắc

Chỉnh sửa CSS variables trong `mobile-layout.css`:

```css
:root {
    --mobile-primary-color: #77dae6;  /* Màu chính */
    --mobile-text-color: #1a162e;     /* Màu text */
    --mobile-bg-color: #ffffff;       /* Màu nền */
}
```

### Thay đổi kích thước

```css
:root {
    --mobile-topbar-height: 56px;     /* Chiều cao topbar */
    --mobile-sidebar-width: 280px;    /* Chiều rộng sidebar */
}
```

### Thêm menu item

Chỉnh sửa `mobile-header.html`:

```html
<li class="mobile-sidebar__item">
    <a th:href="@{/your-page}" class="mobile-sidebar__link">
        <i class="bi bi-your-icon"></i>
        <span>Menu Item</span>
    </a>
</li>
```

---

## ✨ Tính năng nổi bật

### 1. Mobile-First Design
- Thiết kế ưu tiên trải nghiệm mobile
- Touch-friendly với target size >= 40px
- Smooth animations và transitions

### 2. Performance
- CSS được tối ưu với CSS Grid và Flexbox
- JavaScript chỉ load khi cần
- Lazy loading cho images (có thể thêm)

### 3. Accessibility
- Keyboard navigation support
- ARIA labels cho screen readers
- Focus styles rõ ràng
- Reduced motion support

### 4. Dark Mode Ready
- CSS variables hỗ trợ dark mode
- Tự động chuyển đổi với `html.dark`

### 5. Progressive Enhancement
- Hoạt động tốt ngay cả khi JavaScript bị tắt
- Fallback cho các tính năng không được hỗ trợ

---

## 🐛 Troubleshooting

### Sidebar không mở được

**Nguyên nhân:** JavaScript chưa load
**Giải pháp:** Kiểm tra console, đảm bảo `mobile-layout.js` được load

### Layout bị vỡ trên mobile

**Nguyên nhân:** CSS conflict với main.css
**Giải pháp:** Kiểm tra thứ tự load CSS, `mobile-layout.css` nên load sau `main.css`

### Cart count không cập nhật

**Nguyên nhân:** CartAPI chưa được khởi tạo
**Giải pháp:** Đảm bảo `cart-api.js` được load trước `mobile-layout.js`

---

## 📊 Browser Support

- ✅ Chrome/Edge (latest)
- ✅ Firefox (latest)
- ✅ Safari (iOS 12+)
- ✅ Samsung Internet
- ⚠️ IE11 (không hỗ trợ CSS Grid)

---

## 🔄 Migration Guide

### Từ Desktop Layout sang Responsive Layout

1. **Backup code hiện tại**
2. **Thêm CSS và JS** (đã hoàn thành)
3. **Thay thế header** trong từng template:
   ```html
   <div th:replace="~{shared/fragments/responsive-header :: responsiveHeader}"></div>
   ```
4. **Test từng trang**
5. **Deploy lên staging**
6. **Test trên thiết bị thật**
7. **Deploy production**

---

## 📝 Best Practices

### 1. Content Structure

```html
<main class="mobile-main-content">
    <div class="mobile-container">
        <!-- Your content here -->
    </div>
</main>
```

### 2. Grid Layout

```html
<!-- Product grid -->
<div class="mobile-grid mobile-grid--auto">
    <div class="product-card">...</div>
    <div class="product-card">...</div>
</div>
```

### 3. Flexbox Layout

```html
<!-- Header with actions -->
<div class="mobile-flex mobile-flex--between">
    <h2>Title</h2>
    <button>Action</button>
</div>
```

---

## 🎓 Examples

### Example 1: Product List Page

```html
<main class="mobile-main-content">
    <div class="mobile-container">
        <h1>Sản phẩm</h1>
        
        <div class="mobile-grid mobile-grid--auto">
            <div th:each="product : ${products}">
                <!-- Product card -->
            </div>
        </div>
    </div>
</main>
```

### Example 2: Profile Page

```html
<main class="mobile-main-content">
    <div class="mobile-container">
        <div class="mobile-flex mobile-flex--column mobile-flex--gap-16">
            <section><!-- User info --></section>
            <section><!-- Orders --></section>
            <section><!-- Settings --></section>
        </div>
    </div>
</main>
```

---

## 📞 Support

Nếu gặp vấn đề, vui lòng:
1. Kiểm tra console log
2. Xem file demo: `/mobile-demo.html`
3. Đọc lại documentation
4. Liên hệ team support

---

## 🎉 Kết luận

Layout mới đã sẵn sàng sử dụng! Bạn có thể:

1. ✅ Test ngay tại: `http://localhost:8080/mobile-demo.html`
2. ✅ Tích hợp vào các trang hiện tại
3. ✅ Customize theo nhu cầu
4. ✅ Deploy lên production

**Happy coding! 🚀**
