package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.Contract;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.request.TotalNumberRequest;
import com.cntt.rentalmanagement.domain.payload.response.CostResponse;
import com.cntt.rentalmanagement.domain.payload.response.RevenueResponse;
import com.cntt.rentalmanagement.domain.payload.response.TotalNumberResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.ContractRepository;
import com.cntt.rentalmanagement.repository.MaintenanceRepository;
import com.cntt.rentalmanagement.repository.CarRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.StatisticalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticalServiceImpl extends BaseService implements StatisticalService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Override
    public TotalNumberRequest getNumberOfRentalerForStatistical() {
        User user = userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException("Tài khoản không tồn tại"));
        int total = 0;
        int revenue = 0;
        for (Contract contract : contractRepository.getAllContract(getUserId())) {
            Duration duration = Duration.between(contract.getStartDate(), contract.getEndDate());
            long months = duration.toMinutes() / (60 * 24 * 30);
            total += months * contract.getCar().getPrice().intValue();
            revenue += contract.getTotalPrice().intValue();
        }


        TotalNumberRequest totalNumberRequest = new TotalNumberRequest();
        totalNumberRequest.setNumberOfCar((int) carRepository.countAllByUser(user));
        totalNumberRequest.setNumberOfEmptyCar((int) carRepository.countAllByStatusAndUser(CarStatus.CAR_RENT,user) + (int) carRepository.countAllByStatusAndUser(CarStatus.CHECKED_OUT,user));
        if(!contractRepository.findAll().isEmpty()) {
            totalNumberRequest.setNumberOfPeople( contractRepository.sumNumOfPeople(getUserId()) == null ? 0 : (int) contractRepository.sumNumOfPeople(getUserId()).getNumberOfPeople());
        } else {
            totalNumberRequest.setNumberOfPeople(0);
        }
        totalNumberRequest.setRevenue(BigDecimal.valueOf(revenue));
        return totalNumberRequest;
    }

    @Override
    public TotalNumberResponse getStatisticalNumberOfAdmin() {
        TotalNumberResponse totalNumberResponse = new TotalNumberResponse();
        totalNumberResponse.setNumberOfAccount((int) userRepository.countByUser());
        totalNumberResponse.setNumberOfApprove((int) carRepository.countByIsApprove(true));
        totalNumberResponse.setNumberOfApproving((int) carRepository.countByIsApprove(false));
        totalNumberResponse.setNumberOfAccountLocked((int) carRepository.count());
        return totalNumberResponse;
    }

    @Override
    public Page<RevenueResponse> getByMonth() {
        List<RevenueResponse> list = new ArrayList<>();

        Map<YearMonth, Integer> monthTotalMap = new HashMap<>(); // Sử dụng Map để theo dõi tổng theo từng tháng

        for (Contract contract : contractRepository.getAllContract(getUserId())) {
            LocalDateTime endDate = contract.getCreatedAt().withMonth(12).withDayOfMonth(31);

            YearMonth currentMonth = YearMonth.from(contract.getCreatedAt());
            YearMonth endMonth = YearMonth.from(endDate);

            while (currentMonth.isBefore(endMonth) || currentMonth.equals(endMonth)) {
                int months = (int) currentMonth.until(endMonth, ChronoUnit.MONTHS);

                Integer total = monthTotalMap.get(currentMonth);
                if (total == null) {
                    total = 0;
                }

                total += months * contract.getCar().getPrice().intValue();
                monthTotalMap.put(currentMonth, total);

                currentMonth = currentMonth.plusMonths(1);
            }
        }

        for (Map.Entry<YearMonth, Integer> entry : monthTotalMap.entrySet()) {
            RevenueResponse response = new RevenueResponse();
            response.setMonth(entry.getKey().getMonthValue());
            response.setRevenue(BigDecimal.valueOf(entry.getValue()));
            list.add(response);
        }

        return new PageImpl<>(list);
    }

    @Override
    public Page<CostResponse> getByCost() {
        CostResponse costResponse2 = new CostResponse();
        if(!maintenanceRepository.findAll().isEmpty()) {
            costResponse2.setCost(maintenanceRepository.sumPriceOfMaintenance(getUserId()));
        } else  {
            costResponse2.setCost(BigDecimal.valueOf(0));
        }
        costResponse2.setName("Bảo trì");



        int total = 0;
        int revenue = 0;
        for (Contract contract : contractRepository.getAllContract(getUserId())) {
            Duration duration = Duration.between(contract.getStartDate(), contract.getEndDate());
            long months = duration.toMinutes() / (60 * 24 * 30);
            total += months * contract.getTotalPrice().intValue();
            revenue += contract.getTotalPrice().intValue();

        }

        List<CostResponse> costResponses = new ArrayList<>();
        CostResponse costResponse1 = new CostResponse();
        costResponse1.setName("Doanh thu");
        costResponse1.setCost(BigDecimal.valueOf(revenue));


        costResponses.add(costResponse1);
        costResponses.add(costResponse2);
        return new PageImpl<>(costResponses);
    }
}
