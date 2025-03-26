package vn.fpt.tranduykhanh.bookingservicepetshop.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
public class BookingDTO {
    private LocalDate localDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "08:30:00")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(type = "string", pattern = "HH:mm:ss", example = "10:30:00")
    private LocalTime endTime;

    private Long petId;
    private Long serviceId;
    private Long optionalServiceId;
    private Long paymentId;
}
