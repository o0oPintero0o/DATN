package com.cntt.rentalmanagement.repository.impl;

import com.cntt.rentalmanagement.domain.models.Contract;
import com.cntt.rentalmanagement.repository.BaseRepository;
import com.cntt.rentalmanagement.repository.ContractRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class ContractRepositoryCustomImpl implements ContractRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final String FROM_CONTRACT = " from car_rental.contract c ";
    private static final String INNER_JOIN_CAR = " inner join car_rental.car r on c.car_id  = r.id ";

    @Override
    public Page<Contract> searchingContact(String name,String phone,Boolean isInvoice, Long userId, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_CONTRACT);
        strQuery.append(INNER_JOIN_CAR);
        strQuery.append(" where 1=1");

        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(name) && !name.isEmpty()) {
            strQuery.append(" AND c.name LIKE :title");
            params.put("title", "%"+name+"%");
        }

        if (Objects.nonNull(phone) && !phone.isEmpty()) {
            strQuery.append(" AND c.phone = :phone");
            params.put("phone", phone);
        }

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }
        if (Objects.nonNull(isInvoice) && Boolean.TRUE.equals(isInvoice)) {
            strQuery.append(" AND c.is_invoice = :isInvoice");
            params.put("isInvoice", 1);
        } else {
            strQuery.append(" AND c.is_invoice = :isInvoice");
            params.put("isInvoice", 0);
        }

        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT c.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Contract.class);

    }

    @Override
    public List<Contract> getAllContract(Long userId) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_CONTRACT);
        strQuery.append(INNER_JOIN_CAR);
        strQuery.append(" where 1=1");

        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }

        String strSelectQuery = "SELECT * " + strQuery;
        return BaseRepository.getResultListNativeQuery(em,strSelectQuery, params, Contract.class);
    }

    @Override
    public Page<Contract> searchingContact(Long userId, String phone, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_CONTRACT);
        strQuery.append(INNER_JOIN_CAR);
        strQuery.append(" where 1=1");

        Map<String, Object> params = new HashMap<>();

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND (c.user_id = :userId OR c.phone = :phone )");
            params.put("phone", phone);
            params.put("userId", userId);
        }

        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT c.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Contract.class);
    }
}
