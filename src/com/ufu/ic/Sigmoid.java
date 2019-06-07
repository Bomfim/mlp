package com.ufu.ic;

public class Sigmoid extends Activation {

    @Override
    public double executar(double input) {
        return (1 / (1 + Math.pow(Math.E, (-1 * input))));
    }

    @Override
    public double calcularDerivada(double saida) {
        return saida * (1 - saida);
    }


}
