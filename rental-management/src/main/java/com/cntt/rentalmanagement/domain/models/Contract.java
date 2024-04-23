package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String files;
    @Column(name = "name_of_rent")
    private String nameOfRent;
    @Column(name = "deadline_contract")
    private LocalDateTime deadlineContract;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "num_of_people")
    private Long numOfPeople;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String phone;

    @Column(name = "is_invoice")
    private Boolean isInvoice;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Contract(String name, String files, String nameOfRent, String deadlineContract, String createdBy, String updatedBy, Car car) {
        this.name = name;
        this.files = files;
        this.nameOfRent = nameOfRent;
        this.numOfPeople = numOfPeople;
        this.phone = phone;
        this.deadlineContract = LocalDateTime.parse(deadlineContract);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.car = car;
    }

    public Contract(String name, String files, String nameOfRent, String startDate, String endDate, String createdBy, String updatedBy, Car car) {
        this.name = name;
        this.files = files;
        this.nameOfRent = nameOfRent;
        this.startDate =  LocalDateTime.parse(startDate);
        this.endDate =  LocalDateTime.parse(endDate);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.car = car;
    }
}
