package com.freshmart.backend.product.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.freshmart.backend.product.service.ImageUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
    private final Cloudinary cloudinary;

    public ImageUploadServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        HashMap<Object, Object> options = new HashMap<>();
        options.put("folder", folderName);

        Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadedFile.get("public_id");
    }

    @Override
    public void deleteImage(String imagePublicId) throws IOException {
        try {
            Map result = cloudinary.uploader().destroy(imagePublicId, ObjectUtils.emptyMap());
            if (!"ok".equals(result.get("result"))) {
                throw new RuntimeException("Failed to delete image from Cloudinary: " + result.get("result"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting image from Cloudinary", e);
        }
    }

    @Override
    public String generateUrl(String publicId) {
        return cloudinary.url().format("png").secure(true).generate(publicId);
    }

}
