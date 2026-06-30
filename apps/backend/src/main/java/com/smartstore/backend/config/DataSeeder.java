package com.smartstore.backend.config;

import com.smartstore.backend.entities.Category;
import com.smartstore.backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder
        implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

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

    }

}