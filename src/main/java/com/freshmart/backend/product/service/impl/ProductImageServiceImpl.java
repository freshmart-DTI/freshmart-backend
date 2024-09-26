package com.freshmart.backend.product.service.impl;

import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.entity.ProductImage;
import com.freshmart.backend.product.repository.ProductImageRepository;
import com.freshmart.backend.product.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ImageUploadServiceImpl imageUploadService;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ImageUploadServiceImpl imageUploadService) {
        this.productImageRepository = productImageRepository;
        this.imageUploadService = imageUploadService;
    }

    @Override
    @Transactional
    public List<ProductImage> removeOldImages(Product existingProduct, List<Long> imagesToRemove) throws IOException {
        List<ProductImage> remainingImages = new ArrayList<>(existingProduct.getProductImages());
        if (imagesToRemove != null && !imagesToRemove.isEmpty()) {
            for (ProductImage image : existingProduct.getProductImages()) {
                if (imagesToRemove.contains(image.getId())) {
                    imageUploadService.deleteImage(image.getUrl());
                    productImageRepository.delete(image);
                    remainingImages.remove(image);
                }
            }
        }
        return remainingImages;
    }

    @Override
    public List<ProductImage> uploadNewImages(Product existingProduct, List<MultipartFile> newImages) throws Exception {
        List<ProductImage> imagesToSave = new ArrayList<>();
        for (MultipartFile image : newImages) {
            String uploadedUrl = imageUploadService.uploadImage(image,"products"); // Assuming this method handles the upload
            ProductImage productImage = new ProductImage();
            productImage.setUrl(uploadedUrl);
            productImage.setProduct(existingProduct);
            imagesToSave.add(productImage);
        }

        productImageRepository.saveAll(imagesToSave);
        return imagesToSave;
    }
}
