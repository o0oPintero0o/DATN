package com.cntt.rentalmanagement.controller;


import com.cntt.rentalmanagement.services.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @PostMapping
    private ResponseEntity<?> addContract(@RequestParam String name,
                                          @RequestParam Long carId,
                                          @RequestParam String nameOfRent,
                                          @RequestParam Long numOfPeople,
                                          @RequestParam String phone,
                                          @RequestParam String startDate,
                                          @RequestParam String endDate,
                                          @RequestParam List<MultipartFile> files,
                                          @RequestParam Boolean isInvoice) {
        return ResponseEntity.ok(contractService.addContract(name,carId,nameOfRent, numOfPeople, phone,startDate, endDate ,files, isInvoice));
    }


    @GetMapping
    private ResponseEntity<?> getAllContract(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String phone,
                                             @RequestParam(required = false) Boolean isInvoice,
                                             @RequestParam Integer pageNo,
                                             @RequestParam Integer pageSize) {
        return ResponseEntity.ok(contractService.getAllContractOfRentaler(name, phone, isInvoice ,pageNo, pageSize));
    }

    @GetMapping("/customer")
    private ResponseEntity<?> getAllContractForCustomer(
                                             @RequestParam(required = false) String phone,
                                             @RequestParam Integer pageNo,
                                             @RequestParam Integer pageSize) {
        return ResponseEntity.ok(contractService.getAllContractOfCustomer( phone ,pageNo, pageSize));
    }


    @GetMapping("/{id}")
    private ResponseEntity<?> getContractById(@PathVariable Long id){
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateContractInfo(@PathVariable Long id,
                                                 @RequestParam String name,
                                                 @RequestParam Long carId,
                                                 @RequestParam String nameOfRent,
                                                 @RequestParam Long numOfPeople,
                                                 @RequestParam String phone,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") String startDate,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") String endDate,
                                                 @RequestParam List<MultipartFile> files) {
        return ResponseEntity.ok(contractService.editContractInfo(id, name, carId, nameOfRent,numOfPeople, phone, startDate, endDate, files));
    }

    @PutMapping("/order/{id}")
    private ResponseEntity<?> updateContractInfo(@PathVariable Long id,
                                                 @RequestParam String name,
                                                 @RequestParam Long carId,
                                                 @RequestParam String nameOfRent,
                                                 @RequestParam Long numOfPeople,
                                                 @RequestParam String phone,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") String startDate,
                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") String endDate){
        return ResponseEntity.ok(contractService.editContractInfo(id, name, carId, nameOfRent,numOfPeople, phone, startDate, endDate));
    }

}
