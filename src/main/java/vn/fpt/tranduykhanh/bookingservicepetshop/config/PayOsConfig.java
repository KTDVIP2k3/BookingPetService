package vn.fpt.tranduykhanh.bookingservicepetshop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOsConfig {

    @Value("${payos.client_id}")
    private String clientId;

    @Value("${payos.api_key}")
    private String apiKey;

    @Value("${payos.secret_key}")
    private String secretKey;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, secretKey);
    }
}
