package com.cntt.rentalmanagement.repository;


import com.cntt.rentalmanagement.domain.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}
