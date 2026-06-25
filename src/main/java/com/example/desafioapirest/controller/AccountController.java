package com.example.desafioapirest.controller;

import com.example.desafioapirest.dto.AccountRequest;
import com.example.desafioapirest.entity.Account;
import com.example.desafioapirest.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> criarConta(@Valid @RequestBody AccountRequest request) {

        Account contaCriada = accountService.createAccount(request);

        return ResponseEntity.status(201).body(contaCriada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> buscarContaPorId(@PathVariable Long id) {

        Account contaEncontrada = accountService.buscarConta(id);

        return ResponseEntity.ok(contaEncontrada);
    }


}
