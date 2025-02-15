package vn.fpt.tranduykhanh.bookingservicepetshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Cho phép truy cập tất cả API
                .csrf(csrf -> csrf.disable()); // Tắt CSRF nếu cần
        return http.build();
    }
}
