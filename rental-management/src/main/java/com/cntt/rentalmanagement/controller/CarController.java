package com.cntt.rentalmanagement.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cntt.rentalmanagement.services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.DTO.CommentDTO;
import com.cntt.rentalmanagement.domain.payload.request.AssetRequest;
import com.cntt.rentalmanagement.domain.payload.request.CarRequest;
import com.cntt.rentalmanagement.secruity.CurrentUser;
import com.cntt.rentalmanagement.secruity.UserPrincipal;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


//    @GetMapping("/all")
//    private ResponseEntity<?> getAllCar(@RequestParam(required = false) String title,
//                                         @RequestParam(required = false) Boolean approve,
//                                         @RequestParam Integer pageNo,
//                                         @RequestParam Integer pageSize) {
//        return ResponseEntity.ok(blogService.getAllCarForAdmin(title, approve, pageNo, pageSize));
//    }

    @GetMapping("/{userId}/rentaler")
    public ResponseEntity<?> getAllCarOfUserId(@PathVariable Long userId,
                                               @RequestParam Integer pageNo,
                                               @RequestParam Integer pageSize) {
        return ResponseEntity.ok(carService.getCarByUserId(userId, pageNo, pageSize));
    }

    @GetMapping
    public ResponseEntity<?> getCarByRentaler(@RequestParam(required = false) String title,
                                              @RequestParam Integer pageNo,
                                              @RequestParam Integer pageSize) {
        return ResponseEntity.ok(carService.getCarByRentaler(title, pageNo, pageSize));
    }

    @GetMapping("/rent-home")
    public ResponseEntity<?> getRentOfHome() {
        return ResponseEntity.ok(carService.getRentOfHome());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> disableCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.disableCar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarInfo(@PathVariable Long id, MultipartHttpServletRequest request) {
        return ResponseEntity.ok(carService.updateCarInfo(id, putCarRequest(request)));
    }

    @PostMapping
    public ResponseEntity<?> addCar(MultipartHttpServletRequest request) {
        return ResponseEntity.ok(carService.addNewCar(putCarRequest(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.removeCar(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> isApprove(@PathVariable Long id) {
        return ResponseEntity.ok(carService.isApproveCar(id));
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<?> checkoutCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.checkoutCar(id));
    }

    @GetMapping("/{carId}/comments")
    public List<CommentDTO> getAllComment(@PathVariable Long carId) {
        return carService.getAllCommentCar(carId);
    }

    @PostMapping("/{carId}/comments")
//	@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long carId,
                                        @RequestBody CommentDTO commentDTO) {
        System.out.println(commentDTO.getRateRating());
        return carService.addComment(userPrincipal.getId(), commentDTO).equals("Thêm bình luận thành công")
                ? ResponseEntity.ok("Thêm bình luận thành công")
                : new ResponseEntity<String>("Thêm bình luận thất bại", HttpStatus.BAD_REQUEST);
    }

    private CarRequest putCarRequest(MultipartHttpServletRequest request) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        BigDecimal price = BigDecimal.valueOf(Double.valueOf(request.getParameter("price")));
        Double latitude = Double.valueOf(request.getParameter("latitude"));
        Double longitude = Double.valueOf(request.getParameter("longitude"));
        String address = request.getParameter("address");
        Long locationId = Long.valueOf(request.getParameter("locationId"));
        Long categoryId = Long.valueOf(request.getParameter("categoryId"));
        List<AssetRequest> assets = new ArrayList<>();
        for (int i = 0; i < Integer.valueOf(request.getParameter("asset")); i++) {
            String assetName = request.getParameterValues("assets[" + i + "][name]")[0];
            Integer assetNumber = Integer.valueOf(request.getParameterValues("assets[" + i + "][number]")[0]);
            assets.add(new AssetRequest(assetName, assetNumber));
        }

        List<MultipartFile> files = request.getFiles("files");
        return new CarRequest(title, description, price, latitude, longitude, address, locationId, categoryId, locationId, CarStatus.CAR_RENT, assets, files);
    }

    @GetMapping("/update")
    public ResponseEntity<String> updateCars() {
        int updatedCarsCount = carService.updateCar(); // Phương thức updateCars trong carService
//        System.out.println("Số lượng phòng đã cập nhật: " + updatedCarsCount);
        if (updatedCarsCount > 0) {
            return ResponseEntity.ok("Cập nhật thành công " + updatedCarsCount + " phòng!");
        } else {
            return ResponseEntity.noContent().build();
        }

    }

//    public static void main(String[] args) {
//         CarService carService = new CarService() {
//             @Override
//             public MessageResponse addNewCar(CarRequest carRequest) {
//                 return null;
//             }
//
//             @Override
//             public Page<CarResponse> getCarByRentaler(String title, Integer pageNo, Integer pageSize) {
//                 return null;
//             }
//
//             @Override
//             public CarResponse getCarById(Long id) {
//                 return null;
//             }
//
//             @Override
//             public MessageResponse disableCar(Long id) {
//                 return null;
//             }
//
//             @Override
//             public MessageResponse updateCarInfo(Long id, CarRequest carRequest) {
//                 return null;
//             }
//
//             @Override
//             public Page<CarResponse> getRentOfHome() {
//                 return null;
//             }
//
//             @Override
//             public MessageResponse checkoutCar(Long id) {
//                 return null;
//             }
//
//             @Override
//             public MessageResponse isApproveCar(Long id) {
//                 return null;
//             }
//
//             @Override
//             public MessageResponse removeCar(Long id) {
//                 return null;
//             }
//
//             @Override
//             public String addComment(Long id, CommentDTO commentDTO) {
//                 return null;
//             }
//
//             @Override
//             public List<CommentDTO> getAllCommentCar(Long id) {
//                 return null;
//             }
//
//             @Override
//             public int updateCar() {
//                 return 0;
//             }
//
//             @Override
//             public Page<CarResponse> getAllCarForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize) {
//                 return null;
//             }
//
//             @Override
//             public Page<CarResponse> getCarByUserId(Long userId, Integer pageNo, Integer pageSize) {
//                 return null;
//             }
//         };
//         int updatedCarsCount = carService.updateCar();
//         System.out.println("Số lượng phòng đã cập nhật: " + updatedCarsCount);
//    }


}
