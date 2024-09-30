package com.freshmart.backend.order.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String province;
    private String city;
    private String district;
    private String village;
    private String street;
    private String postalCode;
    private Double latitude;
    private Double longitude;
}
