# 🚀 HƯỚNG DẪN DEPLOY NHANH

## TL;DR - Deploy trong 30 phút

Dự án này đã được setup sẵn để deploy lên **Render + Cloudinary**:
- ✅ **Hoàn toàn miễn phí**
- ✅ **Không cần thẻ tín dụng**
- ✅ **Lưu ảnh bền vững**

---

## 📚 Tài liệu chi tiết

1. **[CLOUDINARY_SETUP_GUIDE.md](document_file/CLOUDINARY_SETUP_GUIDE.md)**
   - Hướng dẫn đăng ký Cloudinary
   - Cấu hình upload ảnh
   - Deploy lên Render

2. **[RENDER_DEPLOY_CHECKLIST.md](RENDER_DEPLOY_CHECKLIST.md)**
   - Checklist từng bước
   - Troubleshooting
   - Monitoring

3. **[DEPLOYMENT_GUIDE.md](document_file/DEPLOYMENT_GUIDE.md)**
   - So sánh các platform
   - Deploy lên Google Cloud
   - Deploy lên Railway

---

## ⚡ Quick Start

### Bước 1: Đăng ký Cloudinary (5 phút)

```bash
1. Truy cập: https://cloudinary.com/users/register/free
2. Đăng ký tài khoản
3. Vào Dashboard: https://console.cloudinary.com/
4. Copy: Cloud name, API Key, API Secret
```

### Bước 2: Cấu hình Local (3 phút)

Mở file `.env`, update:

```env
USE_CLOUDINARY=true
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
```

### Bước 3: Test Local (5 phút)

```bash
# Build
.\mvnw clean install

# Run
.\mvnw spring-boot:run

# Test upload ảnh tại: http://localhost:8080
```

### Bước 4: Deploy lên Render (15 phút)

```bash
1. Push code lên GitHub
2. Vào: https://dashboard.render.com
3. New → PostgreSQL (tạo database)
4. New → Web Service (connect GitHub repo)
5. Set environment variables (xem RENDER_DEPLOY_CHECKLIST.md)
6. Deploy!
```

---

## 🎯 Lựa chọn Platform

| Platform | Phù hợp cho | Chi phí | Setup |
|----------|-------------|---------|-------|
| **Render + Cloudinary** | Demo, Portfolio | $0 | 30 phút |
| **Railway + Cloudinary** | Side project | $5/month | 20 phút |
| **Google Cloud Run** | Production | Pay-per-use | 2 giờ |

**Khuyến nghị:** Bắt đầu với **Render + Cloudinary**, sau đó migrate sang Google Cloud nếu cần.

---

## 📞 Hỗ trợ

Gặp vấn đề? Xem:
- [CLOUDINARY_SETUP_GUIDE.md](document_file/CLOUDINARY_SETUP_GUIDE.md) - Troubleshooting section
- [RENDER_DEPLOY_CHECKLIST.md](RENDER_DEPLOY_CHECKLIST.md) - Checklist đầy đủ

---

## ✨ Tính năng đã tích hợp

- ✅ Upload ảnh lên Cloudinary
- ✅ Fallback về local storage (nếu Cloudinary fail)
- ✅ Auto-optimize ảnh
- ✅ CDN toàn cầu
- ✅ Config sẵn cho Render, Railway, Google Cloud

