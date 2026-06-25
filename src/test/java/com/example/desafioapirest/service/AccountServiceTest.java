package com.example.desafioapirest.service;

import com.example.desafioapirest.dto.AccountRequest;
import com.example.desafioapirest.entity.Account;
import com.example.desafioapirest.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService accountService;


    @Test
    public void deveCriarContaComSucesso() {

        // Arrange
        AccountRequest request = new AccountRequest(new BigDecimal("1000.00"));

        Account contaSimulada = new Account();
        contaSimulada.setId(1L);
        contaSimulada.setBalance(new BigDecimal("1000.00"));

        Mockito.when(repository.save(Mockito.any(Account.class))).thenReturn(contaSimulada);

        //  Act
        Account contaCriada = accountService.createAccount(request);

        // Assert
        Assertions.assertNotNull(contaCriada);

        Assertions.assertEquals(new BigDecimal("1000.00"), contaCriada.getBalance());

        Mockito.verify(repository, Mockito.times(1))
                .save(Mockito.any(Account.class));
    }

    @Test
    public void devebuscarContaPorIdComSucesso() {

        // Arrange
        //AccountRequest request = new AccountRequest(1L);
        Account contaSimulada = new Account();
        contaSimulada.setId(1L);
        contaSimulada.setBalance(new BigDecimal("1000.00"));

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(contaSimulada));

        // Act
        Account contaEncontrada = accountService.buscarConta(1L);

        // Assert
        Assertions.assertEquals(1L, contaEncontrada.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deveLancarExcecaoQuandoContaNaoEncontrada() {
        // Arrange
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act e Assert
        IllegalArgumentException erroCapturado = Assertions.assertThrows(
                IllegalArgumentException.class, () -> accountService.buscarConta(99L)
        );

        Assertions.assertEquals("Conta não encontrada", erroCapturado.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());

    }
}
