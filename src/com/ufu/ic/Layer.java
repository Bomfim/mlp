package com.ufu.ic;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    public Activation activation;
    public List<Node> nodes;
    public int number;
    Node bias;

    public Layer(int numNodes, Activation activation, MultilayerPerceptron multilayerPerceptron) {
        super();
        this.activation = activation;
        this.nodes = new ArrayList<Node>();
        this.bias = new Node();
        this.bias.sinapses = new ArrayList<Sinapse>();
        this.number = multilayerPerceptron.layers.size() + 1;
        this.bias.layerNumber = this.number;
        for (int x = 0; x < numNodes; x++) {
            Node node = new Node();
            node.sinapses = new ArrayList<Sinapse>();
            this.nodes.add(node);
            node.layerNumber = this.number;
            node.nodeNumber = x + 1;
        }
        // Criamos as sinapses da camada anterior, conectando esta camada à ela.
        if (multilayerPerceptron.layers.size() > 0) {
            // A input layer não tem camada anterior
            Layer previous = multilayerPerceptron.layers.get(multilayerPerceptron.layers.size() - 1); // Pega a última inserida
            for (Node nprev : previous.nodes) {
                for (Node natu : this.nodes) {
                    Sinapse sinapse = new Sinapse();
                    sinapse.finalNode = natu;
                    sinapse.weight = multilayerPerceptron.getRandom();
                    nprev.sinapses.add(sinapse);
                }
            }
            // Bias da camada anterior (um pouco de repetição, mas dá para entender bem)
            for (Node natu : this.nodes) {
                Sinapse sinapse = new Sinapse();
                sinapse.finalNode = natu;
                sinapse.weight = multilayerPerceptron.getRandom();
                previous.bias.sinapses.add(sinapse);
            }
        } else {
            multilayerPerceptron.firstLayer = this;
        }
        multilayerPerceptron.lastLayer = this;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Layer) obj).number == this.number;
    }

    @Override
    public String toString() {
        String saida = "\n[Layer. Number : " + this.number
                + "\nBias: "
                + this.bias
                + "\nnodes:\n"
                + this.nodes
                + "\n]";
        return saida;
    }
}
