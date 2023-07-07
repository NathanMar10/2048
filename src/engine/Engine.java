package engine;
import logic.*;
import java.util.*;

/*
 * The Engine class will contain the public-facing methods for the AI system. As
 * of yet, all of the specifics are completely unknown. I'll have a bunch of
 * nodes that represent the values in each square and then see what kind of crap
 * works. 
 */


/*
 * profit????
 */
public class Engine implements Runnable {

	public GameBoard gameBoard;
	Network network;
	private Engineer engineer;
	boolean finished = false;
	
	public Engine(int gameSize, Engineer engineer) {
		gameBoard = new GameBoard(4);
		try {
			network = Network.loadNetwork("engine/currNetwork.bin");
		} catch (Exception e) {
			network = new Network();
		}
		network.shiftNetwork();
		this.engineer = engineer;
	}
	
	public void run() {
		int moveCount = 0;
		gameBoard.generatePiece();
		while(!gameBoard.isGameOver()){

			/*
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				
			}
			*/
			network.setFirstLayer(gameBoard.getBoardStateNetwork());
			double[] values = network.updateNetwork();	
			//System.out.println(Arrays.toString(values));
			TreeMap<Double, Integer> indexMap = new TreeMap<>();
			for (int i = 0; i < 4; i++) {
				indexMap.put(values[i], i);
			}

			moveCount++;
				String direction = null;
				switch (indexMap.get(indexMap.lastKey())) {
				case 0:
					direction = "up";
					break;
				case 1:
					direction = "down";
					break;
				case 2:
					direction = "left";
					break;
				case 3:
					direction = "right";
					break;
				}
				if (!gameBoard.slide(direction)) {
					break;
				}
				indexMap.pollLastEntry();
				moveCount++;
			
			gameBoard.generatePiece();
		}
		synchronized(engineer) {
			engineer.runningScore += moveCount;
			engineer.gameCount++;
		}
		finished = true;
	}
}
