package org.example.flyora_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ✅ Cho phép Swagger UI truy cập mà không cần xác thực
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()

                // ✅ API public
                .requestMatchers("/api/public/**").permitAll()

                // 🔐 API chỉ dành cho ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 🔐 Các request còn lại cần xác thực
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {}); // Dùng xác thực HTTP Basic

        return http.build();
    }
}
