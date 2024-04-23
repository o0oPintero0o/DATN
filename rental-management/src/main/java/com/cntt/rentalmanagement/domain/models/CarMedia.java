package com.cntt.rentalmanagement.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "car_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String files;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
