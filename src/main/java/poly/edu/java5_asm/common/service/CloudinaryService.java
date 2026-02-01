package poly.edu.java5_asm.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service interface cho Cloudinary
 */
public interface CloudinaryService {
    
    /**
     * Upload ảnh lên Cloudinary
     * @param file File ảnh cần upload
     * @param folder Thư mục lưu trữ trên Cloudinary
     * @return Map chứa thông tin ảnh (url, public_id, etc.)
     */
    Map<String, Object> uploadImage(MultipartFile file, String folder) throws IOException;
    
    /**
     * Xóa ảnh khỏi Cloudinary
     * @param publicId Public ID của ảnh
     * @return Map chứa kết quả xóa
     */
    Map<String, Object> deleteImage(String publicId) throws IOException;
}
