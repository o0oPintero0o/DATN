package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.services.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/car/all")
    private ResponseEntity<?> getAllCar(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) Boolean approve,
                                         @RequestParam Integer pageNo,
                                         @RequestParam Integer pageSize) {
        return ResponseEntity.ok(blogService.getAllCarForAdmin(title,approve, pageNo, pageSize));
    }

    @GetMapping("/customer/car")
    private ResponseEntity<?> getAllCarForCustomer(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) Long  categoryId,
            @RequestParam Integer pageNo,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Long locationId



    ){
        return ResponseEntity.ok(blogService.getAllCarForCustomer(title, price,categoryId, pageNo, pageSize,locationId));
    }

}
