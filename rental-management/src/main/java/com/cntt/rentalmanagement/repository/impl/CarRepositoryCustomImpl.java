package com.cntt.rentalmanagement.repository.impl;

import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.repository.BaseRepository;
import com.cntt.rentalmanagement.repository.CarRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class CarRepositoryCustomImpl implements CarRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Car> searchingCar(String title, Long userId, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from car_rental.car r ");
        strQuery.append(" where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(title) && !title.isEmpty()) {
            strQuery.append(" AND r.title LIKE :title");
            params.put("title", "%"+title+"%");
        }

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }

        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT r.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Car.class);
    }

    @Override
    public Page<Car> searchingCarForAdmin(String title, Boolean approve, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from car_rental.car r ");
        strQuery.append(" where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(title) && !title.isEmpty()) {
            strQuery.append(" AND r.title LIKE :title");
            params.put("title", "%"+title+"%");
        }

        if (Objects.nonNull(approve)) {
            strQuery.append(" AND r.is_approve = :approve");
            params.put("approve", approve);
        }

        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT r.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Car.class);
    }

    @Override
    public Page<Car> searchingCarForCustomer(String title, BigDecimal price, Long categoryId, Long userId, Pageable pageable, Long locationId) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from car_rental.car r where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(title) && !title.isEmpty()) {
            strQuery.append(" AND r.title LIKE :title");
            params.put("title", "%"+title+"%");
        }

        if (Objects.nonNull(price) ) {
            strQuery.append(" AND r.price = :price");
            params.put("price", price );
        }

        if (Objects.nonNull(categoryId) && categoryId != 0) {
            strQuery.append(" AND r.category_id = :categoryId");
            params.put("categoryId", categoryId);
        }

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }
        if (Objects.nonNull(locationId) && locationId != 0) {
            strQuery.append(" AND r.location_id = :locationId");
            params.put("locationId", locationId);
        }
        strQuery.append(" AND r.is_approve = :approve");
        params.put("approve", true);
        strQuery.append(" AND r.is_locked = 'ENABLE'");
        strQuery.append(" AND r.is_remove = :remove");
        params.put("remove", false);


        String strSelectQuery = "SELECT * " + strQuery + " ORDER BY r.id DESC";

        String strCountQuery = "SELECT COUNT(DISTINCT r.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Car.class);

    }





    @Override
    public Page<Car> getAllRentOfHome(Long userId, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" from car_rental.car r ");
        strQuery.append(" where 1=1");
        strQuery.append(" AND r.status IN ('CHECKED_OUT','CAR_RENT')");
        strQuery.append(" AND r.is_locked = 'ENABLE'");
        strQuery.append(" AND r.is_remove = '0'");
        strQuery.append(" AND r.is_approve = '1'");
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }
        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT r.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Car.class);

    }
}
