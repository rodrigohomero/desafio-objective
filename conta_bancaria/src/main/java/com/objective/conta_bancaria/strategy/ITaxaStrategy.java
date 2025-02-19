package com.objective.conta_bancaria.strategy;

//Strategy Pattern para calcular taxas de transação
public interface ITaxaStrategy {
	
 double calcularTaxa(double valor);
 
}
