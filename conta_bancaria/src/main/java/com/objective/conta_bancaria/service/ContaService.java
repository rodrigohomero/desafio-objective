package com.objective.conta_bancaria.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.objective.conta_bancaria.model.Conta;
import com.objective.conta_bancaria.model.Transacao;
import com.objective.conta_bancaria.repository.ContaRepository;
import com.objective.conta_bancaria.strategy.CreditoStrategy;
import com.objective.conta_bancaria.strategy.DebitoStrategy;
import com.objective.conta_bancaria.strategy.PixStrategy;
import com.objective.conta_bancaria.strategy.ITaxaStrategy;

@Service
public class ContaService {

	@Autowired
	private ContaRepository contaRepository;

	private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

	public Conta criarConta(Conta conta) throws Exception {
		if (contaRepository.findByNumeroConta(conta.getNumeroConta()).isPresent()) {
			throw new Exception("Conta já existe");
		}
		return contaRepository.save(conta);
	}

	public Optional<Conta> buscarConta(int numeroConta) {
		return contaRepository.findByNumeroConta(numeroConta);
	}

	public Conta realizarTransacao(Transacao transacao) throws Exception {
	    Conta conta = contaRepository.findByNumeroConta(transacao.getNumeroConta())
	            .orElseThrow(() -> new Exception("Conta não encontrada"));

	    ITaxaStrategy taxaStrategy;

	    switch (transacao.getFormaPagamento()) {
	        case "D":
	            taxaStrategy = new DebitoStrategy();
	            break;
	        case "C":
	            taxaStrategy = new CreditoStrategy();
	            break;
	        case "P":
	            taxaStrategy = new PixStrategy();
	            break;
	        default:
	            throw new IllegalArgumentException("Forma de pagamento inválida: " + transacao.getFormaPagamento());
	    }

	    double taxa = taxaStrategy.calcularTaxa(transacao.getValor());
	    double valorTotal = transacao.getValor() + taxa;

	    if (conta.getSaldo() < valorTotal) {
	        logger.warn("Transação rejeitada por saldo insuficiente: Conta {}", transacao.getNumeroConta());
	        throw new Exception("Saldo insuficiente");
	    }

	    conta.setSaldo(conta.getSaldo() - valorTotal);

	    return contaRepository.save(conta);
	}

}
