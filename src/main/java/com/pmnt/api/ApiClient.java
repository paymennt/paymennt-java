package com.pmnt.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.pmnt.api.model.Checkout;
import com.pmnt.api.request.WebCheckoutRequest;
import com.pmnt.api.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class ApiClient {

    private static final String API_KEY_HEADER = "X-Paymennt-Api-Key";
    private static final String API_SECRET_HEADER = "X-Paymennt-Api-Secret";

    private final String apiKey;
    private final String apiSecret;
    private final String publicKey;
    private final PaymenntEnvironment env;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ApiClient(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, PaymenntEnvironment.LOCAL, null);
    }

    public ApiClient(String apiKey, String apiSecret, PaymenntEnvironment env, String publicKey) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.publicKey = publicKey;
        this.env = env;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JodaModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Checkout createWebCheckout(WebCheckoutRequest webCheckoutRequest) throws JsonProcessingException {
        webCheckoutRequest.validate();
        String requestBody = objectMapper.writeValueAsString(webCheckoutRequest);
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        String url = env.getBaseUrl() + "checkout/web";

        return apiCall(url, HttpMethod.POST, httpEntity, Checkout.class);
    }

    public Checkout getCheckout(String checkoutId) {
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        String url = env.getBaseUrl() + "checkout/" + checkoutId;

        return apiCall(url, HttpMethod.GET, httpEntity, Checkout.class);
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(API_KEY_HEADER, apiKey);
        headers.set(API_SECRET_HEADER, apiSecret);
        return headers;
    }

    private <T> T apiCall(String url, HttpMethod method, HttpEntity<String> httpEntity, Class<T> responseType) {
        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url, method, httpEntity, ApiResponse.class);

        return objectMapper.convertValue(response.getBody().getResult(), responseType);
    }


    private enum PaymenntEnvironment {
        LIVE("https://api.paymennt.com/mer/v2.0/"),
        TEST("https://api.test.paymennt.com/mer/v2.0/"),
        LOCAL("http://localhost:8080/api/mer/v2.0/");

        private final String baseUrl;

        PaymenntEnvironment(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getBaseUrl() {
            return baseUrl;
        }
    }
}
