package com.altis.library.config;

import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createDefaultAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.name}") String name,
            @Value("${admin.email}") String email,
            @Value("${admin.password}") String password
    ) {
        return args -> initializeAdmin(
                userRepository,
                passwordEncoder,
                name,
                email,
                password
        );
    }

    @Transactional
    void initializeAdmin(UserRepository userRepository,
                         PasswordEncoder passwordEncoder,
                         String name,
                         String email,
                         String password) {
        UserEntity admin = userRepository.findByEmail(email)
                .orElseGet(UserEntity::new);

        LocalDateTime now = LocalDateTime.now();

        admin.setName(name);
        admin.setBirthDate(LocalDate.of(1990, 1, 1));
        admin.setCpf("00000000191");
        admin.setPhone("85999999999");
        admin.setAddress("Biblioteca Altis");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setAdmin(true);
        admin.setActive(true);

        if (admin.getCreatedAt() == null) {
            admin.setCreatedAt(now);
        }
        admin.setUpdatedAt(now);

        userRepository.save(admin);
    }
}
