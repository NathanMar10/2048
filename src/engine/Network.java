package engine;
import java.util.*;
import java.io.*;
import logic.*;

/*
 * The network is the neural network. I'm not sure what all this will include,
 * but theres a chance it'll be sexy.
 */
public class Network implements Serializable {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Node> allNodes, layer0, layer1, layer2, layer3, layer4;
	HashMap<Integer, ArrayList<Node>> layerMap;
	double averageScore;

	static Random shuffleRandom;

	{
		shuffleRandom = new Random();
	}

	
	static Network loadNetwork(String filename) throws Exception {
		File file = new File(filename);
		
		if (!file.exists()) {
			throw new UnsupportedOperationException("Network File Not Found");
		} else {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
			Network network = (Network) input.readObject();
			input.close();
			return network;
		}
	}
	
	static void saveNetwork(Network network, String filename) throws Exception {
		File file = new File(filename);
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
		
		output.writeObject(network);
		output.close();
	}
	
	Network() {
		instantiateLayers();
		setParents();
		setChildren();
		generateWeights();
	}
	private void instantiateLayers() {
		layerMap = new HashMap<Integer, ArrayList<Node>>();
		allNodes = new ArrayList<Node>();
		layer0 = new ArrayList<Node>(); // Inputs
		layer1 = new ArrayList<Node>(); // Second Layer
		layer2 = new ArrayList<Node>(); // Third Layer
		layer3 = new ArrayList<Node>(); // Output Layer
		layerMap.put(0, layer0);
		layerMap.put(1, layer1);
		layerMap.put(2, layer2);
		layerMap.put(3, layer3);
		for (int j = 0; j < 32; j++) {
			layer0.add(new Node(0));
			allNodes.add((layer0).get(j));
		}
		for (int i = 1; i <= 2; i++) {
			ArrayList<Node> thisList = layerMap.get(i);
			for (int j = 0; j < 32; j++) {
				thisList.add(new Node(i));
				allNodes.add(thisList.get(j));
			}
		}
		for (int j = 0; j <= 4; j++) {
			layer3.add(new Node(3));
			allNodes.add(layer3.get(j));
		}
	}
	private void setParents() {
		for (int i = 1; i <= 3; i++) {
			ArrayList<Node> thisList = layerMap.get(i);
			ArrayList<Node> parentList = layerMap.get(i - 1);
			for (Node node : thisList) {
				for (Node parent : parentList) {
					node.parentList.add(parent);
				}
			}
		}
	}
	private void setChildren() {
		for (int i = 0; i <= 2; i++) {
			ArrayList<Node> thisList = layerMap.get(i);
			ArrayList<Node> childList = layerMap.get(i + 1);
			for (Node node : thisList) {
				for (Node child : childList) {
					node.childList.add(child);
				}
			}
		}
	}
	void generateWeights() {
		Random random = new Random(System.currentTimeMillis());
		for (int i = 1; i <= 3; i++) {
			ArrayList<Node> thisList = layerMap.get(i);
			for (Node node : thisList) {
				node.constant = random.nextDouble() * 2 - 1 ;
				for (Node parent : node.parentList) { 
					node.weightMap.put(parent, random.nextDouble() * 2 - 1);
				}
			}
		}
	}

	void setFirstLayer(double[] boardState) {
		for (int i = 0; i < 16; i++) {
			layer0.get(i).value = boardState[i];
		}
	}
		
	double[] updateNetwork() {
		for (Node node : allNodes) {
			node.updated = false;
		}
		double[] result = new double[4];
		for (int i = 0; i < 4; i++) {
			result[i] = layer3.get(i).updateValue();
		}
		return result;
	}
	
	void shiftNetwork() {
		ArrayList<Node> shuffledList = new ArrayList<Node>(allNodes);
		Collections.shuffle(shuffledList);
		Random random = shuffleRandom;

			for (Node node : shuffledList) {
				for (Node parent : node.parentList) {
					double weight = node.weightMap.get(parent);
					if (Math.abs(node.constant) < .05) { 
						if (random.nextInt(10) == 8) {
							node.constant = -node.constant;
						}
					}
					if (random.nextInt(100) == 1) {
						node.weightMap.put(parent, weight + (random.nextDouble() * 2 - 1));
					}
				}
				if (random.nextInt(100) == 15) {
					node.constant = node.constant * (random.nextDouble() * 2 - 1);
					if (Math.abs(node.constant) < .05) { 
						if (random.nextInt(10) == 8) {
							node.constant = -node.constant;
						}
					}
				}
			}
	
		}
	
}
