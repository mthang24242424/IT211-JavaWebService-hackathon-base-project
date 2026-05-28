package re.edu.hackathon.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private Object error;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), message, data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, Object error) {
        return new ApiResponse<>(status.value(), message, null, error, LocalDateTime.now());
    }
}
