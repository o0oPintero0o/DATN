package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CarRepositoryCustom {
    Page<Car> searchingCar(String title, Long userId, Pageable pageable);

    Page<Car> searchingCarForAdmin(String title, Boolean approve, Pageable pageable);

    Page<Car> searchingCarForCustomer(String title, BigDecimal price, Long categoryId, Long userId, Pageable pageable, Long locationId);


    Page<Car> getAllRentOfHome(Long userId, Pageable pageable);

}
