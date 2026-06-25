package com.example.desafioapirest.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public record TransferRequest(Long sourceAccountId,
                              Long targetAccountId,
                              @Positive(message = "O valor da transferência deve ser maior que zero!")
                              BigDecimal amount) {

}
