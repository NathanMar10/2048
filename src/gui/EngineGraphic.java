package gui;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.stage.*;
import engine.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;


public class EngineGraphic extends Application{
	
	private int gameSize, sceneSize, gameNumber;
	private Image[] imageArr;
	private ImageView[][] imageBoard;
	private Label scoreLabel;
	private Engine[][] engineBoard;
	private Thread[][] threadBoard;
	private GridPane grid;
	private Stage gameStage;

	@Override
	public void start(Stage gameStage) {
		this.gameStage = gameStage;
		runGame();
	}
	
	void runGame() {

		imageArr = new Image[17];
		{
			for (int i = 0; i < 17; i++) {
				int tileNum;
				if (i == 0) {
					tileNum = 0;
				} else {
					tileNum = (int) Math.pow(2, i);
				}
				imageArr[i] = new Image("TileImages/Tile_" + tileNum + ".png");
			}
		}

		// Start Button handler, creates a new gameScene

		gameSize = 4;
		gameNumber = 7;
		sceneSize = 600;

		// Creates the gameBoards and Engines
		Engineer engineer = new Engineer(this);
		engineBoard = new Engine[gameNumber][gameNumber];
		threadBoard = new Thread[gameNumber][gameNumber];
		for (int row = 0; row < gameNumber; row++) {
			for (int col = 0; col < gameNumber; col++) {
				engineBoard[row][col] = new Engine(gameSize, engineer);
				threadBoard[row][col] = new Thread(engineBoard[row][col]);
				engineer.engineList.add(engineBoard[row][col]);
			}
		}

		// Building the main gridPane
		grid = new GridPane();
		grid.setMaxSize(gameNumber, gameNumber);
		grid.setMaxHeight(800);
		grid.setMaxWidth(850);
		grid.setHgap(7);
		grid.setVgap(7);
		grid.setAlignment(Pos.CENTER);

		// Building the Smaller gridPanes
		for (int row = 0; row < gameNumber; row++) {
			for (int col = 0; col < gameNumber; col++) {
				GridPane thisGrid = new GridPane();
				thisGrid.setMaxSize(gameSize, gameSize);
				thisGrid.setMaxHeight(100);
				thisGrid.setMaxWidth(100);
				thisGrid.setAlignment(Pos.CENTER);

				imageBoard = new ImageView[gameSize][gameSize];
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						imageBoard[i][j] = new ImageView("TileImages/Tile_0.png");
						imageBoard[i][j].setFitHeight(sceneSize / gameSize / gameNumber);
						imageBoard[i][j].setFitWidth(sceneSize / gameSize / gameNumber);

						thisGrid.add(imageBoard[i][j], i, j);
					}
				}
				grid.add(thisGrid, row, col);
			}
		}

		HBox infoBar = new HBox();
		{
			infoBar.setPadding(new Insets(20, 20, 100, 20));
		}
		{
			scoreLabel = new Label();
			scoreLabel.setMinHeight(15);
			scoreLabel.setMinWidth(40);
			scoreLabel.setFont(new Font(24));
			infoBar.getChildren().add(scoreLabel);
		}
		BorderPane view = new BorderPane();
		view.setCenter(grid);
		view.setBottom(infoBar);
		Scene gameScene = new Scene(view, sceneSize, sceneSize + 15);

		gameStage.setScene(gameScene);
		gameStage.setTitle("2048 - Can You Do it?");
		gameStage.setWidth(sceneSize + 10 * gameNumber + sceneSize / 50);
		gameStage.setHeight(sceneSize + 100 + sceneSize / 50);
		gameStage.show();

		gameBoardToImageBoard();

		for (Thread[] row : threadBoard) {
			for (Thread thread : row) {
				thread.start();
			}
		}

		Thread engineerThread = new Thread(engineer);
		engineerThread.start();
	}

	/**
	 * Transfers the data from the gameBoard to the imageBoard, where the imageBoard
	 * directly updates the gridPane (shared references)
	 */
	void gameBoardToImageBoard() {

		for (int i = 0; i < gameNumber; i++) {
			for (int j = 0; j < gameNumber; j++) {
				for (int row = 0; row < gameSize; row++) {
					for (int col = 0; col < gameSize; col++) {
						int tileIndex;
						int tileValue = engineBoard[i][j].gameBoard.get(col, row).getValue();

						// is (column, row) because gridPane has weird indexing
						if (tileValue == 0) {
							tileIndex = 0;
						} else {
							tileIndex = tileValue;
						}
						GridPane pane = ((GridPane) (grid.getChildren().get(i * gameNumber + j)));
						ImageView view = (ImageView) (pane.getChildren().get(col * gameSize + row));
						view.setImage(imageArr[tileIndex]);
					}

				}
			}

		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
