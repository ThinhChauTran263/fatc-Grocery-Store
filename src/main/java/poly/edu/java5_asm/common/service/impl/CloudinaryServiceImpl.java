package poly.edu.java5_asm.common.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poly.edu.java5_asm.common.service.CloudinaryService;

import java.io.IOException;
import java.util.Map;

/**
 * Implementation của CloudinaryService
 */
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    
    private static final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);
    private final Cloudinary cloudinary;

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folder) throws IOException {
        log.info("Uploading image to Cloudinary folder: {}", folder);
        
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", folder,
                        "resource_type", "image",
                        "quality", "auto",
                        "fetch_format", "auto"
                )
        );
        
        log.info("Image uploaded successfully: {}", uploadResult.get("secure_url"));
        return uploadResult;
    }

    @Override
    public Map<String, Object> deleteImage(String publicId) throws IOException {
        log.info("Deleting image from Cloudinary: {}", publicId);
        
        Map<String, Object> deleteResult = cloudinary.uploader().destroy(
                publicId,
                ObjectUtils.emptyMap()
        );
        
        log.info("Image deleted: {}", deleteResult);
        return deleteResult;
    }
}
