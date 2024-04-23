package com.cntt.rentalmanagement.domain.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TotalNumberRequest {
    private Integer numberOfCar;
    private Integer numberOfPeople;
    private Integer numberOfEmptyCar;
    private BigDecimal revenue;


}
