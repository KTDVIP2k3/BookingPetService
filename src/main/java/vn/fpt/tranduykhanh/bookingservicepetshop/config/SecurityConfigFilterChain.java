    package vn.fpt.tranduykhanh.bookingservicepetshop.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
    import vn.fpt.tranduykhanh.bookingservicepetshop.HandlerException.CustomAccessDeniedHandler;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfigFilterChain {
        @Autowired
        private JWTAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        private CustomAccessDeniedHandler customAccessDeniedHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(customizer -> customizer.disable())
                    .cors(customizer -> customizer.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests(auth ->
    //                        auth.requestMatchers("/api/user/getAllAccount").hasAuthority("ADMIN")
    //                        .requestMatchers("api/user/**").permitAll()
                            auth.anyRequest().permitAll())
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling(cus -> cus.accessDeniedHandler(customAccessDeniedHandler));
            http.httpBasic(customizer -> customizer.disable());
            http.formLogin(customizer -> customizer.disable());
            return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("http://localhost:5173"); // Cho phép frontend trên localhost:5173
            configuration.addAllowedMethod("*"); // Cho phép tất cả phương thức HTTP (GET, POST, PUT, DELETE, ...)
            configuration.addAllowedHeader("*"); // Cho phép tất cả header (content-type, authorization, ...)

            // Nếu backend của bạn sử dụng cookie hay xác thực session, bạn có thể cần bật thêm những tùy chọn dưới đây:
            // configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration); // Áp dụng cấu hình cho tất cả các đường dẫn
            return source;
        }
    }
