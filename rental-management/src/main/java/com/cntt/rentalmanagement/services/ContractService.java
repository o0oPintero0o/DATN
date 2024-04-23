package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.response.ContractResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContractService {
    MessageResponse addContract(String name, Long roomId, String nameRentHome,Long numOfPeople,String phone, String startDate, String endDate, List<MultipartFile> files, Boolean isInvoice);

    Page<ContractResponse> getAllContractOfRentaler(String name, String phone,Boolean isInvoice, Integer pageNo, Integer pageSize);

    ContractResponse getContractById(Long id);


    MessageResponse editContractInfo(Long id, String name, Long roomId, String nameOfRent,Long numOfPeople,String phone, String startDate, String endDate, List<MultipartFile> files);

    MessageResponse editContractInfo(Long id, String name, Long roomId, String nameOfRent,Long numOfPeople,String phone, String startDate, String endDate);

    Page<ContractResponse> getAllContractOfCustomer(String phone, Integer pageNo, Integer pageSize);
}
