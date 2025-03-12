package vn.fpt.tranduykhanh.bookingservicepetshop.HandlerException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.fpt.tranduykhanh.bookingservicepetshop.response.ResponseObj;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseObj> handleJsonParseException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObj(HttpStatus.BAD_REQUEST.toString(), "Found Bad Request", "RoleName has to be correct one of 3 value [ADMIN] [CUSTOMER] [STAFF]"));
    }
}
