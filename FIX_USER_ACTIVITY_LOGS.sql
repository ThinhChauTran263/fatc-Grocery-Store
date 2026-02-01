-- ============================================
-- FIX USER_ACTIVITY_LOGS TABLE FOR POSTGRESQL
-- ============================================
-- Chạy script này trên Render PostgreSQL để fix lỗi
-- Cách chạy: Vào Render Dashboard > Database > Connect > Run SQL

-- Xóa bảng cũ (nếu có data quan trọng thì backup trước)
DROP TABLE IF EXISTS user_activity_logs CASCADE;

-- Tạo lại bảng với cấu trúc đúng
CREATE TABLE user_activity_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    session_id VARCHAR(255),
    activity_type VARCHAR(20) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    page_url VARCHAR(500),
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Tạo index để tăng performance
CREATE INDEX idx_user_activity_logs_user_id ON user_activity_logs(user_id);
CREATE INDEX idx_user_activity_logs_session_id ON user_activity_logs(session_id);
CREATE INDEX idx_user_activity_logs_created_at ON user_activity_logs(created_at);

-- Kiểm tra kết quả
SELECT 
    column_name, 
    data_type, 
    character_maximum_length,
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'user_activity_logs'
ORDER BY ordinal_position;
