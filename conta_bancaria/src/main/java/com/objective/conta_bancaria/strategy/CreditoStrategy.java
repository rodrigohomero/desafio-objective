package com.objective.conta_bancaria.strategy;

public class CreditoStrategy implements ITaxaStrategy {
	
    public double calcularTaxa(double valor) {
        return valor * 0.05;
    }
}
