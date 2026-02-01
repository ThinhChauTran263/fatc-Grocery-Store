# 🔧 Fix Categories & Brands Không Hiển Thị

## Vấn đề
- Trang chủ không hiển thị danh mục (categories)
- Dropdown trong admin form không có dữ liệu

## Nguyên nhân
1. Dữ liệu categories/brands có `is_active = null` hoặc `false`
2. Cache đang giữ dữ liệu cũ (rỗng)

## Giải pháp

### Bước 1: Fix Database
Chạy SQL trong Render Console:

```sql
-- Fix categories
UPDATE categories SET is_active = true WHERE is_active IS NULL OR is_active = false;

-- Fix brands  
UPDATE brands SET is_active = true WHERE is_active IS NULL OR is_active = false;

-- Verify
SELECT 
    (SELECT COUNT(*) FROM categories WHERE is_active = true) as active_categories,
    (SELECT COUNT(*) FROM brands WHERE is_active = true) as active_brands;
```

### Bước 2: Clear Cache
Sau khi fix database, **PHẢI restart service** để clear cache:

1. Vào Render Dashboard
2. Chọn service của bạn
3. Click **Manual Deploy** > **Clear build cache & deploy**

HOẶC

Click **Restart** để restart service nhanh hơn

### Bước 3: Kiểm tra
1. Mở trang chủ - kiểm tra categories có hiển thị không
2. Vào admin form - kiểm tra dropdown có dữ liệu không

## File SQL Hỗ Trợ
- `FIX_CATEGORIES_BRANDS.sql` - Script fix đầy đủ với kiểm tra
- `CHECK_CATEGORIES_BRANDS.sql` - Script kiểm tra dữ liệu

## Lưu ý
- Cache được config trong `CacheConfig.java`
- Categories cache key: `CacheNames.CATEGORIES`
- Brands cache key: `CacheNames.BRANDS`
- Cache sẽ tự động clear sau 1 giờ, nhưng restart nhanh hơn
