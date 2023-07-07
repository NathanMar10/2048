package engine;
import java.util.*;
import java.io.*;

/*
 * A node is a neuron in the neural network. I'm not positive what that means, 
 * but maybe i'll find out one day
 */

public class Node implements Serializable {
	
	private static final long serialVersionUID = 1L;

	ArrayList<Node> parentList, childList;
	
	HashMap<Node, Double> weightMap;
	double constant;
	
	private int layer;
	double value;
	boolean updated = false;
	
	Node(int layer) {
		this.layer = layer;
		parentList = new ArrayList<Node>();
		childList = new ArrayList<Node>();
		weightMap = new HashMap<Node, Double>();
	}
	
	double updateValue() {
		if (this.layer == 0) {
			return value;
		}
		updated = true;
		double runningTotal = constant;
		for (Node node : parentList) {
			if (!node.updated) { 
				node.updateValue();
			} 
			runningTotal += 1/(1 + Math.exp(node.value * weightMap.get(node)));
		}
		value = runningTotal;
		return runningTotal;
	}
	
}
