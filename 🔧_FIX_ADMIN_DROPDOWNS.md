# 🔧 Fix: Admin Product Form Dropdowns

## Vấn đề
Dropdown "Danh mục" và "Thương hiệu" trong form thêm sản phẩm không có dữ liệu.

## Nguyên nhân
Frontend đang gọi sai endpoints:
- ❌ Cũ: `/api/categories` và `/api/brands` (không tồn tại)
- ✅ Mới: `/api/admin/products/categories` và `/api/admin/products/brands`

## Đã fix
- ✅ Update `loadCategories()` gọi đúng endpoint
- ✅ Update `loadBrands()` gọi đúng endpoint
- ✅ Thêm check `data.success` và `data.data` (vì API trả về wrapped response)
- ✅ Thêm error logging rõ ràng hơn

## Commit
```
6901ed5 - fix: Update admin product form to use correct API endpoints for categories and brands
```

## Test Local (Nhanh)

### Cách 1: Test trực tiếp trên Render (sau khi deploy)
1. Deploy code mới lên Render
2. Login admin: https://your-app.onrender.com/admin/login
3. Vào **Quản lý sản phẩm**
4. Click **Thêm sản phẩm**
5. Check dropdown "Danh mục" và "Thương hiệu" có data

### Cách 2: Test local
```bash
# Pull code mới
git pull origin ThinhDev

# Build và run
mvnw.cmd clean spring-boot:run

# Mở browser
http://localhost:8080/admin/login
```

Login với:
- Username: `admin`
- Password: `admin123`

## Kết quả mong đợi

### Dropdown "Danh mục" sẽ có:
1. Coffee
2. Tea
3. Milk & Dairy
4. Snacks
5. Beverages
6. Fresh Produce
7. Bakery
8. Frozen Foods
9. Household

### Dropdown "Thương hiệu" sẽ có:
1. Lavazza
2. Starbucks
3. Trung Nguyen
4. Nescafe
5. Vinamilk

## API Response Format

Endpoints trả về format:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Coffee",
      "slug": "coffee",
      "productCount": 5
    }
  ]
}
```

Code đã được update để parse đúng format này.

## Nếu vẫn không có data

### Check 1: Verify API hoạt động
Mở browser console (F12) và test:
```javascript
fetch('/api/admin/products/categories')
  .then(r => r.json())
  .then(data => console.log(data));
```

Phải trả về `{success: true, data: [...]}`

### Check 2: Verify database
Chạy trong PostgreSQL:
```sql
SELECT COUNT(*) FROM categories WHERE is_active = true;
SELECT COUNT(*) FROM brands WHERE is_active = true;
```

Phải có >= 9 categories và >= 5 brands

### Check 3: Check browser console
Mở F12 → Console tab, xem có error gì không.

## Deploy lên Render

1. Vào Render Dashboard
2. Manual Deploy → Deploy latest commit
3. Đợi build xong
4. Restart service
5. Test form thêm sản phẩm

---
**Commit**: `6901ed5`  
**Files changed**: `products.html`  
**Status**: Ready to deploy 🚀
