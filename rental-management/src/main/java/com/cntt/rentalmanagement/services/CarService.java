package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.models.DTO.CommentDTO;
import com.cntt.rentalmanagement.domain.payload.request.CarRequest;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.domain.payload.response.CarResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface CarService {

    MessageResponse addNewCar(CarRequest carRequest);

    Page<CarResponse> getCarByRentaler(String title, Integer pageNo, Integer pageSize);

    CarResponse getCarById(Long id);

    MessageResponse disableCar(Long id);

    MessageResponse updateCarInfo(Long id, CarRequest carRequest);

    Page<CarResponse> getRentOfHome();
    MessageResponse checkoutCar(Long id);

    MessageResponse isApproveCar(Long id);

    MessageResponse removeCar(Long id);

	String addComment(Long id, CommentDTO commentDTO);

	List<CommentDTO> getAllCommentCar(Long id);
	int updateCar();


    Page<CarResponse> getAllCarForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize);

    Page<CarResponse> getCarByUserId(Long userId, Integer pageNo, Integer pageSize);
}
