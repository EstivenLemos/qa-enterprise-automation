package com.smartstore.backend.controllers;

import com.smartstore.backend.dto.category.CategoryRequestDTO;
import com.smartstore.backend.dto.category.CategoryResponseDTO;
import com.smartstore.backend.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryControllerIT extends AbstractIntegrationTest {

    @Test
    void create_thenFindById_roundTrips() {

        CategoryRequestDTO request = new CategoryRequestDTO("Toys");

        ResponseEntity<ApiResponse<CategoryResponseDTO>> createResponse = restTemplate.exchange(
                url("/categories"),
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(request, adminAuthHeaders()),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        assertThat(createResponse.getBody().success()).isTrue();

        Long createdId = createResponse.getBody().data().id();

        ResponseEntity<ApiResponse<CategoryResponseDTO>> getResponse = restTemplate.exchange(
                url("/categories/" + createdId),
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().data().name()).isEqualTo("Toys");

    }

    @Test
    void create_withBlankName_returnsBadRequest() {

        CategoryRequestDTO invalidRequest = new CategoryRequestDTO(" ");

        ResponseEntity<ApiResponse<Object>> response = restTemplate.exchange(
                url("/categories"),
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(invalidRequest, adminAuthHeaders()),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().success()).isFalse();

    }

    @Test
    void create_withoutAuthentication_returnsUnauthorized() {

        CategoryRequestDTO request = new CategoryRequestDTO("Toys");

        ResponseEntity<ApiResponse<Object>> response = restTemplate.exchange(
                url("/categories"),
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(request),
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);

    }

    @Test
    void findById_whenMissing_returnsNotFound() {

        ResponseEntity<ApiResponse<Object>> response = restTemplate.exchange(
                url("/categories/999999"),
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

}
