package com.cntt.rentalmanagement.domain.payload.response;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContractResponse {
    private Long id;
    private String name;
    private String files;
    private String nameOfRent;
    private LocalDateTime deadlineContract;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;
    private CarResponse car;
    private LocalDateTime createdAt;
    private String phone;
    private Long numOfPeople;
    private Boolean isInvoice;

}
