package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.enums.LockedStatus;
import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.Contract;
import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.domain.payload.response.ContractResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.ContractRepository;
import com.cntt.rentalmanagement.repository.CarRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.ContractService;
import com.cntt.rentalmanagement.services.FileStorageService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl extends BaseService implements ContractService {

    private final ContractRepository contractRepository;
    private final CarRepository roomRepository;
    private final FileStorageService fileStorageService;
    private final MapperUtils mapperUtils;

    @Override
    public MessageResponse addContract(String name, Long roomId, String nameRentHome,Long numOfPeople,String phone, String startDate, String endDate, List<MultipartFile> files, Boolean isInvoice) {
        Car car = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));
        if (car.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Phòng đã bị khóa");
        }




        String file = fileStorageService.storeFile(files.get(0)).replace("photographer/files/", "");
        Contract contract = new Contract(name,"http://localhost:8080/document/" +file, nameRentHome, startDate, endDate ,getUsername(), getUsername(), car);
        contract.setNumOfPeople(numOfPeople);
        contract.setIsInvoice(true);
        contract.setPhone(phone);
        BigDecimal totalPrice = car.getPrice().multiply(BigDecimal.valueOf(getLongDay(startDate, endDate)));
        contract.setTotalPrice(totalPrice);
        contractRepository.save(contract);

        car.setStatus(CarStatus.HIRED);
        roomRepository.save(car);
        return MessageResponse.builder().message("Thêm hợp đồng mới thành công").build();
    }

    @Override
    public Page<ContractResponse> getAllContractOfRentaler(String name,String phone,Boolean isInvoice, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(contractRepository.searchingContact(name,phone, isInvoice ,getUserId(),pageable),ContractResponse.class, pageable);
    }

    @Override
    public ContractResponse getContractById(Long id) {
        return mapperUtils.convertToResponse(contractRepository.findById(id).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!")), ContractResponse.class);
    }

    @Override
    public MessageResponse editContractInfo(Long id, String name, Long roomId, String nameOfRent,Long numOfPeople,String phone, String startDate, String endDate, List<MultipartFile> files) {
        Car car = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Xe đã không tồn tại"));
        if (car.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Xe đã bị khóa");
        }

        Contract contract = contractRepository.findById(id).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        contract.setNumOfPeople(numOfPeople);
        contract.setPhone(phone);
        contract.setStartDate(LocalDateTime.parse(startDate));
        contract.setEndDate(LocalDateTime.parse(endDate));
        contract.setCar(car);
        contract.setName(name);
        if (Objects.nonNull(files.get(0))) {
            String file = fileStorageService.storeFile(files.get(0)).replace("photographer/files/", "");
            contract.setFiles("http://localhost:8080/document/"+file);
        }
        contract.setNameOfRent(nameOfRent);
        contractRepository.save(contract);
        return MessageResponse.builder().message("Cập nhật hợp đồng thành công.").build();
    }

    @Override
    public MessageResponse editContractInfo(Long id, String name, Long roomId, String nameOfRent, Long numOfPeople, String phone, String startDate, String endDate) {
        Car car = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));
        if (car.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Phòng đã bị khóa");
        }

        Contract contract = contractRepository.findById(id).orElseThrow(() -> new BadRequestException("Hợp đồng không tồn tại!"));
        contract.setNumOfPeople(numOfPeople);
        contract.setPhone(phone);
        contract.setStartDate(LocalDateTime.parse(startDate));
        contract.setEndDate(LocalDateTime.parse(endDate));
        contract.setCar(car);
        contract.setName(name);

        contract.setNameOfRent(nameOfRent);
        contractRepository.save(contract);
        return MessageResponse.builder().message("Cập nhật hợp đồng thành công.").build();
    }

    @Override
    public Page<ContractResponse> getAllContractOfCustomer(String phone, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(contractRepository.searchingContact(getUserId(),phone,pageable),ContractResponse.class, pageable);
    }

    private Long getLongDay(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);

        long daysBetween = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());

        return daysBetween;
    }

}
