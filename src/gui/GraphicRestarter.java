package gui;
import javafx.*;
import javafx.application.Platform;

public class GraphicRestarter implements Runnable {

	EngineGraphic engineGraphic;
	
	public GraphicRestarter(EngineGraphic engineGraphic) {
		this.engineGraphic = engineGraphic;
	}
	
	public void run() {
		engineGraphic.runGame();
	}
}
