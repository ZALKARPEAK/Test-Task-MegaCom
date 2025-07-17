package com.career.testtaskmegacom.service.impl;

import com.career.testtaskmegacom.service.ExternalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceImpl.class);

    @Override
    public String fetchAndLogExternalData() {
        String url = "https://api.restful-api.dev/objects";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            logger.info("HTTP GET to {} succeeded with status: {}", url, response.getStatusCode());
            logger.info("Response body: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error during GET request to {}: {}", url, e.getMessage(), e);
        }

        return null;
    }
}