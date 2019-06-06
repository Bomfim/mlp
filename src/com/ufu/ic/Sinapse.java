package com.facom;

public class Sinapse {

    public Node finalNode;
    public double weight;
    public double gradient;

    @Override
    public String toString() {
        String saida = "\n\t[Sinapse. Final node number: "
                + finalNode.layerNumber + "/" + finalNode.nodeNumber
                + ", weight: "
                + this.weight
                + "]";
        return saida;
    }

    @Override
    public boolean equals(Object obj) {
        Sinapse sinapse = (Sinapse) obj;
        Node outro = sinapse.finalNode;
        Node node = this.finalNode;
        return node.layerNumber == outro.layerNumber
                && node.nodeNumber == outro.nodeNumber;
    }
}
