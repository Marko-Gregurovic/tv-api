package fer.hr.tvapi.exception;

import fer.hr.tvapi.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AdviceHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDto> handleBadRequestException(HttpServletRequest req, BadRequestException exception) {
        ErrorDto errorDto = ErrorDto
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDto> handleNotFoundException(HttpServletRequest req, Exception exception) {
        ErrorDto errorDto = ErrorDto
                .builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorDto> handleForbiddenException(HttpServletRequest req, Exception exception) {
        ErrorDto errorDto = ErrorDto
                .builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorDto);
    }


}
