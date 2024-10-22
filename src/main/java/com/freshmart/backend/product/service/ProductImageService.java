package com.freshmart.backend.product.service;

import com.freshmart.backend.product.entity.Product;
import com.freshmart.backend.product.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ProductImageService {
    List<ProductImage> removeOldImages(Product existingProduct, List<Long> imagesToRemove) throws IOException;
    List<ProductImage> uploadNewImages(Product existingProduct, List<MultipartFile> newImages) throws Exception;
}
