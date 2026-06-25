package com.example.desafioapirest.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErroTransferencia(IllegalArgumentException e) {

        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> tratamentoErroTransferenciaNulasNegativas(MethodArgumentNotValidException e) {

        String mensagemDeErro = e.getFieldError().getDefaultMessage();

        return ResponseEntity.badRequest().body(mensagemDeErro);
    }
}
