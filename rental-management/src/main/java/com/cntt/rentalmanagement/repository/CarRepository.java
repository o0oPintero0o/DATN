package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.domain.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryCustom {
    long countAllByUser(User user);

    long count();
    long countAllByStatusAndUser(CarStatus status, User user);

    Car[] findByUser(User user);

    long countByIsApprove(Boolean isApprove);
    @Modifying  // Required for update queries
    @Query(value = "" +
            "UPDATE rental_home.car r " +
            "LEFT JOIN car_rental.contract c ON r.id = c.car_id " +
            "SET r.status = 'CHECKED_OUT' " +
            "WHERE c.end_date < curdate()", nativeQuery = true)
    int updateCheckedOutCars();


}
