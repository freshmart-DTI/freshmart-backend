package com.freshmart.backend.store.dto;

import com.freshmart.backend.store.entity.Store;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreDto {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Province is required")
    @NotBlank(message = "Province cannot be blank")
    private String province;

    @NotNull(message = "City is required")
    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotNull(message = "District is required")
    @NotBlank(message = "District cannot be blank")
    private String district;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.0")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.0")
    private BigDecimal longitude;

    @NotNull(message = "Is Main is required")
    private Boolean isMain;

    public Store toEntity() {
        Store store = new Store();

        store.setName(name);
        store.setProvince(province);
        store.setCity(city);
        store.setDistrict(district);
        store.setLatitude(latitude);
        store.setLongitude(longitude);
        store.setIsMain(isMain);

        return store;
    }
}
