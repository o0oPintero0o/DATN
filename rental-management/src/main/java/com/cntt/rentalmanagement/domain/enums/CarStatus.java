package com.cntt.rentalmanagement.domain.enums;

public enum CarStatus {
    HIRED("HIRED"),
    CAR_RENT("CAR_RENT"),
    CHECKED_OUT("CHECKED_OUT");


    private String value;

    CarStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
