-- ============================================
-- FULL IMPORT FOR RENDER POSTGRESQL
-- ============================================
-- Copy TOÀN BỘ file này và paste vào SQLite Online
-- Hoặc chạy: psql -h ... -U java5_user -d java5_asm -f IMPORT_DATA_RENDER.sql

-- ============================================
-- 1. USERS
-- ============================================
INSERT INTO users (username, email, password, full_name, phone, registered_date, role, provider, is_active, created_at, updated_at) VALUES
('admin', 'admin@grocerystore.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'Admin User', '0123456789', '2022-01-01', 'ADMIN', 'local', true, NOW(), NOW()),
('imrankhan', 'imran@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'Imran Khan', '0987654321', '2022-05-17', 'USER', 'local', true, NOW(), NOW()),
('johnsmith', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'John Smith', '0912345678', '2023-03-10', 'USER', 'local', true, NOW(), NOW()),
('maryjane', 'mary@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'Mary Jane', '0923456789', '2023-06-20', 'USER', 'local', true, NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

-- ============================================
-- 2. CATEGORIES
-- ============================================
INSERT INTO categories (name, slug, description, display_order, parent_id, created_at, updated_at) VALUES
('Departments', 'departments', 'All departments', 1, NULL, NOW(), NOW()),
('Grocery', 'grocery', 'Grocery items', 2, NULL, NOW(), NOW()),
('Beauty', 'beauty', 'Beauty products', 3, NULL, NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;

-- Get category IDs for subcategories
INSERT INTO categories (name, slug, description, display_order, parent_id, created_at, updated_at) 
SELECT 'Coffee', 'coffee', 'Coffee products', 1, id, NOW(), NOW()
FROM categories WHERE slug = 'departments'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO categories (name, slug, description, display_order, parent_id, created_at, updated_at) 
SELECT 'Coffee Beans', 'coffee-beans', 'Whole coffee beans', 1, id, NOW(), NOW()
FROM categories WHERE slug = 'coffee'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO categories (name, slug, description, display_order, parent_id, created_at, updated_at) 
SELECT 'Ground Coffee', 'ground-coffee', 'Pre-ground coffee', 2, id, NOW(), NOW()
FROM categories WHERE slug = 'coffee'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO categories (name, slug, description, display_order, parent_id, created_at, updated_at) 
SELECT 'Instant Coffee', 'instant-coffee', 'Instant coffee', 3, id, NOW(), NOW()
FROM categories WHERE slug = 'coffee'
ON CONFLICT (slug) DO NOTHING;

-- ============================================
-- 3. BRANDS
-- ============================================
INSERT INTO brands (name, slug, description, created_at, updated_at) VALUES
('Lavazza', 'lavazza', 'Italian coffee brand', NOW(), NOW()),
('welikecoffee', 'welikecoffee', 'Premium coffee brand', NOW(), NOW()),
('Starbucks', 'starbucks', 'Global coffee chain', NOW(), NOW()),
('Nescafe', 'nescafe', 'Instant coffee brand', NOW(), NOW()),
('Trung Nguyen', 'trung-nguyen', 'Vietnamese coffee brand', NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;

-- ============================================
-- 4. PRODUCTS
-- ============================================
INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) 
SELECT 
    'Coffee Beans - Espresso Arabica and Robusta Beans', 
    'coffee-beans-espresso-arabica-robusta', 
    'Premium blend of Arabica and Robusta coffee beans, perfect for espresso lovers. Rich, full-bodied flavor with notes of chocolate and caramel.', 
    'Premium espresso blend with rich flavor', 
    1175000, NULL, 
    c.id, b.id, 
    100, 'CB-ESP-001', 
    'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419600/products/coffee-beans-1.jpg', 
    true, NOW(), NOW()
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'lavazza'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) 
SELECT 
    'Lavazza Coffee Blends - Try the Italian Espresso', 
    'lavazza-coffee-blends-italian-espresso',
    'Authentic Italian espresso blend from Lavazza. Medium roast with balanced acidity and smooth finish.',
    'Authentic Italian espresso blend',
    1325000, 1225000, 
    c.id, b.id, 
    80, 'CB-LAV-002', 
    'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419601/products/lavazza-espresso.jpg', 
    true, NOW(), NOW()
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'lavazza'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) 
SELECT 
    'Lavazza - Caffè Espresso Black Tin - Ground coffee', 
    'lavazza-caffe-espresso-black-tin',
    'Classic Lavazza espresso in convenient ground form. Intense flavor with a velvety crema.',
    'Classic ground espresso coffee',
    2499000, NULL, 
    c.id, b.id, 
    50, 'GC-LAV-003', 
    'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419602/products/lavazza-ground.jpg', 
    true, NOW(), NOW()
FROM categories c, brands b
WHERE c.slug = 'ground-coffee' AND b.slug = 'lavazza'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) 
SELECT 
    'Starbucks Pike Place Roast', 
    'starbucks-pike-place-roast',
    'Smooth and balanced medium roast coffee. Perfect for everyday brewing.',
    'Smooth medium roast coffee',
    800000, 700000, 
    c.id, b.id, 
    120, 'CB-STA-004', 
    'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419603/products/starbucks-pike.jpg', 
    false, NOW(), NOW()
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'starbucks'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) 
SELECT 
    'Trung Nguyen Creative 3', 
    'trung-nguyen-creative-3',
    'Vietnamese premium coffee blend. Bold and aromatic with unique flavor profile.',
    'Vietnamese premium blend',
    1125000, NULL, 
    c.id, b.id, 
    90, 'CB-TRN-005', 
    'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419604/products/trung-nguyen.jpg', 
    true, NOW(), NOW()
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'trung-nguyen'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) 
SELECT 
    'Nescafe Gold Instant Coffee', 
    'nescafe-gold-instant-coffee',
    'Premium instant coffee with rich aroma. Quick and convenient.',
    'Premium instant coffee',
    600000, NULL, 
    c.id, b.id, 
    200, 'IC-NES-006', 
    'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419605/products/nescafe-gold.jpg', 
    false, NOW(), NOW()
FROM categories c, brands b
WHERE c.slug = 'instant-coffee' AND b.slug = 'nescafe'
ON CONFLICT (slug) DO NOTHING;

-- ============================================
-- COMPLETED
-- ============================================
-- Refresh website: https://fatc-grocery-store.onrender.com
-- Login: admin / admin123
