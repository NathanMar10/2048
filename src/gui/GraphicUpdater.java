package gui;

public class GraphicUpdater implements Runnable {

	EngineGraphic engineGraphic;
	
	public GraphicUpdater(EngineGraphic engineGraphic) {
		this.engineGraphic = engineGraphic;
	}
	
	public void run() {
		engineGraphic.gameBoardToImageBoard();
	}
}
