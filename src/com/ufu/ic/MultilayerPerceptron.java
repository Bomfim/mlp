package com.ufu.ic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultilayerPerceptron {
    public List<Layer> layers;
    public double lossValue;
    Random random;
    Layer firstLayer;
    Layer lastLayer;

    public double[] forwardPass(double[] input) {
        double[] outputValues = new double[layers.get(layers.size() - 1).nodes.size()];
        for (Layer layer : this.layers) {
            if (layer.equals(this.firstLayer)) {
                int i = 0;
                for (Node node : layer.nodes) {
                    node.input = input[i];
                    node.value = input[i];
                    i++;
                }
            } else {
                for (Node node : layer.nodes) {
                    double finalValue = 0.0;
                    int idx = this.layers.indexOf(layer);
                    idx--;
                    Layer previous = this.layers.get(idx);
                    for (Node nprev : previous.nodes) {
                        Sinapse sinapse = this.getSinapse(nprev, node);
                        finalValue += (nprev.value * sinapse.weight);
                    }
                    // Bias anterior:
                    Sinapse sinapse = this.getSinapse(previous.bias, node);
                    finalValue += (sinapse.weight);
                    node.input = finalValue;
                    node.value = layer.activation.executar(node.input);
                }
            }
        }
        for (int x = 0; x < this.lastLayer.nodes.size(); x++) {
            outputValues[x] = this.lastLayer.nodes.get(x).value;
        }
        return outputValues;
    }

    private Sinapse getSinapse(Node origem, Node destino) {
        Sinapse sinapse = new Sinapse();
        sinapse.finalNode = destino;
        int indexSin = origem.sinapses.indexOf(sinapse);
        sinapse = origem.sinapses.get(indexSin);
        return sinapse;
    }


    public void backPropagation(double[] target, double learningRate) {
        // Começamos pela penúltima camada:
        int indiceUltima = this.layers.indexOf(this.lastLayer);
        Layer ultima = this.layers.get(indiceUltima);
        int qtdSaida = ultima.nodes.size();
        double[] outputErrors = new double[qtdSaida];
        double[] outputs = new double[qtdSaida];
        for (int x = 0; x < qtdSaida; x++) {
            Node node = ultima.nodes.get(x);
            outputErrors[x] = node.value - target[x];
            outputs[x] = node.value;
        }
        for (int l = (indiceUltima - 1); l >= 0; l--) {
            Layer layer = this.layers.get(l);
            Layer proxima = this.layers.get(l + 1);
            for (Node node : layer.nodes) {
                if (l == (indiceUltima - 1)) {
                    for (Sinapse sinapse : node.sinapses) {
                        double erro = outputErrors[sinapse.finalNode.nodeNumber - 1];
                        sinapse.gradient = erro * proxima.activation.calcularDerivada(sinapse.finalNode.value) * node.value;
                    }
                } else {
                    // Da antepenúltima para trás, somamos os deltas z
                    for (Sinapse sinapse : node.sinapses) {
                        double valorFinal = 0.0;
                        for (Sinapse s2 : sinapse.finalNode.sinapses) {
                            double deltaz = outputErrors[s2.finalNode.nodeNumber - 1] * outputs[s2.finalNode.nodeNumber - 1] * (1 - outputs[s2.finalNode.nodeNumber - 1]);
                            valorFinal += (deltaz * s2.weight);
                        }
                        sinapse.gradient = valorFinal * proxima.activation.calcularDerivada(sinapse.finalNode.value) * node.value;
                    }
                }
            }
            // Peso do Bias
            if (l == (indiceUltima - 1)) {
                for (Sinapse sinapse : layer.bias.sinapses) {
                    double erro = sinapse.finalNode.value - target[sinapse.finalNode.nodeNumber - 1];
                    sinapse.gradient = erro * layer.activation.calcularDerivada(sinapse.finalNode.value);
                }
            } else {
                for (Sinapse sinapse : layer.bias.sinapses) {
                    double valorFinal = 0.0;
                    for (Sinapse s2 : sinapse.finalNode.sinapses) {
                        double deltaz = outputErrors[s2.finalNode.nodeNumber - 1] * outputs[s2.finalNode.nodeNumber - 1] * (1 - outputs[s2.finalNode.nodeNumber - 1]);
                        valorFinal += (deltaz * s2.weight);
                    }
                    sinapse.gradient = valorFinal * proxima.activation.calcularDerivada(sinapse.finalNode.value);
                }
            }
        }
        // Atualização dos pesos
        for (int la = 0; la < this.layers.size() - 1; la++) {
            Layer layer = this.layers.get(la);
            for (Node node : layer.nodes) {
                for (Sinapse sinapse : node.sinapses) {
                    sinapse.weight = sinapse.weight - learningRate * sinapse.gradient;
                }
            }
        }
    }

    public void fit(double[][] dataset, int trainCount, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            double MSE = 0.0;
            for (int n = 0; n < trainCount; n++) {
                double[] outputs = this.forwardPass(dataset[n]);
                for (int z = 0; z < outputs.length; z++) {
                    MSE += Math.pow((dataset[n][4 + z] - outputs[z]), 2);
                }
                this.backPropagation(getTargets(dataset[n]), learningRate); // Gradient Descent
            }
            MSE = MSE / trainCount;
            System.out.println("Epoch: " + epoch + " MSE: " + MSE);
        }
    }

    private double[] getTargets(double[] ds) {
        double[] saida = new double[3];
        saida[0] = ds[4];
        saida[1] = ds[5];
        saida[2] = ds[6];
        return saida;
    }

    public MultilayerPerceptron() {
        super();
        this.layers = new ArrayList<Layer>();
        this.random = new Random();
    }

    public MultilayerPerceptron(int seed) {
        this();
        this.random = new Random(seed);
    }

    public double calcSquaredErrors(double[] target, double[] estimated) {
        double retorno = 0.0;
        for (int y = 0; y < target.length; y++) {
            retorno += Math.pow((target[y] - estimated[y]), 2);
        }
        return retorno;
    }

    double getRandom() {
        return -1.0 + (1.0 - (-1.0)) * this.random.nextDouble();
    }
}
