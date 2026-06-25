package com.example.desafioapirest.controller;

import com.example.desafioapirest.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransferService transferService;

    @Test
    public void deveRealizarTransferenciaComSucesso() throws Exception {

        // arrange
        String mensagemJson = """
                {
                  "sourceAccountId": 1,
                  "targetAccountId": 2,
                  "amount": 100
                
                }
                """;

        // Act Assert
        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mensagemJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência realizada com sucesso!"));

        Mockito.verify(transferService, Mockito.times(1))
                                        .transfer(Mockito.any());

    }

    @Test
    public void deveRetornarErro400quandoTransferenciaFalhar() throws Exception {

        // arrange
        String mensagemJson = """
                {
                  "sourceAccountId": 1,
                  "targetAccountId": 2,
                  "amount": 100
                
                }
                """;
        Mockito.doThrow(new IllegalArgumentException("Saldo insuficiente na conta de origem!"))
                .when(transferService)
                .transfer(Mockito.any());

        // Act Assert
        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mensagemJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente na conta de origem!"));
    }


}
