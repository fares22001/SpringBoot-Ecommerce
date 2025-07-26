package com.project.project.services;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.project.model.Category;

@Service
public class categoryservice {
    private RestTemplate restTemplate;
    private String baseUrl = "http://localhost:8085";

    public categoryservice() {
        this.restTemplate = new RestTemplate();
    }

    public List<Category> findAll() {
        try {
            String url = baseUrl + "/categories";
            return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Category>>() {}
            ).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch categories", e);
        }
    }

    public void save(Category category) {
        String url = baseUrl + "/categories/add";
        restTemplate.postForObject(url, category, Category.class);
    }

    public void delete(int id) {
        try {
            String url = baseUrl + "/categories/delete/" + id;
            restTemplate.delete(url);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to delete category", e);
        }
    }
}
