package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.domain.models.CarMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarMediaRepository extends JpaRepository<CarMedia, Long> {

    void deleteAllByCar(Car car);
}
