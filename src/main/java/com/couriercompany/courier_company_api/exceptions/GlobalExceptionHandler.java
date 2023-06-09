package com.couriercompany.courier_company_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExists(InputMismatchException ne) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("The provided input does not match with this field");
        errorResponse.setStatus(HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(UserNotFoundException ne) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("User not found");
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExist(AlreadyExistsException ne) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("Already exists");
        errorResponse.setStatus(HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException ne) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("Request not found");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotAvailableException.class)
    public ResponseEntity<ErrorResponse> notAvailable(NotAvailableException ne) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("Not Available");
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> tokenNotFound(InvalidTokenException ne) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("Token not found");
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedUserException(UnauthorizedUserException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .debugMessage("User does not have the right access")
                .status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> invalidAttributes(InvalidAttributeException ie) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ie.getMessage());
        errorResponse.setDebugMessage("Attribute not valid or does not exist");
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValid(MethodArgumentNotValidException ie) {
        ErrorResponse errorResponse = new ErrorResponse();

        String errorMessage = Objects.requireNonNull(ie.getFieldError()).getDefaultMessage();
        errorResponse.setMessage(errorMessage);
        errorResponse.setDebugMessage("Invalid Input Filled in Field");
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> notAvailable(UnauthorizedException ne){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("Not Available");
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setDebugMessage(ex.getLocalizedMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<ErrorResponse> passwordMismatchException(PasswordMisMatchException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setDebugMessage("Invalid password");
        errorResponse.setStatus(HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmptyListException.class)
    protected ResponseEntity<ErrorResponse> emptyList(EmptyListException ne){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage(ne.getDebugMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsException(InvalidCredentialsException ne){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED);
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("");
       return  new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CannotBeEmptyException.class)
    public ResponseEntity<ErrorResponse> cannotBeEmptyException(CannotBeEmptyException ne){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NO_CONTENT);
        errorResponse.setMessage(ne.getMessage());
        errorResponse.setDebugMessage("");
        return  new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
    }

}

