package com.example.desafioapirest.service;

import com.example.desafioapirest.dto.AccountRequest;
import com.example.desafioapirest.entity.Account;
import com.example.desafioapirest.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(AccountRequest request) {

        Account novaConta = new Account();
        novaConta.setBalance(request.initialBalance());
        return repository.save(novaConta);
    }

    public Account buscarConta(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
    }
}
