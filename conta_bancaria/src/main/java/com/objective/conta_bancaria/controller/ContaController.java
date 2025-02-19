package com.objective.conta_bancaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.objective.conta_bancaria.model.Conta;
import com.objective.conta_bancaria.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController {

	@Autowired
	private ContaService contaService;

	@PostMapping
	public ResponseEntity<?> criarConta(@RequestBody Conta conta) {
		try {
			return ResponseEntity.status(201).body(contaService.criarConta(conta));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<Conta> buscarConta(@RequestParam("numero_conta") int numeroConta) {
		return contaService.buscarConta(numeroConta).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

}
