-- ============================================
-- FIX: PostgreSQL bytea type error
-- ============================================
-- Lỗi: operator does not exist: character varying ~~ bytea
-- Nguyên nhân: Có thể do data bị insert sai kiểu hoặc cache cũ

-- BƯỚC 1: Kiểm tra data types hiện tại
SELECT column_name, data_type, character_maximum_length
FROM information_schema.columns 
WHERE table_name = 'products' 
AND column_name IN ('name', 'description', 'short_description', 'tags', 'search_keywords')
ORDER BY ordinal_position;

-- BƯỚC 2: Kiểm tra có data nào bị lỗi không
SELECT id, name, 
       pg_typeof(description) as desc_type,
       pg_typeof(tags) as tags_type,
       pg_typeof(search_keywords) as keywords_type
FROM products 
LIMIT 5;

-- BƯỚC 3: Nếu cần, force cast lại data type
-- Chỉ chạy nếu thấy có column bị bytea

-- ALTER TABLE products ALTER COLUMN description TYPE TEXT USING description::TEXT;
-- ALTER TABLE products ALTER COLUMN tags TYPE VARCHAR(500) USING tags::VARCHAR(500);
-- ALTER TABLE products ALTER COLUMN search_keywords TYPE TEXT USING search_keywords::TEXT;

-- BƯỚC 4: Update featured products để test
UPDATE products 
SET is_featured = true 
WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8)
AND is_active = true;

-- BƯỚC 5: Verify featured products
SELECT id, name, is_featured, is_active, 
       SUBSTRING(description, 1, 50) as desc_preview
FROM products 
WHERE is_featured = true AND is_active = true;

-- BƯỚC 6: Test query giống như trong code
SELECT id, name, price, image_url
FROM products 
WHERE is_featured = true AND is_active = true
LIMIT 8;
