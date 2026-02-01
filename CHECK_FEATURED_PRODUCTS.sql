-- Kiểm tra sản phẩm featured trên Render
-- Chạy trong Render PostgreSQL Console

-- 1. Kiểm tra có bao nhiêu products featured
SELECT COUNT(*) as featured_count 
FROM products 
WHERE is_featured = true AND is_active = true;

-- 2. Xem chi tiết products featured
SELECT id, name, is_featured, is_active, price, image_url
FROM products 
WHERE is_featured = true AND is_active = true
LIMIT 10;

-- 3. Nếu không có products featured, update một số products
-- CHẠY CÁI NÀY NẾU COUNT = 0
UPDATE products 
SET is_featured = true 
WHERE id IN (1, 2, 3, 4, 5)
AND is_active = true;

-- 4. Kiểm tra lại sau khi update
SELECT COUNT(*) as featured_count_after_update
FROM products 
WHERE is_featured = true AND is_active = true;
