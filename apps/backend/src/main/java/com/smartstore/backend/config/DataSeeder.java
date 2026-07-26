package com.smartstore.backend.config;

import com.smartstore.backend.entities.Category;
import com.smartstore.backend.entities.Role;
import com.smartstore.backend.entities.User;
import com.smartstore.backend.repositories.CategoryRepository;
import com.smartstore.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder
        implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.seed.email}")
    private String adminSeedEmail;

    @Value("${admin.seed.password}")
    private String adminSeedPassword;

    @Override
    public void run(
            String... args
    ) {

        if (
                categoryRepository.count() == 0
        ) {

            categoryRepository.save(
                    Category.builder()
                            .name("Electronics")
                            .build()
            );

            categoryRepository.save(
                    Category.builder()
                            .name("Books")
                            .build()
            );

            categoryRepository.save(
                    Category.builder()
                            .name("Sports")
                            .build()
            );

        }

        if (
                userRepository.findByEmail(adminSeedEmail).isEmpty()
        ) {

            userRepository.save(
                    User.builder()
                            .firstName("Admin")
                            .lastName("SmartStore")
                            .email(adminSeedEmail)
                            .password(passwordEncoder.encode(adminSeedPassword))
                            .role(Role.ADMIN)
                            .build()
            );

        }

    }

}
