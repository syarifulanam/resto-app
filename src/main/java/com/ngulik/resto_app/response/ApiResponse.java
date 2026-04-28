package com.ngulik.resto_app.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private PaginationMeta pagination;

    // NOTE: Static Helper method untuk response sukses (tanpa pagination)
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // NOTE: Static helper method untuk response sukses (dengan pagination)
    public static <T> ApiResponse<T> success(String message, T data, PaginationMeta pagination) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .pagination(pagination)
                .build();
    }

    // NOTE: Static helper method untuk response gagal
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    // NOTE: Static helper method untuk response gagal dengan data tambahan (optional)
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }

}
