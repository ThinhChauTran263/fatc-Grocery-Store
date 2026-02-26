-- ============================================
-- ADD DISCOUNT AND PROMO CODE COLUMNS TO ORDERS TABLE
-- ============================================

USE java5_asm;

-- Add promo_code column if not exists
ALTER TABLE orders 
ADD COLUMN IF NOT EXISTS promo_code VARCHAR(50) COMMENT 'Mã giảm giá đã áp dụng';

-- Add discount_amount column if not exists
ALTER TABLE orders 
ADD COLUMN IF NOT EXISTS discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT 'Số tiền giảm giá';

-- Add index for promo_code for better query performance
CREATE INDEX IF NOT EXISTS idx_promo_code ON orders(promo_code);

-- ============================================
-- COMPLETED - DISCOUNT AND PROMO CODE COLUMNS ADDED
-- ============================================
