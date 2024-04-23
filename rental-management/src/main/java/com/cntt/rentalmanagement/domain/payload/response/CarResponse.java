package com.cntt.rentalmanagement.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CarResponse {
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Double latitude;

    private Double longitude;

    private String address;

    private String status;

    private String isLocked;

    private Boolean isApprove;

    private Boolean isRemove;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocationResponse location;

    private CategoryResponse category;

    private List<AssetResponse> assets;

    private List<CarMediaResponse> roomMedia;

    private UserResponse user;
}
