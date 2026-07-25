package com.smartstore.backend.controllers;

import com.smartstore.backend.entities.Role;
import com.smartstore.backend.entities.User;
import com.smartstore.backend.repositories.UserRepository;
import com.smartstore.backend.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    // Patrón "singleton container": se arranca manualmente (sin @Testcontainers/@Container)
    // para que Ryuk lo limpie al terminar la JVM en vez de que JUnit lo detenga
    // entre clases de test que comparten este mismo contenedor.
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        POSTGRES.start();
    }

    @LocalServerPort
    protected int port;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;

    protected final TestRestTemplate restTemplate = new TestRestTemplate();

    protected String url(String path) {
        return "http://localhost:" + port + "/api" + path;
    }

    protected HttpHeaders adminAuthHeaders() {

        String email = "admin-" + System.nanoTime() + "@smartstore.test";

        User admin = User.builder()
                .firstName("Admin")
                .lastName("Test")
                .email(email)
                .password(passwordEncoder.encode("password"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtService.generateAccessToken(admin));

        return headers;

    }

}
