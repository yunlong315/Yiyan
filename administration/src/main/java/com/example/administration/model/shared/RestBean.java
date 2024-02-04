package com.example.administration.model.shared;


import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Data
public class RestBean<T> {
    private int status;
    private boolean success;
    private T message;

    public RestBean(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }


    public static <T>ResponseEntity<RestBean> success(T data) {
        return ResponseEntity.ok(new RestBean<>(200, true, data));
    }
    public static <T>ResponseEntity<RestBean> success(int status, T data) {
        return ResponseEntity.status(HttpStatusCode.valueOf(status)).body(new RestBean<>(200, true, data));
    }
    public static <T>ResponseEntity<RestBean> success(HttpStatusCode status, T data) {
        return ResponseEntity.status(status).body(new RestBean<>(status.value(), true, data));
    }

    public static <T> ResponseEntity<RestBean> failure(HttpStatusCode status, T data) {
        return ResponseEntity.status(status).body(new RestBean<>(status.value(), false, data));
    }
    public static <T> ResponseEntity<RestBean> failure(int status, T data) {
        return ResponseEntity.status(HttpStatusCode.valueOf(status)).body(new RestBean<>(status, false, data));
    }
}
