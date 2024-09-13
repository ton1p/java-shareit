package ru.practicum.shareit.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BaseClient {
    private final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    protected <T> ResponseEntity<T> get(String path) {
        return get(path, null, null);
    }

    protected <T> ResponseEntity<T> get(String path, long userId) {
        return get(path, userId, null);
    }

    protected <T> ResponseEntity<T> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return (ResponseEntity<T>) makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <B, T> ResponseEntity<T> post(String path, B body) {
        return post(path, null, null, body);
    }

    protected <B, T> ResponseEntity<T> post(String path, long userId, B body) {
        return post(path, userId, null, body);
    }

    protected <B, T> ResponseEntity<T> post(String path, Long userId, @Nullable Map<String, Object> parameters, B body) {
        return (ResponseEntity<T>) makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <B, T> ResponseEntity<T> put(String path, long userId, B body) {
        return put(path, userId, null, body);
    }

    protected <B, T> ResponseEntity<T> put(String path, long userId, @Nullable Map<String, Object> parameters, B body) {
        return (ResponseEntity<T>) makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <B, T> ResponseEntity<T> patch(String path, B body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<T> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    protected <B, T> ResponseEntity<T> patch(String path, long userId, B body) {
        return patch(path, userId, null, body);
    }

    protected <B, T> ResponseEntity<T> patch(String path, Long userId, @Nullable Map<String, Object> parameters, B body) {
        return (ResponseEntity<T>) makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<T> delete(String path) {
        return delete(path, null, null);
    }

    protected <T> ResponseEntity<T> delete(String path, long userId) {
        return delete(path, userId, null);
    }

    protected <T> ResponseEntity<T> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return (ResponseEntity<T>) makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    private <B> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId, @Nullable Map<String, Object> parameters, @Nullable B body) {
        HttpEntity<B> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> shareitServerResponse;
        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(shareitServerResponse);
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
