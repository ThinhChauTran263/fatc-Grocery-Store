# 🚨 FIX LỖI DATABASE TRÊN RENDER

## Vấn đề
Bảng `user_activity_logs` trên PostgreSQL không khớp với Java Entity, gây lỗi 500.

## Giải pháp - Chạy SQL trên Render

### Bước 1: Truy cập Render Database
1. Vào https://dashboard.render.com
2. Chọn database **java5_asm**
3. Click tab **"Connect"** hoặc **"Shell"**

### Bước 2: Chạy SQL Script
Copy và paste toàn bộ nội dung file `FIX_USER_ACTIVITY_LOGS.sql` vào console

Hoặc chạy từng lệnh:

```sql
-- 1. Xóa bảng cũ
DROP TABLE IF EXISTS user_activity_logs CASCADE;

-- 2. Tạo lại bảng đúng cấu trúc
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

-- 3. Tạo indexes
CREATE INDEX idx_user_activity_logs_user_id ON user_activity_logs(user_id);
CREATE INDEX idx_user_activity_logs_session_id ON user_activity_logs(session_id);
CREATE INDEX idx_user_activity_logs_created_at ON user_activity_logs(created_at);
```

### Bước 3: Kiểm tra
```sql
-- Xem cấu trúc bảng
SELECT 
    column_name, 
    data_type, 
    character_maximum_length,
    is_nullable
FROM information_schema.columns 
WHERE table_name = 'user_activity_logs'
ORDER BY ordinal_position;
```

Kết quả phải có các cột:
- ✅ id (bigint)
- ✅ user_id (bigint)
- ✅ session_id (varchar 255)
- ✅ activity_type (varchar 20)
- ✅ ip_address (varchar 45)
- ✅ user_agent (text)
- ✅ page_url (varchar 500)
- ✅ metadata (text)
- ✅ created_at (timestamp)

### Bước 4: Restart Service
1. Quay lại Render Dashboard
2. Chọn web service **fatc-grocery-store**
3. Click **"Manual Deploy"** > **"Clear build cache & deploy"**

## Xong!
Website sẽ hoạt động bình thường sau khi deploy xong (khoảng 2-3 phút).

---

## Nếu không muốn chạy SQL

Có thể tắt activity logging tạm thời bằng cách comment code trong `WebMvcConfig.java`:

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    // Tạm tắt activity logging
    // registry.addInterceptor(activityLoggingInterceptor)
    //     .addPathPatterns("/**")
    //     .excludePathPatterns(...);
}
```

Nhưng cách này không khuyến nghị vì mất tính năng tracking user activity.
