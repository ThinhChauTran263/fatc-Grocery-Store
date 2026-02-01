# 🎯 IMPORT DỮ LIỆU MẪU VÀO RENDER

## Vấn đề
Form "Thêm Sản Phẩm" không có danh mục và thương hiệu để chọn vì database trống.

## Giải pháp - Import dữ liệu mẫu

### Bước 1: Truy cập Render Database
1. Vào https://dashboard.render.com
2. Chọn database **java5_asm**
3. Click tab **"Connect"** hoặc **"Shell"**

### Bước 2: Copy SQL từ file
Mở file `mariadb_init/postgresql-data.sql` và copy toàn bộ nội dung

### Bước 3: Paste và chạy
Paste vào console Render và Enter

## Dữ liệu sẽ được import:

### ✅ Categories (Danh mục):
- Departments
- Grocery  
- Beauty
- Coffee
- Electronics
- Clothing
- Coffee Beans
- Ground Coffee
- Instant Coffee

### ✅ Brands (Thương hiệu):
- Lavazza
- welikecoffee
- Starbucks
- Nescafe
- Trung Nguyen

### ✅ Products (10 sản phẩm mẫu):
- Coffee Beans - Espresso Arabica
- Lavazza Coffee Blends
- Lavazza Caffè Espresso
- Starbucks Pike Place
- Trung Nguyen Creative 3
- Nescafe Gold
- Lavazza Qualità Rossa
- Starbucks French Roast
- Trung Nguyen Gourmet
- Nescafe Classic

### ✅ Users:
- **admin** / admin123 (ADMIN)
- imrankhan / admin123 (USER)
- johnsmith / admin123 (USER)
- maryjane / admin123 (USER)

## Sau khi import xong:

1. Refresh trang admin
2. Form "Thêm Sản Phẩm" sẽ có đầy đủ danh mục và thương hiệu
3. Có thể thêm sản phẩm mới ngay!

---

## Nếu gặp lỗi "duplicate key"

Không sao! Nghĩa là dữ liệu đã tồn tại. Chỉ cần refresh trang là được.

## Kiểm tra nhanh

Chạy query này để xem có bao nhiêu categories và brands:

```sql
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as total_brands FROM brands;
SELECT COUNT(*) as total_products FROM products;
```

Kết quả mong đợi:
- Categories: 9
- Brands: 5  
- Products: 10
