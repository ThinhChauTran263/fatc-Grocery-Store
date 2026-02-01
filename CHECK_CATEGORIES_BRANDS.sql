-- Kiểm tra dữ liệu categories
SELECT 
    id,
    name,
    slug,
    is_active,
    display_order,
    parent_id
FROM categories
ORDER BY display_order, id;

-- Kiểm tra dữ liệu brands
SELECT 
    id,
    name,
    slug,
    is_active
FROM brands
ORDER BY name;

-- Kiểm tra số lượng categories và brands active
SELECT 
    (SELECT COUNT(*) FROM categories WHERE is_active = true) as active_categories,
    (SELECT COUNT(*) FROM brands WHERE is_active = true) as active_brands;

-- Kiểm tra products có category và brand không
SELECT 
    p.id,
    p.name,
    p.category_id,
    c.name as category_name,
    p.brand_id,
    b.name as brand_name,
    p.is_active
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN brands b ON p.brand_id = b.id
ORDER BY p.id;

-- Fix categories nếu is_active = null
UPDATE categories SET is_active = true WHERE is_active IS NULL;

-- Fix brands nếu is_active = null
UPDATE brands SET is_active = true WHERE is_active IS NULL;
