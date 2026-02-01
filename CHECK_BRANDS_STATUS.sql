-- Kiểm tra trạng thái brands trong database

-- 1. Xem tất cả brands và trạng thái
SELECT id, name, slug, is_active, created_at
FROM brands
ORDER BY id;

-- 2. Đếm brands theo trạng thái
SELECT 
    is_active,
    COUNT(*) as count
FROM brands
GROUP BY is_active;

-- 3. Nếu cần, active tất cả brands
-- CHẠY CÁI NÀY NẾU CÓ BRANDS BỊ is_active = false
UPDATE brands 
SET is_active = true 
WHERE is_active = false OR is_active IS NULL;

-- 4. Verify sau khi update
SELECT id, name, is_active 
FROM brands 
ORDER BY id;
