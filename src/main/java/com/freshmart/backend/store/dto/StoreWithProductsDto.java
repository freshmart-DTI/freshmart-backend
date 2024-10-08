package com.freshmart.backend.store.dto;

import com.freshmart.backend.product.dto.ProductWithInventoryDto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StoreWithProductsDto {
    private Long id;
    private String name;
    private String province;
    private String city;
    private String district;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isMain;
    private List<ProductWithInventoryDto> products;
}
