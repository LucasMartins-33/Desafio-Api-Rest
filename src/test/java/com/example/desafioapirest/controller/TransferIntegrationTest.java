package com.example.desafioapirest.controller;

import com.example.desafioapirest.dto.TransferRequest;
import com.example.desafioapirest.entity.Account;
import com.example.desafioapirest.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    public void deveRealizarTransferenciaComSucessoEAtualizarOBanco() throws Exception {

        // Arrange

        Account origem = new Account();
        origem.setBalance(new BigDecimal("500.00"));
        origem = repository.save(origem);

        Account destino = new Account();
        destino.setBalance(new BigDecimal("500.00"));
        destino = repository.save(destino);

        TransferRequest request = new TransferRequest(origem.getId(), destino.getId(), new BigDecimal("100.00"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        // Act

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());

        // Assert
        Account origemAtualizada = repository.findById(origem.getId()).orElseThrow();
        Account destinoAtualizada = repository.findById(destino.getId()).orElseThrow();

        Assertions.assertEquals(new BigDecimal("400.00"), origemAtualizada.getBalance());
        Assertions.assertEquals(new BigDecimal("600.00"), destinoAtualizada.getBalance());
    }
}
