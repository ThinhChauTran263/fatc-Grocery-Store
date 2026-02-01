-- ============================================
-- SAMPLE DATA FOR GROCERY STORE (PostgreSQL)
-- ============================================
-- Chạy file này trong Render PostgreSQL để import data mẫu

-- ============================================
-- 1. USERS
-- ============================================
INSERT INTO users (username, email, password, full_name, phone, role, oauth_provider, is_active, created_at) VALUES
('admin', 'admin@grocerystore.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'Admin User', '0123456789', 'ADMIN', 'local', true, '2022-01-01'),
('imrankhan', 'imran@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'Imran Khan', '0987654321', 'USER', 'local', true, '2022-05-17'),
('johnsmith', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'John Smith', '0912345678', 'USER', 'local', true, '2023-03-10'),
('maryjane', 'mary@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy', 'Mary Jane', '0923456789', 'USER', 'local', true, '2023-06-20')
ON CONFLICT (username) DO NOTHING;
-- Password for all: admin123

-- ============================================
-- 2. CATEGORIES
-- ============================================
INSERT INTO categories (name, slug, description, display_order, parent_id) VALUES
('Departments', 'departments', 'All departments', 1, NULL),
('Grocery', 'grocery', 'Grocery items', 2, NULL),
('Beauty', 'beauty', 'Beauty products', 3, NULL),
('Coffee', 'coffee', 'Coffee products', 1, 1),
('Electronics', 'electronics', 'Electronic devices', 2, 1),
('Clothing', 'clothing', 'Clothing and accessories', 3, 1),
('Coffee Beans', 'coffee-beans', 'Whole coffee beans', 1, 4),
('Ground Coffee', 'ground-coffee', 'Pre-ground coffee', 2, 4),
('Instant Coffee', 'instant-coffee', 'Instant coffee', 3, 4)
ON CONFLICT (slug) DO NOTHING;

-- ============================================
-- 3. BRANDS
-- ============================================
INSERT INTO brands (name, slug, description) VALUES
('Lavazza', 'lavazza', 'Italian coffee brand'),
('welikecoffee', 'welikecoffee', 'Premium coffee brand'),
('Starbucks', 'starbucks', 'Global coffee chain'),
('Nescafe', 'nescafe', 'Instant coffee brand'),
('Trung Nguyen', 'trung-nguyen', 'Vietnamese coffee brand')
ON CONFLICT (slug) DO NOTHING;

-- ============================================
-- 4. PRODUCTS (VND Prices)
-- ============================================
INSERT INTO products (name, slug, description, short_description, price, discount_price, category_id, brand_id, stock_quantity, sku, image_url, is_featured, created_at, updated_at) VALUES
('Coffee Beans - Espresso Arabica and Robusta Beans', 'coffee-beans-espresso-arabica-robusta', 
'Premium blend of Arabica and Robusta coffee beans, perfect for espresso lovers. Rich, full-bodied flavor with notes of chocolate and caramel.', 
'Premium espresso blend with rich flavor', 
1175000, NULL, 7, 1, 100, 'CB-ESP-001', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419600/products/coffee-beans-1.jpg', true, NOW(), NOW()),

('Lavazza Coffee Blends - Try the Italian Espresso', 'lavazza-coffee-blends-italian-espresso',
'Authentic Italian espresso blend from Lavazza. Medium roast with balanced acidity and smooth finish.',
'Authentic Italian espresso blend',
1325000, 1225000, 7, 1, 80, 'CB-LAV-002', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419601/products/lavazza-espresso.jpg', true, NOW(), NOW()),

('Lavazza - Caffè Espresso Black Tin - Ground coffee', 'lavazza-caffe-espresso-black-tin',
'Classic Lavazza espresso in convenient ground form. Intense flavor with a velvety crema.',
'Classic ground espresso coffee',
2499000, NULL, 8, 1, 50, 'GC-LAV-003', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419602/products/lavazza-ground.jpg', true, NOW(), NOW()),

('Starbucks Pike Place Roast', 'starbucks-pike-place-roast',
'Smooth and balanced medium roast coffee. Perfect for everyday brewing.',
'Smooth medium roast coffee',
800000, 700000, 7, 3, 120, 'CB-STA-004', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419603/products/starbucks-pike.jpg', false, NOW(), NOW()),

('Trung Nguyen Creative 3', 'trung-nguyen-creative-3',
'Vietnamese premium coffee blend. Bold and aromatic with unique flavor profile.',
'Vietnamese premium blend',
1125000, NULL, 7, 5, 90, 'CB-TRN-005', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419604/products/trung-nguyen.jpg', true, NOW(), NOW()),

('Nescafe Gold Instant Coffee', 'nescafe-gold-instant-coffee',
'Premium instant coffee with rich aroma. Quick and convenient.',
'Premium instant coffee',
600000, NULL, 9, 4, 200, 'IC-NES-006', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419605/products/nescafe-gold.jpg', false, NOW(), NOW()),

('Lavazza Qualità Rossa', 'lavazza-qualita-rossa',
'Medium roast blend with chocolate notes. Perfect for moka pot.',
'Medium roast with chocolate notes',
950000, 875000, 8, 1, 75, 'GC-LAV-007', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419606/products/lavazza-rossa.jpg', false, NOW(), NOW()),

('Starbucks French Roast', 'starbucks-french-roast',
'Dark roast with smoky flavor. Intense and bold.',
'Dark roast, intense flavor',
875000, NULL, 7, 3, 60, 'CB-STA-008', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419607/products/starbucks-french.jpg', false, NOW(), NOW()),

('Trung Nguyen Gourmet Blend', 'trung-nguyen-gourmet-blend',
'Premium Vietnamese coffee with unique processing method.',
'Premium Vietnamese gourmet',
1300000, NULL, 7, 5, 70, 'CB-TRN-009', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419608/products/trung-nguyen-gourmet.jpg', true, NOW(), NOW()),

('Nescafe Classic Instant', 'nescafe-classic-instant',
'Classic instant coffee for quick preparation.',
'Classic instant coffee',
450000, NULL, 9, 4, 250, 'IC-NES-010', 'https://res.cloudinary.com/dnzarfrkx/image/upload/v1738419609/products/nescafe-classic.jpg', false, NOW(), NOW())
ON CONFLICT (slug) DO NOTHING;

-- ============================================
-- COMPLETED
-- ============================================
-- Sau khi chạy xong, refresh trang web để thấy sản phẩm!
