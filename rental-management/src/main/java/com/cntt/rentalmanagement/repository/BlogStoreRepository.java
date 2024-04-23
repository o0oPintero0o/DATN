package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.BlogStore;
import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BlogStoreRepository extends JpaRepository<BlogStore, Long>,BlogStoreRepositoryCustom {
    Optional<BlogStore> findByCarAndUser(Car car, User user);
}
