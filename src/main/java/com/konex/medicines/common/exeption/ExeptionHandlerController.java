package com.konex.medicines.common.exeption;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.konex.medicines.common.dto.ResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestControllerAdvice
public class ExeptionHandlerController {
  
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handlerValidateExept(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<String, String>();
    ex.getBindingResult().getAllErrors().forEach((err) -> {
      String filedName = ((FieldError) err).getField();
      String message = err.getDefaultMessage();
      errors.put(filedName, message);
    });
    return errors;
  }

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseDto requestNotValidExept(
    HttpServletRequest req,
    HttpMessageNotReadableException ex) {
    return new ResponseDto("Body not sent", null);
  }
}
