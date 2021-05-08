/*
 * package com.Inventory.Project.AssectService.Exception;
 * 
 * import java.util.Date;
 * 
 * import org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.MethodArgumentNotValidException; import
 * org.springframework.web.bind.annotation.ControllerAdvice; import
 * org.springframework.web.bind.annotation.ExceptionHandler; import
 * org.springframework.web.context.request.WebRequest;
 * 
 * @ControllerAdvice public class GlobalExceptionHandler {
 * 
 * // handle specific exceptions
 * 
 * @ExceptionHandler(ResourceNotFoundException.class) public ResponseEntity<?>
 * handlleResourceNotFoundException(ResourceNotFoundException exception,
 * WebRequest request) { ErrorDetails errorDetails = new ErrorDetails(new
 * Date(), exception.getMessage(), request.getDescription(false)); return new
 * ResponseEntity(errorDetails, HttpStatus.NOT_FOUND); }
 * 
 * // handle ApiNotFound exceptions
 * 
 * @ExceptionHandler(APIException.class) public ResponseEntity<?>
 * handleAPIException(APIException exception, WebRequest request) { ErrorDetails
 * errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
 * request.getDescription(false)); return new ResponseEntity(errorDetails,
 * HttpStatus.NOT_FOUND); }
 * 
 * // handle global exceptions
 * 
 * @ExceptionHandler(Exception.class) public ResponseEntity<?>
 * handlleGlobalException(Exception exception, WebRequest request) {
 * ErrorDetails errorDetails = new ErrorDetails(new Date(),
 * exception.getMessage(), request.getDescription(false)); return new
 * ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR); }
 * 
 * // handling custom validation errors
 * 
 * @ExceptionHandler(MethodArgumentNotValidException.class) public
 * ResponseEntity<?>
 * customValidationErrorHandliing(MethodArgumentNotValidException exception) {
 * ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Error",
 * exception.getBindingResult().getFieldError().getDefaultMessage()); return new
 * ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST); }
 * 
 * }
 */