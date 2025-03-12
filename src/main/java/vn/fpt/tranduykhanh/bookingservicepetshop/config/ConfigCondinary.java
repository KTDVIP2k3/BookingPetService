package vn.fpt.tranduykhanh.bookingservicepetshop.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCondinary {

    @Bean
    public Cloudinary configKey(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dky9dvlho");
        config.put("api_key", "297991486574694");
        config.put("api_secret", "olb0LiQ8wmu7uJkCkYMsfxbEeUc");
        return new Cloudinary(config);
    }
}
