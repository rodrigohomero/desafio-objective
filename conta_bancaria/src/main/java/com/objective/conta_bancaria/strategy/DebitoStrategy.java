package com.objective.conta_bancaria.strategy;

public class DebitoStrategy implements ITaxaStrategy {
	
    public double calcularTaxa(double valor) {
        return valor * 0.03;
    }
}