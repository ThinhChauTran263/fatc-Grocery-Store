# ⚡ FIX NGAY - DATABASE ERROR

## ❌ Lỗi: Cannot connect to database

## ✅ GIẢI PHÁP (1 phút)

### Nguyên nhân:
Database URL **THIẾU HOSTNAME SUFFIX**!

### Fix:

**Sai:** ❌
```
dpg-d5vlucngi27c73cej9pg-a
```

**Đúng:** ✅
```
dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com
```

---

## 🚀 CÁCH FIX (3 bước)

### Bước 1: Vào Render
1. Dashboard → Service → **Environment**

### Bước 2: Sửa URL
1. Tìm biến: `SPRING_DATASOURCE_URL`
2. Click **Edit**
3. Thay bằng:
```
jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com/java5_asm
```
**⚠️ LƯU Ý:** Phải có `jdbc:` ở đầu!
4. Click **Save Changes**

### Bước 3: Redeploy
1. Click **Manual Deploy**
2. Đợi 5 phút
3. Check logs → Success! ✅

---

## 📋 HOẶC: Import lại toàn bộ

1. Xóa 3 biến database cũ
2. Click **"Add from .env"**
3. Copy từ file **render-import-TEMPLATE.env** (đã fix)
4. Paste và **Add Variables**

---

## ✅ URL ĐÚNG

```
jdbc:postgresql://java5_user:X04NvMLy7TJE6jT4ckDCW4qIm3KAdO2D@dpg-d5vlucngi27c73cej9pg-a.singapore-postgres.render.com/java5_asm
```

**Lưu ý:** 
- Có `jdbc:` ở đầu! ✅
- Có `.singapore-postgres.render.com` ở cuối hostname! ✅

---

## 🎉 DONE!

Sau khi fix, app sẽ start thành công!

**Chi tiết:** Xem file **🐛_FIX_DATABASE_ERROR.md**
