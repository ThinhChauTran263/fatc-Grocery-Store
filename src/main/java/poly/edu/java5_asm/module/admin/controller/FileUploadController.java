package poly.edu.java5_asm.module.admin.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.java5_asm.common.service.CloudinaryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Controller xử lý upload file
 * Hỗ trợ cả local storage và Cloudinary
 */
@RestController
@RequestMapping("/api/admin/upload")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class FileUploadController {
    
    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
    private final CloudinaryService cloudinaryService;
    
    // Whitelist các extension được phép upload
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".svg"
    );
    
    // Whitelist các MIME types được phép
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/svg+xml"
    );

    @Value("${app.upload.dir:src/main/resources/static/assets/img/product}")
    private String uploadDir;
    
    @Value("${app.upload.use-cloudinary:true}")
    private boolean useCloudinary;

    /**
     * Upload hình ảnh sản phẩm
     * Tự động chọn Cloudinary hoặc local storage
     */
    @PostMapping("/product-image")
    public ResponseEntity<Map<String, Object>> uploadProductImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, "products");
    }

    /**
     * Upload logo thương hiệu
     */
    @PostMapping("/brand-logo")
    public ResponseEntity<Map<String, Object>> uploadBrandLogo(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, "brands");
    }

    /**
     * Upload icon danh mục
     */
    @PostMapping("/category-icon")
    public ResponseEntity<Map<String, Object>> uploadCategoryIcon(@RequestParam("file") MultipartFile file) {
        return uploadImage(file, "categories");
    }

    /**
     * Upload image chung - tự động chọn Cloudinary hoặc local storage
     */
    private ResponseEntity<Map<String, Object>> uploadImage(MultipartFile file, String folder) {
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng chọn file");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate MIME type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            response.put("success", false);
            response.put("message", "Chỉ chấp nhận file hình ảnh (jpg, png, gif, webp, svg)");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate file extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            response.put("success", false);
            response.put("message", "Extension không được phép. Chỉ chấp nhận: jpg, jpeg, png, gif, webp, svg");
            return ResponseEntity.badRequest().body(response);
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            response.put("success", false);
            response.put("message", "File không được vượt quá 5MB");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String imageUrl;
            String filename;
            
            if (useCloudinary) {
                // Upload to Cloudinary
                log.info("Uploading to Cloudinary folder: {}", folder);
                Map<String, Object> uploadResult = cloudinaryService.uploadImage(file, folder);
                
                imageUrl = (String) uploadResult.get("secure_url");
                filename = (String) uploadResult.get("public_id");
                
                response.put("cloudinary", true);
                response.put("publicId", filename);
                
            } else {
                // Upload to local storage (fallback)
                log.info("Uploading to local storage folder: {}", folder);
                String newFilename = folder + "-" + UUID.randomUUID().toString().substring(0, 8) + extension;

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                imageUrl = "/assets/img/" + folder + "/" + newFilename;
                filename = newFilename;
                
                response.put("cloudinary", false);
            }
            
            response.put("success", true);
            response.put("message", "Upload thành công");
            response.put("imageUrl", imageUrl);
            response.put("filename", filename);
            
            log.info("Uploaded image to {}: {}", folder, imageUrl);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Lỗi upload file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
