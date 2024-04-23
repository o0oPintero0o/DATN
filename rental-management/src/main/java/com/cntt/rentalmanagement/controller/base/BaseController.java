package com.cntt.rentalmanagement.controller.base;



import com.cntt.rentalmanagement.controller.base.message.BaseMessage;
import com.cntt.rentalmanagement.controller.base.message.ExtendedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    /**
     * Nơi nhận dữ liệu truyền từ các Controller khi trả
     * về và khi được khai báo ở Controller.
     * @param message
     * @param description
     * @param data
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<?> createSuccessResponse(String message, String description, T data) {
        ExtendedMessage<T> responseMessage = new ExtendedMessage<>(
                HttpStatus.OK.value() + "",
                true,
                message,
                description,
                data);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public <T> ResponseEntity<?> createSuccessResponse(String message, T data) {
        return createSuccessResponse(message, null, data);
    }

    public <T> ResponseEntity<?> createSuccessResponse(T data) {
        return createSuccessResponse(null, null, data);
    }

    public ResponseEntity<?> createSuccessResponse() {
        return createSuccessResponse(null, null, null);
    }

    /**
     * Tương tự ở trên và đây xử lý các hàm trả về failuer response
     * @param code
     * @param message
     * @param description
     * @return
     */
    public ResponseEntity<?> createFailureResponse(String code, String message, String description) {
        BaseMessage responseMessage = new BaseMessage(
                code,
                false,
                message,
                description);
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(Integer.parseInt(code)));
    }
}

