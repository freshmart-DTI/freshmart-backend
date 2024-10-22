package com.freshmart.backend.product.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploadService {
    String uploadImage(MultipartFile file, String folderName) throws IOException;
    void deleteImage(String imagePublicId) throws IOException;
    String generateUrl(String publicId);
}
