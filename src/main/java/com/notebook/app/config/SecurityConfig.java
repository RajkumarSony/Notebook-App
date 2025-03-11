package com.notebook.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // Allow access to contact page and public resources and deny access to admin page for all requests by default
        http.authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/contact").permitAll()        // Allow access to contact page
                        .requestMatchers("/public/**").permitAll()      // Allow access to public resources
                        .requestMatchers("/admin").denyAll()            // Deny access to admin page
                        .anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);

        // Disable session creation by default in Spring Security 5 to prevent session fixation vulnerabilities and improve security
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //        http.formLogin(Customizer.withDefaults());

        // Enable basic authentication for all requests
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
