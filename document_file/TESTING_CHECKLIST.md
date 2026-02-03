# ✅ Checklist Kiểm tra - Testing Guide

## 🎯 Kiểm tra nhanh (5 phút)

### 1. Reset Button Active State
```
□ Vào trang chủ (localhost:8080/)
□ Bấm nút "Bộ lọc"
□ Chọn rating "4+ ⭐"
□ Bấm nút "Đặt lại"
□ ✅ Kiểm tra: Nút "Tất cả" phải có màu xanh (active)
□ ✅ Kiểm tra: Popup filter tự động đóng
```

### 2. Mobile UI
```
□ Mở Chrome DevTools (F12)
□ Bấm Ctrl+Shift+M (toggle device toolbar)
□ Chọn "iPhone 12 Pro"
□ ✅ Kiểm tra: Scroll mượt mà, không giật
□ ✅ Kiểm tra: Buttons đủ lớn để bấm (44px)
□ ✅ Kiểm tra: Filter sidebar full screen khi mở
□ ✅ Kiểm tra: Không có horizontal scroll
```

### 3. FOUC Prevention
```
□ Mở Incognito mode (Ctrl+Shift+N)
□ Vào localhost:8080/
□ Bấm Ctrl+Shift+R (hard refresh)
□ ✅ Kiểm tra: Không thấy flash trắng
□ ✅ Kiểm tra: Trang fade in mượt mà
□ ✅ Kiểm tra: Font load ngay, không nhảy
```

### 4. Language Consistency
```
□ Kiểm tra trang chủ (/)
□ Kiểm tra trang sản phẩm (/products)
□ Kiểm tra trang chi tiết (/product/1)
□ ✅ Kiểm tra: Tất cả text hiển thị là tiếng Việt
□ ✅ Kiểm tra: Buttons, labels đều tiếng Việt
□ ✅ Kiểm tra: Thông báo, errors đều tiếng Việt
```

---

## 🔍 Kiểm tra chi tiết (15 phút)

### A. Desktop Testing

#### Trang chủ (/)
```
□ Slideshow tự động chạy
□ Quick categories hiển thị đúng
□ Sản phẩm được đánh giá cao load đúng
□ Sản phẩm nổi bật load đúng
□ Pagination hoạt động
□ Filter popup mở/đóng mượt mà
□ Rating filter hoạt động (3+, 4+, 4.5+)
□ Price slider hoạt động
□ Wishlist checkbox hoạt động
□ Reset button hoạt động đúng
```

#### Trang sản phẩm (/products)
```
□ Products load đúng
□ Filter sidebar sticky
□ Brand filter hoạt động
□ Price filter hoạt động (auto-apply)
□ Rating filter hoạt động
□ Sort dropdown hoạt động
□ Pagination hoạt động
□ Scroll to top khi filter
□ Product cards hover effect
□ Wishlist toggle hoạt động
□ Add to cart hoạt động
```

#### Trang chi tiết (/product/1)
```
□ Product info hiển thị đúng
□ Images load đúng
□ Reviews hiển thị đúng
□ Rating summary đúng
□ Add to cart hoạt động
□ Wishlist toggle hoạt động
□ Related products hiển thị
```

### B. Mobile Testing (iPhone 12 Pro - 390x844)

#### Responsive Layout
```
□ Header responsive
□ Navigation menu mobile
□ Product grid: 2 columns
□ Quick categories: 2 columns
□ Filter sidebar: full screen overlay
□ Footer responsive
□ Pagination: smaller buttons
□ Touch targets: minimum 44px
```

#### Performance
```
□ Scroll mượt mà (60fps)
□ Animations không lag
□ Images load nhanh
□ Filter open/close mượt
□ No horizontal scroll
□ No layout shift
```

#### Interactions
```
□ Tap buttons dễ dàng
□ Swipe slideshow hoạt động
□ Filter overlay có backdrop
□ Close filter bằng backdrop
□ Wishlist toggle instant feedback
□ Add to cart instant feedback
```

### C. Tablet Testing (iPad - 768x1024)

```
□ Layout 3 columns cho products
□ Filter sidebar visible
□ Touch targets đủ lớn
□ Spacing hợp lý
□ Typography readable
```

### D. Small Mobile (iPhone SE - 375x667)

```
□ Layout không bị vỡ
□ Text không bị cắt
□ Buttons không overlap
□ Images scale đúng
□ Filter popup fit screen
```

---

## 🎨 Dark Mode Testing

```
□ Toggle dark mode
□ Tất cả colors đổi đúng
□ Contrast đủ cao
□ Shadows visible
□ Borders visible
□ Text readable
□ Icons visible
□ Hover effects hoạt động
```

---

## ⚡ Performance Testing

### Chrome DevTools Lighthouse

```
□ Mở DevTools (F12)
□ Tab "Lighthouse"
□ Chọn "Mobile"
□ Chọn "Performance"
□ Bấm "Analyze page load"

✅ Mục tiêu:
  - Performance: > 90
  - Accessibility: > 95
  - Best Practices: > 90
  - SEO: > 90
```

