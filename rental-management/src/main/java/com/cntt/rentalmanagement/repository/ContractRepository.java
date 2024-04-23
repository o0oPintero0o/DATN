package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.Contract;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.response.NumberOfPeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    @Query(value = "select sum(c.num_of_people) as numberOfPeople from contract c " +
            "inner join car r ON c.car_id = r.id " +
            "where r.user_id =  :userId ", nativeQuery = true)
    NumberOfPeople sumNumOfPeople(Long userId);
}
