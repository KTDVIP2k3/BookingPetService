package vn.fpt.tranduykhanh.bookingservicepetshop.response;

import lombok.*;

@Builder
@Data
public class ResponseObj {
    private String status;
    private String message;
    private Object data;

    public ResponseObj(){}

    public ResponseObj(String status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
