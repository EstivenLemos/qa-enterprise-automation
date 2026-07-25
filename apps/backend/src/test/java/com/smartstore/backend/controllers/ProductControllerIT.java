package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.category.CategoryRequestDTO;
import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.dto.product.ProductRequestDTO;
import com.smartstore.backend.dto.product.ProductResponseDTO;
import com.smartstore.backend.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductControllerIT extends AbstractIntegrationTest {

    @Test
    void create_thenFindById_roundTrips() {

        var headers = adminAuthHeaders();

        Long categoryId = createCategory(headers, "Gaming");

        ProductRequestDTO request = new ProductRequestDTO(
                "Console", "Next-gen console", new BigDecimal("499.99"), 10, categoryId
        );

        ResponseEntity<ApiResponse<ProductResponseDTO>> createResponse = restTemplate.exchange(
                url("/products"),
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ProductResponseDTO created = createResponse.getBody().data();
        assertThat(created.category()).isEqualTo("Gaming");

        ResponseEntity<ApiResponse<ProductResponseDTO>> getResponse = restTemplate.exchange(
                url("/products/" + created.id()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().data().name()).isEqualTo("Console");

    }

    @Test
    void create_withUnknownCategory_returnsNotFound() {

        ProductRequestDTO request = new ProductRequestDTO(
                "Console", "desc", new BigDecimal("100"), 1, 999999L
        );

        ResponseEntity<ApiResponse<Object>> response = restTemplate.exchange(
                url("/products"),
                HttpMethod.POST,
                new HttpEntity<>(request, adminAuthHeaders()),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void create_withInvalidPrice_returnsBadRequest() {

        var headers = adminAuthHeaders();
        Long categoryId = createCategory(headers, "Misc");

        ProductRequestDTO request = new ProductRequestDTO(
                "Console", "desc", new BigDecimal("-10"), 1, categoryId
        );

        ResponseEntity<ApiResponse<Object>> response = restTemplate.exchange(
                url("/products"),
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    private Long createCategory(org.springframework.http.HttpHeaders headers, String name) {

        ResponseEntity<ApiResponse<CategoryResponseDTO>> response = restTemplate.exchange(
                url("/categories"),
                HttpMethod.POST,
                new HttpEntity<>(new CategoryRequestDTO(name), headers),
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().data().id();

    }

}
