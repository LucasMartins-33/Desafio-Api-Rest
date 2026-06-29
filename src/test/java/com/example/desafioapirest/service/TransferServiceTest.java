package com.example.desafioapirest.service;

import com.example.desafioapirest.dto.TransferRequest;
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
public class TransferServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private TransferService transferService;

    @Test
    public void deveRealizarTransferenciacomSucesso() {

        // arrange
        Account contaOrigem = new Account();
        contaOrigem.setId(1L);
        contaOrigem.setBalance(new BigDecimal("500"));

        Account contaDestino = new Account();
        contaDestino.setId(2L);
        contaDestino.setBalance(new BigDecimal("500"));


        TransferRequest request = new TransferRequest
                (1L, 2L, new BigDecimal("100.00"));

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(contaDestino));

        // Act
        transferService.transfer(request);

        // assert
        Assertions.assertEquals(new BigDecimal("400.00"), contaOrigem.getBalance());
        Assertions.assertEquals(new BigDecimal("600.00"), contaDestino.getBalance());

        Mockito.verify(repository, Mockito.times(1)).save(contaOrigem);
        Mockito.verify(repository, Mockito.times(1)).save(contaDestino);

    }

    @Test
    public void deveLancarExcecaoQuandoSaldoInsuficiente() {

        // arrange
        Account contaOrigem = new Account();
        contaOrigem.setBalance(new BigDecimal("500"));
        contaOrigem.setId(1L);

        Account contaDestino = new Account();
        contaDestino.setId(2L);
        contaDestino.setBalance(new BigDecimal("500"));

        TransferRequest request = new TransferRequest
                (1L, 2L, new BigDecimal("700"));

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(contaDestino));

        //act e assert
        IllegalArgumentException erroCapturado = Assertions.assertThrows(
                IllegalArgumentException.class, () -> transferService.transfer(request)
        );

        Assertions.assertEquals("Saldo insuficiente na conta de origem!", erroCapturado.getMessage());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoTransferirParaMesmaConta() {
        // Arrange
        TransferRequest request = new TransferRequest(1L, 1L, new BigDecimal("100.00"));

        // Act & Assert
        IllegalArgumentException erroCapturado = Assertions.assertThrows(
                IllegalArgumentException.class, () -> transferService.transfer(request)
        );

        Assertions.assertEquals("Não é possível transferir para a mesma conta!", erroCapturado.getMessage());

        Mockito.verify(repository, Mockito.never()).findById(Mockito.anyLong());
    }
}
