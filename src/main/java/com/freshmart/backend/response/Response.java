package com.freshmart.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Response<T> {
    private int statusCode;
    private String message;
    private boolean success = false;
    private T data;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.success = (statusCode == HttpStatus.OK.value());
    }

    public static <T>ResponseEntity<Response<T>> failed (String message) {
        return failed(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <T>ResponseEntity<Response<T>> failed(T data) {
        return failed(HttpStatus.BAD_REQUEST.value(), "Bad request", data);
    }

    public static <T>ResponseEntity<Response<T>> failed(int statusCode, String message) {
        return failed(statusCode, message, null);
    }

    public static <T>ResponseEntity<Response<T>> failed(int statusCode, String message, T data) {
        Response<T> response = new Response<>(statusCode, message, false, data);
        return ResponseEntity.status(statusCode).body(response);
    }

    public static <T>ResponseEntity<Response<T>> success(String message) {
        return success(HttpStatus.OK.value(), message, null);
    }

    public static <T>ResponseEntity<Response<T>> success(String message, T data) {
        return success(HttpStatus.OK.value(), message, data);
    }

    public static <T>ResponseEntity<Response<T>> success(int statusCode, String message, T data) {
        Response<T> response = new Response<>(statusCode, message, true, data);
        return ResponseEntity.status(statusCode).body(response);
    }
}
