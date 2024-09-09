package com.freshmart.backend.product.dto;

import com.freshmart.backend.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    public Category toEntity() {
        Category category = new Category();

        category.setId(id);
        category.setName(name);

        return category;
    }
}
