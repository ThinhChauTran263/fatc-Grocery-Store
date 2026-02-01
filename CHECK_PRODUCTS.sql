-- Kiểm tra dữ liệu trong bảng products
SELECT 
    id,
    name,
    slug,
    price,
    is_active,
    is_featured,
    category_id,
    brand_id,
    stock_quantity
FROM products
ORDER BY id;

-- Kiểm tra encoding của các cột text
SELECT 
    id,
    name,
    octet_length(name) as name_bytes,
    char_length(name) as name_chars,
    description,
    octet_length(description) as desc_bytes
FROM products
LIMIT 5;

-- Test query đơn giản không dùng LOWER
SELECT id, name, price, is_active, is_featured
FROM products
WHERE is_active = true AND is_featured = true;

-- Test query với CAST
SELECT id, name, price
FROM products
WHERE is_active = true 
  AND LOWER(CAST(name AS TEXT)) LIKE '%coffee%';
