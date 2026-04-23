package com.ngulik.resto_app.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.awt.image.PixelGrabber;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .build();
    }

    public  static <T> ApiResponse<T> success(String message) {
        return success(message, null);
    }
}