### Network Throttling

```
□ DevTools > Network tab
□ Chọn "Fast 3G"
□ Refresh trang
□ ✅ Kiểm tra: Trang vẫn load < 3s
□ ✅ Kiểm tra: Critical content hiển thị ngay
□ ✅ Kiểm tra: Skeleton loading hoạt động
```

---

## ♿ Accessibility Testing

### Keyboard Navigation

```
□ Bấm Tab để navigate
□ ✅ Focus indicators rõ ràng
□ ✅ Tất cả buttons accessible
□ ✅ Tất cả links accessible
□ ✅ Filter popup accessible
□ ✅ Modal accessible
□ Bấm Enter để activate
□ Bấm Escape để close
```

### Screen Reader (Optional)

```
□ Bật Windows Narrator (Win+Ctrl+Enter)
□ Navigate qua trang
□ ✅ Alt text cho images
□ ✅ Labels cho form fields
□ ✅ Headings có hierarchy
□ ✅ Buttons có descriptive text
```

---

## 🌐 Browser Compatibility

### Chrome (Latest)
```
□ Desktop: All features work
□ Mobile: All features work
□ DevTools: No console errors
```

### Firefox (Latest)
```
□ Desktop: All features work
□ Mobile: All features work
□ Scrollbar styling correct
```

### Safari (Latest)
```
□ Desktop: All features work
□ iOS: All features work
□ Font rendering correct
□ Input styling correct
```

### Edge (Latest)
```
□ Desktop: All features work
□ Flexbox layout correct
□ Animations smooth
```

---

## 🐛 Common Issues to Check

### Layout Issues
```
□ No horizontal scroll
□ No overlapping elements
□ No cut-off text
□ No broken images
□ No layout shift on load
```

### Performance Issues
```
□ No lag when scrolling
□ No lag when filtering
□ No lag when opening modals
□ Images load progressively
□ Fonts load without flash
```

### Interaction Issues
```
□ All buttons clickable
□ All links work
□ Forms submit correctly
□ Filters apply correctly
□ Pagination works
□ Wishlist toggle works
□ Cart add works
```

---

## 📊 Performance Metrics

### Target Metrics
```
✅ First Contentful Paint (FCP): < 1.8s
✅ Largest Contentful Paint (LCP): < 2.5s
✅ Time to Interactive (TTI): < 3.8s
✅ Cumulative Layout Shift (CLS): < 0.1
✅ First Input Delay (FID): < 100ms
```

### How to Measure
```
1. Open Chrome DevTools
2. Go to "Performance" tab
3. Click "Record" button
4. Refresh page
5. Stop recording after page loads
6. Check metrics in summary
```

---

## ✅ Final Checklist

### Before Deployment
```
□ All tests passed
□ No console errors
□ No console warnings
□ Performance score > 90
□ Accessibility score > 95
□ Mobile UI tested on real device
□ Dark mode works correctly
□ All browsers tested
□ FOUC not visible
□ Language 100% Vietnamese
```

### After Deployment
```
□ Test on production URL
□ Test with real users
□ Monitor performance metrics
□ Check error logs
□ Verify analytics tracking
```

---

## 🎯 Quick Test Script

Copy-paste vào console để test nhanh:

```javascript
// Test 1: Check if all Vietnamese
console.log('🇻🇳 Language Test:');
const allText = document.body.innerText;
const englishWords = ['Option', 'Select', 'Choose', 'Loading', 'Error', 'Success'];
const found = englishWords.filter(word => allText.includes(word));
console.log(found.length === 0 ? '✅ All Vietnamese' : '❌ Found English: ' + found);

// Test 2: Check mobile optimizations
console.log('\n📱 Mobile Test:');
const isMobile = window.innerWidth < 768;
console.log('Is Mobile:', isMobile);
console.log('Touch Targets:', document.querySelectorAll('button, .btn, a').length);

// Test 3: Check performance
console.log('\n⚡ Performance Test:');
console.log('DOM Nodes:', document.querySelectorAll('*').length);
console.log('Images:', document.querySelectorAll('img').length);
console.log('Scripts:', document.querySelectorAll('script').length);

// Test 4: Check FOUC prevention
console.log('\n🎨 FOUC Test:');
const hasCriticalCSS = document.head.innerHTML.includes('Critical CSS');
console.log(hasCriticalCSS ? '✅ Critical CSS found' : '❌ No critical CSS');

console.log('\n✅ Quick test complete!');
```

---

## 📞 Support

Nếu gặp vấn đề:
1. Check console errors (F12)
2. Check network tab (slow requests?)
3. Check mobile view (responsive issues?)
4. Clear cache and retry (Ctrl+Shift+R)
5. Test in incognito mode (extension conflicts?)

**Tất cả tests phải PASS trước khi deploy! ✅**
