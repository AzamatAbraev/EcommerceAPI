package org.ecommerce.ecommerceapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiExceptionResponse {
    private String message;
    private HttpStatus status;
    private int code;
    private String path;
    private LocalDateTime timestamp;
    private List<ValidationException> errors;
}

