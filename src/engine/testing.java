package engine;

import java.util.Arrays;

public class testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Network net = null;
		try {
			net = Network.loadNetwork("prevNetwork.bin");
		} catch (Exception e) {
			
		}
		net.averageScore = 2.0;
		try {
			Network.saveNetwork(net, "prevNetwork.bin");
		} catch (Exception e) {
			
		}
	}
}
