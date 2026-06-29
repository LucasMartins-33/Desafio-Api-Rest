package com.example.desafioapirest.service;

import com.example.desafioapirest.dto.TransferRequest;
import com.example.desafioapirest.entity.Account;
import com.example.desafioapirest.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final AccountRepository repository;

    public TransferService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transfer(TransferRequest request) {

        if(request.sourceAccountId().equals(request.targetAccountId())) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta!");
        }

        Account contaOrigem = repository.findById(request.sourceAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta de origem não encontrada."));

        Account contaDestino = repository.findById(request.targetAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Conta do destino não encontrada"));

        if (contaOrigem.getBalance().compareTo(request.amount()) < 0){
            throw new IllegalArgumentException("Saldo insuficiente na conta de origem!");
        }

        // Faz a subtração do saldo
        BigDecimal novoSaldoOrigem = contaOrigem.getBalance().subtract(request.amount());
        contaOrigem.setBalance(novoSaldoOrigem);

        // Adicionando o valor na conta destino
        BigDecimal novoSaldoContaDestino = contaDestino.getBalance().add(request.amount());
        contaDestino.setBalance(novoSaldoContaDestino);

        // salvando os dados no banco de dados
        repository.save(contaOrigem);
        repository.save(contaDestino);
    }
}
