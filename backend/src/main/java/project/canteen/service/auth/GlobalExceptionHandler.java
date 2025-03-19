package project.canteen.service.auth;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import project.canteen.model.auth.ResponMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi chung (Exception - 500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponMessage handleException(Exception ex) {
        return new ResponMessage(500L, "Lỗi hệ thống: " + ex.getMessage(), null);
    }

    // Xử lý lỗi tham số không hợp lệ (IllegalArgumentException - 400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponMessage handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponMessage(400L, ex.getMessage(), null);
    }

    // Xử lý lỗi không tìm thấy dữ liệu (EntityNotFoundException - 404 Not Found)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponMessage handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponMessage(404L, "Không tìm thấy dữ liệu: " + ex.getMessage(), null);
    }

    // Xử lý lỗi tải lên file quá lớn (MaxUploadSizeExceededException - 413 Payload Too Large)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponMessage handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        System.out.println("Lỗi file quá lớn đã được xử lý!");
        return new ResponMessage(413L, "File quá lớn! Vui lòng chọn file nhỏ hơn.", null);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponMessage handleNotFoundException(NoHandlerFoundException ex) {
        return new ResponMessage(404L, "API không tồn tại: " + ex.getRequestURL(), null);
    }
}
