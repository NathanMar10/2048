package engine;
import java.io.*;
import gui.*;
import java.util.*;
import gui.GraphicsDriver;
import javafx.application.*;
import gui.*;
/*
 * The Engineer is a Runnable application that will simulate a game of 2048 
 * (preferably with a visual) and record its score for the Engine class to
 * take stock of and then modify the Neural Net accordingly.
 */
public class Engineer implements Runnable {

	Network prevNetwork, currNetwork;
	double runningScore;
	int gameCount;
	public ArrayList<Engine> engineList;
	EngineGraphic engineGraphic;


	public Engineer(EngineGraphic engineGraphic) {
		prevNetwork = null;
		currNetwork = null;
		try {
			prevNetwork = Network.loadNetwork("prevNetwork.bin");
			currNetwork = Network.loadNetwork("currNetwork.bin");
		} catch (Exception e) {
			System.err.println("Error Loading Networks");
			prevNetwork = new Network();
			currNetwork = new Network();
			try {
				Network.saveNetwork(prevNetwork, "prevNetwork.bin");
			} catch (Exception i) {
				System.err.println("Error Saving new prevNetwork");
			}
		}
		engineList = new ArrayList<Engine>();
		this.engineGraphic = engineGraphic;
	}
	
	public void run() {
		while (!threadsFinished()) {
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				
			}
			
			Platform.runLater(new GraphicUpdater(engineGraphic));
		}
		
		currNetwork.averageScore = runningScore/gameCount;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("pastScores.txt", true));
			writer.write(runningScore + "\n");
			writer.close();
			System.out.println("Average Score: " + currNetwork.averageScore + ", Score to Beat: " + prevNetwork.averageScore);
		} catch (Exception e) {
			System.err.println("Error Writing Score");
		}
		
		/*
		 * check which network to save
		 */
		/*
		 * If the curr averageScore is greater than the prev, make the curr
		 * network the new prev and shuffle it to make the new curr
		 */
		if (prevNetwork.averageScore < currNetwork.averageScore || prevNetwork == null) {
			try {
				System.out.println("Saving New Network");
				currNetwork.averageScore = runningScore / gameCount;
				Network.saveNetwork(currNetwork, "prevNetwork.bin");
				prevNetwork = Network.loadNetwork("prevNetwork.bin");
			} catch (Exception e) {
				System.err.println("Error Changing PrevNetwork");
			}

		}
		prevNetwork.shiftNetwork();
		try {
			Network.saveNetwork(prevNetwork, "currNetwork.bin");
		} catch (Exception e) {
			System.err.println("Error Saving currNetwork");
		}
		
		
		
		
		Platform.runLater(new GraphicRestarter(engineGraphic));
	}
	
	private boolean threadsFinished() {
		for (Engine engine : engineList) {
			if (!engine.finished) {
				return false;
			}
		}
		return true;
	}
}
