package com.facom;

import java.util.List;

public class Node {

    public int layerNumber;
    public int nodeNumber;
    public List<Sinapse> sinapses;
    public double input;  // net value before activation
    public double value;  // output or current value

    @Override
    public boolean equals(Object obj) {
        Node other = (Node) obj;
        return other.input == this.input && other.layerNumber == this.layerNumber;
    }

    @Override
    public String toString() {
        String saida = "\n\t[Node. Layer: "
                + this.layerNumber
                + ", node: "
                + this.nodeNumber
                + ", input: "
                + this.input
                + ", value: "
                + this.value
                + " sinapses: "
                + sinapses
                + "]";
        return saida;
    }
}
