package com.freshmart.backend.order.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String province;
    private String city;
    private String district;
    private String village;
    private String street;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
