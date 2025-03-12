package com.notebook.app.security;

import com.notebook.app.models.Role;
import com.notebook.app.models.RoleName;
import com.notebook.app.models.User;
import com.notebook.app.repositories.RoleRepository;
import com.notebook.app.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        // http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(RoleName.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(RoleName.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", "{noop}password1");
                user1.setAccountNonLocked(false);
                setUserData(userRepository, userRole, user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", "{noop}password");
                admin.setAccountNonLocked(true);
                setUserData(userRepository, adminRole, admin);
            }
        };
    }

    private void setUserData(UserRepository userRepository, Role adminRole, User admin) {
        admin.setAccountNonExpired(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
        admin.setTwoFactorEnabled(false);
        admin.setSignUpMethod("email");
        admin.setRole(adminRole);
        userRepository.save(admin);
    }
}