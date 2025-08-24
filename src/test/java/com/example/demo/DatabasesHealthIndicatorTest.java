package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DatabasesHealthIndicatorTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnHealthyStatusWhenDatabasesAreUp() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/actuator/health", Map.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("status")).isEqualTo("UP");
        
        Map<String, Object> components = (Map<String, Object>) response.getBody().get("components");
        Map<String, Object> databases = (Map<String, Object>) components.get("databases");
        assertThat(databases.get("status")).isEqualTo("UP");
        
        Map<String, Object> details = (Map<String, Object>) databases.get("details");
        
        // Check nested redis health
        Map<String, Object> redis = (Map<String, Object>) details.get("redis");
        assertThat(redis.get("status")).isEqualTo("UP");
        
        // Check nested mariadb health
        Map<String, Object> mariadb = (Map<String, Object>) details.get("mariadb");
        assertThat(mariadb.get("status")).isEqualTo("UP");
    }

    @Test
    void shouldReturnDatabasesHealthDetails() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/actuator/health/databases", Map.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("status")).isEqualTo("UP");

        var details = (Map<String, Object>) response.getBody().get("details");

        // Check nested redis health
        Map<String, Object> redis = (Map<String, Object>) details.get("redis");
        assertThat(redis.get("status")).isEqualTo("UP");
        
        // Check nested mariadb health  
        Map<String, Object> mariadb = (Map<String, Object>) details.get("mariadb");
        assertThat(mariadb.get("status")).isEqualTo("UP");
    }
}