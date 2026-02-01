# Fix Homepage - Không có sản phẩm

## Vấn đề
Homepage vẫn không hiển thị sản phẩm sau khi deploy.

## Checklist cần làm NGAY

### 1. Kiểm tra database có featured products
Chạy trong Render PostgreSQL Shell:
```sql
SELECT COUNT(*) FROM products WHERE is_featured = true AND is_active = true;
```
**Phải >= 5**. Nếu = 0, chạy:
```sql
UPDATE products SET is_featured = true WHERE id IN (1,2,3,4,5,6,7,8);
```

### 2. Deploy code mới nhất
- Commit hiện tại: `7590a94`
- Vào Render → Manual Deploy → Deploy latest commit
- Đợi build xong (3-5 phút)

### 3. RESTART service (BẮT BUỘC)
- Click nút 3 chấm → Restart Service
- Đợi 30 giây

### 4. Check logs
- Click tab Logs
- Search "Featured products" hoặc "getFeaturedProducts"
- Xem có error gì không

### 5. Test homepage
- Mở: https://your-app.onrender.com/
- View page source (Ctrl+U)
- Search "product-card" - phải thấy HTML

## Nếu vẫn không có sản phẩm

### Test API trực tiếp
Mở browser console (F12):
```javascript
fetch('/api/products/featured?page=0&size=8')
  .then(r => r.json())
  .then(data => console.log(data));
```

### Clear build cache
Render → Manual Deploy → Clear build cache & deploy

### Verify environment variables
Phải có:
- SPRING_PROFILES_ACTIVE=prod
- DATABASE_URL=postgresql://...
