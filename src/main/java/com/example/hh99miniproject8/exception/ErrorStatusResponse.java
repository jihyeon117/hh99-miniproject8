package com.example.hh99miniproject8.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

@Getter
@Builder
public class ErrorStatusResponse {
    private String errorMessage;
    private int statusCode;

    public static ResponseEntity<ErrorStatusResponse> errorStatus(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorStatusResponse.builder()
                        .errorMessage(errorCode.getErrorMessage())
                        .statusCode(errorCode.getHttpStatus().value())
                        .build()

                );
    }
}
