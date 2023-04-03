package me.ilias.ressources;

import me.ilias.exceptions.ExceptionResponse;
import me.ilias.exceptions.NoEntityFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionsHandler {

    public static final int STATUS_400 = 400;

    @ExceptionHandler(NoEntityFoundException.class)
    ResponseEntity<ExceptionResponse> handleNoEntityFound(NoEntityFoundException exception)
    {
        int status=404;
        return ResponseEntity.status(status).body(ExceptionResponse.builder().message(exception.getMessage()).status(status).build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException exception)
    {
        return ResponseEntity.status(STATUS_400).body(ExceptionResponse.builder().message(exception.getMessage()).status(STATUS_400).build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(STATUS_400).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ExceptionResponse> globalHandler(Exception exception)
    {
        return ResponseEntity.status(STATUS_400).body(ExceptionResponse.builder().message(exception.getMessage()).status(STATUS_400).build());
    }
}
