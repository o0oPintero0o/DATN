package com.cntt.rentalmanagement.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cntt.rentalmanagement.domain.enums.LockedStatus;
import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.Asset;
import com.cntt.rentalmanagement.domain.models.Category;
import com.cntt.rentalmanagement.domain.models.Comment;
import com.cntt.rentalmanagement.domain.models.Location;
import com.cntt.rentalmanagement.domain.models.Rate;
import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.domain.models.CarMedia;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.models.DTO.CommentDTO;
import com.cntt.rentalmanagement.domain.payload.request.AssetRequest;
import com.cntt.rentalmanagement.domain.payload.request.CarRequest;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.domain.payload.response.CarResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.AssetRepository;
import com.cntt.rentalmanagement.repository.CategoryRepository;
import com.cntt.rentalmanagement.repository.CommentRepository;
import com.cntt.rentalmanagement.repository.LocationRepository;
import com.cntt.rentalmanagement.repository.CarMediaRepository;
import com.cntt.rentalmanagement.repository.CarRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.FileStorageService;
import com.cntt.rentalmanagement.services.CarService;
import com.cntt.rentalmanagement.utils.MapperUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarServiceImpl extends BaseService implements CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final FileStorageService fileStorageService;
    private final CarMediaRepository carMediaRepository;
    private final CategoryRepository categoryRepository;
    private final AssetRepository assetRepository;
    private final CommentRepository commentRepository;
    private final MapperUtils mapperUtils;

    @Override
    public MessageResponse addNewCar(CarRequest carRequest) {
        Location location = locationRepository.
                findById(carRequest.getLocationId()).orElseThrow(() -> new BadRequestException("Thành phố chưa tồn tại."));
        Category category = categoryRepository.findById(carRequest.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Danh mục không tồn tại"));
        Car car = new Car(
                carRequest.getTitle(),
                carRequest.getDescription(),
                carRequest.getPrice(),
                carRequest.getLatitude(),
                carRequest.getLongitude(),
                carRequest.getAddress(),
                getUsername(),
                getUsername(),
                location,
                category,
                getUser(),
                carRequest.getStatus());
        carRepository.save(car);
        for (MultipartFile file : carRequest.getFiles()) {
            String fileName = fileStorageService.storeFile(file);
            CarMedia carMedia = new CarMedia();
            carMedia.setFiles("http://localhost:8080/image/"+fileName);
            carMedia.setCar(car);
            carMediaRepository.save(carMedia);
        }

        for (AssetRequest asset: carRequest.getAssets()) {
            Asset a = new Asset();
            a.setCar(car);
            a.setName(asset.getName());
            a.setNumber(asset.getNumber());
            assetRepository.save(a);
        }
        return MessageResponse.builder().message("Thêm tin phòng thành công").build();
    }

    @Override
    public Page<CarResponse> getCarByRentaler(String title, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CarResponse> result = mapperUtils.convertToResponsePage(carRepository.searchingCar(title, getUserId() ,pageable), CarResponse.class,pageable);
        return mapperUtils.convertToResponsePage(carRepository.searchingCar(title, getUserId(), pageable), CarResponse.class, pageable);
    }

    @Override
    public CarResponse getCarById(Long id) {
        return mapperUtils.convertToResponse(carRepository.findById(id).orElseThrow(() ->
                new BadRequestException("Phòng trọ này không tồn tại.")), CarResponse.class);
    }

    @Override
    public MessageResponse disableCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new BadRequestException("Thông tin phòng không tồn tại."));
        car.setIsLocked(LockedStatus.DISABLE);
        carRepository.save(car);
        return MessageResponse.builder().message("Bài đăng của phòng đã được ẩn đi.").build();
    }

    @Override
    @Transactional
    public MessageResponse updateCarInfo(Long id, CarRequest carRequest) {
        Car car = carRepository.findById(id).orElseThrow(() -> new BadRequestException("Thông tin phòng không tồn tại."));
        Location location = locationRepository.
                findById(carRequest.getLocationId()).orElseThrow(() -> new BadRequestException("Thành phố chưa tồn tại."));
        Category category = categoryRepository.findById(carRequest.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Danh mục không tồn tại"));
        car.setUpdatedBy(getUsername());
        car.setTitle(carRequest.getTitle());
        car.setDescription(carRequest.getDescription());
        car.setPrice(carRequest.getPrice());
        car.setLatitude(carRequest.getLatitude());
        car.setLongitude(carRequest.getLongitude());
        car.setAddress(carRequest.getAddress());
        car.setUpdatedBy(getUsername());
        car.setLocation(location);
        car.setCategory(category);
        car.setStatus(carRequest.getStatus());
        carRepository.save(car);

        if (Objects.nonNull(carRequest.getFiles())) {
            carMediaRepository.deleteAllByCar(car);
            for (MultipartFile file : carRequest.getFiles()) {
                String fileName = fileStorageService.storeFile(file);
                CarMedia carMedia = new CarMedia();
                carMedia.setFiles("http://localhost:8080/image/"+fileName);
                carMedia.setCar(car);
                carMediaRepository.save(carMedia);
            }
        }

        assetRepository.deleteAllByCar(car);
        for (AssetRequest asset: carRequest.getAssets()) {
            Asset a = new Asset();
            a.setCar(car);
            a.setName(asset.getName());
            a.setNumber(asset.getNumber());
            assetRepository.save(a);
        }
        return MessageResponse.builder().message("Cập nhật thông tin thành công").build();
    }

    @Override
    public Page<CarResponse> getRentOfHome() {
        Pageable pageable = PageRequest.of(0,100);
        return mapperUtils.convertToResponsePage(carRepository.getAllRentOfHome( getUserId(), pageable), CarResponse.class, pageable);
    }
    
    @Override
    public List<CommentDTO> getAllCommentCar(Long id){
    	Car car = carRepository.findById(id).get();
    	return mapperUtils.convertToEntityList(car.getComment(), CommentDTO.class);
    }

    @Override
    public Page<CarResponse> getAllCarForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(carRepository.searchingCarForAdmin(title, approve ,pageable), CarResponse.class, pageable);
    }

    @Override
    public Page<CarResponse> getCarByUserId(Long userId, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(carRepository.searchingCarForCustomer(null,null,null,userId, pageable, null), CarResponse.class, pageable);
    }

    private List<CarResponse> sortCars(List<CarResponse> cars, String typeSort) {
        if ("Thời gian: Mới đến cũ".equals(typeSort)) {
            cars.sort(Comparator.comparing(CarResponse::getCreatedAt).reversed());
        } else if ("Thời gian: Cũ đến mới".equals(typeSort)) {
            cars.sort(Comparator.comparing(CarResponse::getCreatedAt));
        } else if ("Giá: Thấp đến cao".equals(typeSort)) {
            cars.sort(Comparator.comparing(CarResponse::getPrice));
        } else if ("Giá: Cao đến thấp".equals(typeSort)) {
            cars.sort(Comparator.comparing(CarResponse::getPrice).reversed());
        }
        
        return cars;
    }


    @Override
    public MessageResponse checkoutCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new BadRequestException("Phòng không còn tồn tại"));
        car.setStatus(CarStatus.CHECKED_OUT);
        carRepository.save(car);
        return MessageResponse.builder().message("Trả phòng và xuất hóa đơn thành công.").build();
    }

    @Override
    public MessageResponse isApproveCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new BadRequestException("Phòng không còn tồn tại"));
        if (car.getIsApprove().equals(Boolean.TRUE)) {
            throw new BadRequestException("Phòng đã được phê duyệt");
        } else {
            car.setIsApprove(Boolean.TRUE);
        }
        carRepository.save(car);
        return MessageResponse.builder().message("Phê duyệt tin phòng thành công.").build();
    }

    @Override
    public MessageResponse removeCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new BadRequestException("Phòng không còn tồn tại"));
        if(Boolean.TRUE.equals(car.getIsRemove())){
            throw new BadRequestException("Bài đăng đã bị gỡ");
        }
        car.setIsRemove(Boolean.TRUE);
        carRepository.save(car);
        return MessageResponse.builder().message("Bài đăng đã bị gỡ thành công").build();
    }

	@Override
	public String addComment(Long id, CommentDTO commentDTO) {
		try {
			Car car = carRepository.findById(commentDTO.getCar_id()).orElseThrow(() -> new BadRequestException("Phòng không còn tồn tại"));
			User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("Người dùng không tồn tại"));
			Rate rate = new Rate();
			rate.setRating(commentDTO.getRateRating());
			rate.setUser(user);
			rate.setCar(car);
			Comment comment = new Comment(commentDTO.getContent(), user, car, rate);
			commentRepository.save(comment);
			return "Thêm bình luận thành công";
		} catch (Exception e) {
			return "Thêm bình luận thất bại";
		}
		
	}
	
	private User getUser() {
        return userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException("Người dùng không tồn tại"));
    }
    public int updateCar(){
        return carRepository.updateCheckedOutCars();

    }



}
