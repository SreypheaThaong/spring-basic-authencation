package org.example.demospringbasicauthencation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
        @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                .requestMatchers("/admin").hasRole("USER")
                .requestMatchers("/user").hasAnyRole("ADMIN", "USER")
                .requestMatchers(
                        "/test",
                        "/api/v1/users/register",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
        );
                http.httpBasic(Customizer.withDefaults());
//                http.formLogin(Customizer.withDefaults());

        return http.build();
    }
}
