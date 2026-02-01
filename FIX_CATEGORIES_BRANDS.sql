-- ============================================
-- FIX CATEGORIES AND BRANDS FOR RENDER
-- ============================================

-- 1. Kiểm tra trạng thái hiện tại
SELECT 'BEFORE FIX - Categories' as info;
SELECT id, name, is_active, display_order FROM categories ORDER BY id;

SELECT 'BEFORE FIX - Brands' as info;
SELECT id, name, is_active FROM brands ORDER BY id;

-- 2. Fix categories - set is_active = true cho tất cả
UPDATE categories SET is_active = true WHERE is_active IS NULL OR is_active = false;

-- 3. Fix brands - set is_active = true cho tất cả
UPDATE brands SET is_active = true WHERE is_active IS NULL OR is_active = false;

-- 4. Kiểm tra sau khi fix
SELECT 'AFTER FIX - Categories' as info;
SELECT id, name, is_active, display_order FROM categories ORDER BY id;

SELECT 'AFTER FIX - Brands' as info;
SELECT id, name, is_active FROM brands ORDER BY id;

-- 5. Verify count
SELECT 
    (SELECT COUNT(*) FROM categories WHERE is_active = true) as active_categories,
    (SELECT COUNT(*) FROM brands WHERE is_active = true) as active_brands,
    (SELECT COUNT(*) FROM products WHERE is_active = true) as active_products;
