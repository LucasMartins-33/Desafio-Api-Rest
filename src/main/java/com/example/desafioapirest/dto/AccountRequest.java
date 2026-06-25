package com.example.desafioapirest.dto;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AccountRequest(
        @PositiveOrZero
        BigDecimal initialBalance) {

}
