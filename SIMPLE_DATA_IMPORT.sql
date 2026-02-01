-- ============================================
-- SIMPLE DATA IMPORT (Khớp với Hibernate schema)
-- ============================================

-- 1. USERS (đã có rồi, skip)

-- 2. CATEGORIES (không có updated_at)
INSERT INTO categories (name, slug, description, display_order, parent_id) VALUES
('Departments', 'departments', 'All departments', 1, NULL),
('Grocery', 'grocery', 'Grocery items', 2, NULL),
('Beauty', 'beauty', 'Beauty products', 3, NULL),
('Coffee', 'coffee', 'Coffee products', 1, 1),
('Coffee Beans', 'coffee-beans', 'Whole coffee beans', 1, 4),
('Ground Coffee', 'ground-coffee', 'Pre-ground coffee', 2, 4),
('Instant Coffee', 'instant-coffee', 'Instant coffee', 3, 4)
ON CONFLICT (slug) DO NOTHING;

-- 3. BRANDS
INSERT INTO brands (name, slug, description) VALUES
('Lavazza', 'lavazza', 'Italian coffee brand'),
('Starbucks', 'starbucks', 'Global coffee chain'),
('Nescafe', 'nescafe', 'Instant coffee brand'),
('Trung Nguyen', 'trung-nguyen', 'Vietnamese coffee brand')
ON CONFLICT (slug) DO NOTHING;

-- 4. PRODUCTS (dùng ID thực tế từ categories và brands)
INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured) 
SELECT 
    'Coffee Beans - Espresso Arabica', 
    'coffee-beans-espresso-arabica', 
    'Premium blend of Arabica and Robusta coffee beans', 
    'Premium espresso blend', 
    1175000, NULL, 
    c.id, b.id, 
    100, 'CB-ESP-001', 
    'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=500', 
    true
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'lavazza'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured) 
SELECT 
    'Lavazza Italian Espresso', 
    'lavazza-italian-espresso',
    'Authentic Italian espresso blend',
    'Italian espresso',
    1325000, 1225000, 
    c.id, b.id, 
    80, 'CB-LAV-002', 
    'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=500', 
    true
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'lavazza'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured) 
SELECT 
    'Starbucks Pike Place Roast', 
    'starbucks-pike-place',
    'Smooth medium roast coffee',
    'Medium roast',
    800000, 700000, 
    c.id, b.id, 
    120, 'CB-STA-003', 
    'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=500', 
    true
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'starbucks'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured) 
SELECT 
    'Trung Nguyen Creative 3', 
    'trung-nguyen-creative-3',
    'Vietnamese premium coffee',
    'Vietnamese blend',
    1125000, NULL, 
    c.id, b.id, 
    90, 'CB-TRN-004', 
    'https://images.unsplash.com/photo-1447933601403-0c6688de566e?w=500', 
    true
FROM categories c, brands b
WHERE c.slug = 'coffee-beans' AND b.slug = 'trung-nguyen'
ON CONFLICT (slug) DO NOTHING;

INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured) 
SELECT 
    'Nescafe Gold Instant', 
    'nescafe-gold-instant',
    'Premium instant coffee',
    'Instant coffee',
    600000, NULL, 
    c.id, b.id, 
    200, 'IC-NES-005', 
    'https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=500', 
    false
FROM categories c, brands b
WHERE c.slug = 'instant-coffee' AND b.slug = 'nescafe'
ON CONFLICT (slug) DO NOTHING;

-- DONE!
-- Refresh: https://fatc-grocery-store.onrender.com
