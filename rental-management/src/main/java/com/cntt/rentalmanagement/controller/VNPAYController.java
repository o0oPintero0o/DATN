package com.cntt.rentalmanagement.controller;

import com.cntt.rentalmanagement.config.VnpayConfig;
import com.cntt.rentalmanagement.domain.enums.LockedStatus;
import com.cntt.rentalmanagement.domain.enums.CarStatus;
import com.cntt.rentalmanagement.domain.models.Contract;
import com.cntt.rentalmanagement.domain.models.Car;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.request.PaymentRestDTO;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.ContractRepository;
import com.cntt.rentalmanagement.repository.CarRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class VNPAYController extends BaseService  {

    private final CarRepository roomRepository;

    private final FileStorageService fileStorageService;

    private final UserRepository userRepository;

    private final ContractRepository contractRepository;


    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/create_payment")
    @CrossOrigin
    public ResponseEntity<?> createPayment(
            @RequestParam Long roomId,
            @RequestParam String nameOfRent,
            @RequestParam Long numOfPeople,
            @RequestParam String phone,
            @RequestParam BigDecimal totalPrice,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") String startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") String endDate
    ) throws IOException, MessagingException {
        BigDecimal number = new BigDecimal("100");

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String bankCode = "NCB";
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalPrice.multiply(number)));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        Car car = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));
        if (car.getIsLocked().equals(LockedStatus.DISABLE)) {
            throw new BadRequestException("Phòng đã bị khóa");
        }




        Contract contract = new Contract("Hợp đồng thành toán toán online" + nameOfRent,null, nameOfRent, startDate, endDate ,getUsername(), getUsername(), car);
        contract.setNumOfPeople(numOfPeople);
        contract.setPhone(phone);
        contract.setUserId(getUserId());
        contract.setIsInvoice(false);
        BigDecimal total = car.getPrice().multiply(BigDecimal.valueOf(getLongDay(startDate, endDate)));
        contract.setTotalPrice(total);
        contractRepository.save(contract);

        car.setStatus(CarStatus.HIRED);
        roomRepository.save(car);

        String paymentUrl = VnpayConfig.vnp_PayUrl  + "?" + queryUrl;


        PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        paymentRestDTO.setStatus("OK");
        paymentRestDTO.setMessage("Successfully");
        paymentRestDTO.setURL(paymentUrl);

        User user = userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException("Tài khoản không tồn tại"));
        sendEmailFromTemplate(user.getEmail(), user, contract);

        return ResponseEntity.ok(paymentRestDTO);
    }

    @PostMapping("send-invoice/{id}")
    private ResponseEntity<?> sendInvoice(@PathVariable Long id) throws MessagingException, IOException {
        return ResponseEntity.ok("Gửi email hóa đơn thành công");
    }

    private Long getLongDay(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);

        return ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
    }

    public void sendEmailFromTemplate(String email, User user, Contract contract) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress("khanhhn.hoang@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Hóa đơn thanh toán homestay/villa");

        // Read the HTML template into a String variable
        String htmlTemplate = readFile("order-success.html");

        htmlTemplate = htmlTemplate.replace("[Order ID]", contract.getId().toString());
        htmlTemplate = htmlTemplate.replace("[Order Date]", contract.getCreatedBy());
        htmlTemplate = htmlTemplate.replace("[Full Name]", user.getName());
        htmlTemplate = htmlTemplate.replace("[Address]", "Sài Gòn");
        htmlTemplate = htmlTemplate.replace("[Phone Number]", contract.getPhone());
        htmlTemplate = htmlTemplate.replace("[Perfumer]", contract.getCar().getTitle());
        htmlTemplate = htmlTemplate.replace("[Perfume Title]", contract.getCar().getTitle());
        htmlTemplate = htmlTemplate.replace("[Perfume Type]",contract.getCar().getAddress());
        htmlTemplate = htmlTemplate.replace("[Perfume Volume]", contract.getCar().getCategory().getName());
        htmlTemplate = htmlTemplate.replace("[Perfume Price]", contract.getCar().getPrice().toString());
        htmlTemplate = htmlTemplate.replace("[Total Price]", contract.getTotalPrice().toString());


        // Set the email's content to be the HTML template
        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public static String readFile(String filename) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + filename);
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, StandardCharsets.UTF_8);
    }

}
