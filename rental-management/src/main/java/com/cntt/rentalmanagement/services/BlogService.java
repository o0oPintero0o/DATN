package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.response.CarResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface BlogService {
    Page<CarResponse> getAllCarForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize);



    Page<CarResponse> getAllCarForCustomer(String title, BigDecimal price, Long categoryId, Integer pageNo, Integer pageSize, Long locationId);



}
