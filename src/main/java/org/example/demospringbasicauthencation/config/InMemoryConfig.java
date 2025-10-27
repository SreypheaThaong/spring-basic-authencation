package org.example.demospringbasicauthencation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//@RequiredArgsConstructor
//public class InMemoryConfig {
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User
//                .withUsername("user1")
//                .password(passwordEncoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin1 = User
//                .withUsername("admin1")
//                .password(passwordEncoder.encode("123"))
//                .roles("ADMIN")
//                .build();
//
////        InMemoryUserDetailsManager memory = new InMemoryUserDetailsManager(user1, admin1);
////        System.out.println(memory.loadUserByUsername("user1"));
//        return new InMemoryUserDetailsManager(user1, admin1);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request
//                .requestMatchers("/admin").hasRole("USER")
//                .requestMatchers("/user").hasAnyRole("ADMIN", "USER")
//                .requestMatchers("/test").permitAll()
//                .anyRequest().authenticated()
//        )
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//}
