package org.ecommerce.ecommerceapi.utils;

import org.ecommerce.ecommerceapi.model.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {
    public static ResponseEntity<ApiResponse> build(HttpStatus status, String message, Object data) {
        var response = new ApiResponse();
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(status.value());
        response.setData(data);
        return new ResponseEntity<>(response, status);
    }
}

