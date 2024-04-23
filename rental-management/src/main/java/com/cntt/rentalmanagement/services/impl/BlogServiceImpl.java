package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.payload.response.CarResponse;
import com.cntt.rentalmanagement.repository.CarRepository;
import com.cntt.rentalmanagement.services.BlogService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final CarRepository carRepository;
    private final MapperUtils mapperUtils;
    @Override
    public Page<CarResponse> getAllCarForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(carRepository.searchingCarForAdmin(title, approve,pageable), CarResponse.class, pageable);
    }



    @Override
    public Page<CarResponse> getAllCarForCustomer(String title, BigDecimal price, Long categoryId, Integer pageNo, Integer pageSize, Long locationId) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(carRepository.searchingCarForCustomer(title,price,categoryId,null, pageable, locationId), CarResponse.class,pageable);
    }

//    @Override
//    public Page<CarResponse> getAllCarForCustomer(String title, BigDecimal price, Long categoryId, Integer pageNo, Integer pageSize, Long locationId) {
//        int page = pageNo == 0 ? pageNo : pageNo - 1;
//        Pageable pageable = PageRequest.of(page, pageSize);
//        return mapperUtils.convertToResponsePage(carRepository.searchingCarForCustomer(title,price,categoryId,null, pageable),CarResponse.class,pageable);
//    }



    }

