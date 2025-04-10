package vn.fpt.tranduykhanh.bookingservicepetshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Booking Service API", version = "1.0", description = "API documentation for Booking Pet Shop"))
@EnableScheduling
public class BookingservicepetshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingservicepetshopApplication.class, args);
	}

}
