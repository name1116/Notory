package com.pearl_castle.notory.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/", "/home", "/join").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .defaultSuccessUrl("/home")
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .permitAll()
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}