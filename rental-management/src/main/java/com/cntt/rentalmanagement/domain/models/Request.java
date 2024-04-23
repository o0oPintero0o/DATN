package com.cntt.rentalmanagement.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;
    private String description;
    @Column(name = "is_answer")
    private Boolean isAnswer;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Request(String name, String phoneNumber, String description, Car car) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.car = car;
    }
}
