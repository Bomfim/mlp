package com.ufu.ic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron(42);
        multilayerPerceptron.layers.add(new Layer(4, null, multilayerPerceptron)); // Input layer não tem activation
        multilayerPerceptron.layers.add(new Layer(8, new Sigmoid(), multilayerPerceptron));
        multilayerPerceptron.layers.add(new Layer(3, new Sigmoid(), multilayerPerceptron));

        for (Layer l : multilayerPerceptron.layers) {
            System.out.println(l);
        }

        int irisElementos = 150;
        int categorias = 3;
        int variaveis = 4;
        int epochs = 1000;
        double learningRate = 0.01;

        double[][] iris = loadIris(irisElementos, categorias, variaveis);

        multilayerPerceptron.fit(iris, 120, epochs, learningRate);

        int erros = 0;
        int contagem = 0;
        for (int n = 120; n < irisElementos; n++) {
            double[] testes = new double[7];
            for (int i = 0; i < 7; i++) {
                testes[i] = iris[n][i];
            }
            double[] saidas = multilayerPerceptron.forwardPass(testes);
            System.out.println("Entrada: " + Arrays.toString(testes));
            System.out.println("Calculado: " + Arrays.toString(saidas));
            contagem++;
            boolean erro = false;
            for (int i = 0; i < 3; i++) {
                erro = Math.round(saidas[i]) != testes[i + 4];
            }
            if (erro) {
                erros++;
            }
        }

        System.out.println("Testes: " + contagem + " erros: " + erros + " acurácia: " + (100.0 - ((double) erros / (double) contagem) * 100) + "%");

    }

    private static double[][] loadIris(int elementos, int categorias, int variaveis) throws IOException {
        double[][] dataset = new double[elementos][variaveis + categorias];
        BufferedReader br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/resources/iris-dataset.data")));
        String linha;
        String[] linhas = new String[elementos];
        for (int x = 0; x < elementos; x++) {
            linha = br.readLine();
            if (linha.length() > 0) {
                linhas[x] = linha;
            }
        }
        List<String> registros = Arrays.asList(linhas);
        Collections.shuffle(registros);
        int reg = 0;
        for (String linha2 : registros) {
            String[] vetor1 = linha2.split(",");
            for (int v = 0; v < variaveis; v++) {
                dataset[reg][v] = Double.parseDouble(vetor1[v]);
            }
            // One hot encoding: 1 0 0 - "Iris-setosa" 0 1 0 - "Iris-versicolor" 0 0 1 - "Iris-virginica"
            if (vetor1[4].equals("Iris-setosa")) {
                dataset[reg][4] = 1.0;
                dataset[reg][5] = 0.0;
                dataset[reg][6] = 0.0;
            } else if (vetor1[4].equals("Iris-versicolor")) {
                dataset[reg][4] = 0.0;
                dataset[reg][5] = 1.0;
                dataset[reg][6] = 0.0;
            } else {
                dataset[reg][4] = 0.0;
                dataset[reg][5] = 0.0;
                dataset[reg][6] = 1.0;
            }
            reg++;
        }
        return dataset;
    }
}
