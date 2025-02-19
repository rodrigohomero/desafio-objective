package com.objective.conta_bancaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.objective.conta_bancaria.model.Transacao;
import com.objective.conta_bancaria.service.ContaService;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
	
	@Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<?> realizarTransacao(@RequestBody Transacao transacao) {
        try {
            return ResponseEntity.status(201).body(contaService.realizarTransacao(transacao));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
	

}
