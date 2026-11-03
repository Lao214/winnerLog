package com.example.winnerLogBackend.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "统一API响应包装类")
public class ApiResponse<T> {

    @Schema(description = "响应状态码：200为成功，非200为异常", example = "200")
    private int code;

    @Schema(description = "响应消息提示", example = "操作成功")
    private String message;

    @Schema(description = "响应业务数据")
    private T data;

    @Schema(description = "响应时间")
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse() {}

    public ApiResponse(int code, String message, T data, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private int code;
        private String message;
        private T data;
        private LocalDateTime timestamp = LocalDateTime.now();

        public ApiResponseBuilder<T> code(int code) { this.code = code; return this; }
        public ApiResponseBuilder<T> message(String message) { this.message = message; return this; }
        public ApiResponseBuilder<T> data(T data) { this.data = data; return this; }
        public ApiResponseBuilder<T> timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ApiResponse<T> build() {
            return new ApiResponse<>(code, message, data, timestamp);
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(500, message);
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
