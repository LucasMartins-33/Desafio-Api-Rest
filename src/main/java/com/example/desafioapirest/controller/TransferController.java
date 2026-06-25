package com.example.desafioapirest.controller;

import com.example.desafioapirest.dto.TransferRequest;
import com.example.desafioapirest.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController (TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<String> realizarTransferencia(@Valid @RequestBody TransferRequest request) {

        transferService.transfer(request);
        return ResponseEntity.ok("Transferência realizada com sucesso!");
    }
}
