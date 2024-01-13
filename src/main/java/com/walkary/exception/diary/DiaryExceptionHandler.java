package com.walkary.exception.diary;
import com.walkary.models.dto.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DiaryExceptionHandler {

    @ExceptionHandler(DiaryNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleDiaryNotFoundException(DiaryNotFoundException e) {
        return ResponseEntity.ok(ErrorMessage.builder().message(e.getMessage()).build());
    }
}

