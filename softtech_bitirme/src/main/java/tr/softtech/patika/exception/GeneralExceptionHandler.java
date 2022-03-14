package tr.softtech.patika.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import tr.softtech.patika.dto.GenericResponseDto;
import tr.softtech.patika.model.exception.ErrorMessage;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.MissingFormatArgumentException;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<GenericResponseDto> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(GenericResponseDto.error(errorMessage, errorMessage.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<GenericResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(GenericResponseDto.error(errorMessage, errorMessage.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<GenericResponseDto> handleInvalidStatusException(IllegalStateException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity(GenericResponseDto.error(errorMessage, errorMessage.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {MissingFormatArgumentException.class})
    public ResponseEntity<GenericResponseDto> handleMissingArgumentsException(MissingFormatArgumentException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity(GenericResponseDto.error(errorMessage, errorMessage.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidPropertiesFormatException.class})
    public ResponseEntity<GenericResponseDto> handleInvalidPropertiesFormatException(InvalidPropertiesFormatException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity(GenericResponseDto.error(errorMessage, errorMessage.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GenericResponseDto> globalExceptionHandler(Exception ex, WebRequest webRequest) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity(GenericResponseDto.error(errorMessage, errorMessage.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<GenericResponseDto> handleItemNotFoundException(ItemNotFoundException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity(GenericResponseDto.error(errorMessage, errorMessage.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UsernameAlreadyExistException.class})
    public ResponseEntity<GenericResponseDto>  handleUsernameAlreadyExistException(UsernameAlreadyExistException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(GenericResponseDto.error(errorMessage, errorMessage.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {ItemAlreadyExistException.class})
    public ResponseEntity<GenericResponseDto> handleFileAlreadyExistsException(ItemAlreadyExistException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(GenericResponseDto.error(errorMessage, errorMessage.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<GenericResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(new Date())
                .message(ex.getFieldError().getDefaultMessage())
                .description(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(GenericResponseDto.error(errorMessage, errorMessage.getMessage()),HttpStatus.BAD_REQUEST);
    }

}
