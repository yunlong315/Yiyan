package yiyan.research.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
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

    public static <T> ResponseEntity<RestBean<T>> success(T data) {
        return ResponseEntity.ok(new RestBean<>(HttpStatus.OK.value(), true, data));
    }

    public static <T> ResponseEntity<RestBean<T>> success(int status, T data) {
        return ResponseEntity.status(status).body(new RestBean<>(status, true, data));
    }

    public static <T> ResponseEntity<RestBean<T>> success(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new RestBean<>(status.value(), true, data));
    }

    public static <T> ResponseEntity<RestBean<T>> failure(HttpStatus status, T data) {
        return ResponseEntity.status(status).body(new RestBean<>(status.value(), false, data));
    }

    public static <T> ResponseEntity<RestBean<T>> failure(int status, T data) {
        return ResponseEntity.status(status).body(new RestBean<>(status, false, data));
    }
}
