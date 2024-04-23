package com.cntt.rentalmanagement.domain.payload.request;

import com.cntt.rentalmanagement.domain.enums.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    private String title;

    private String description;

    private BigDecimal price;

    private Double latitude;

    private Double longitude;

    private String address;

    private Long locationId;

    private Long categoryId;

    private Long colorId;

    private CarStatus status;

    private List<AssetRequest> assets;

    private List<MultipartFile> files;

}
