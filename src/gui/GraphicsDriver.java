package gui;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.stage.*;
import logic.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;


public class GraphicsDriver extends Application{
	
	private int gameSize, sceneSize;
	private boolean completedMove;
	private Image[] imageArr;
	private ImageView[][] imageBoard;
	private GameBoard gameBoard;
	private Label scoreLabel;
	
	@Override
	public void start(Stage gameStage) {
		/* Building up the Starting Stage */
		Stage startStage = new Stage();
		BorderPane startPane = new BorderPane();
			// Text at the top of the screen
		Label welcome = new Label("Welcome to 2048");
		Label loserLabel = new Label("You Suck!");
		welcome.setFont(new Font("Segoe UI", 40));
		loserLabel.setFont(new Font("Segoe UI", 40));
		BorderPane.setAlignment(loserLabel, Pos.CENTER);
		BorderPane.setAlignment(welcome, Pos.CENTER);
			// Start Button
		Button startButton = new Button("Start");
		startButton.setFont(new Font("Segoe UI", 40));
		startButton.setMinWidth(400);
		startButton.setMinHeight(250);
			// Configuration Labels
		Label gameSizeLabel = new Label("Game Size");
		Label sceneSizeLabel = new Label("Screen Size");
			// Configuration Sliders
		Slider gameSizeSlider = new Slider();
		Slider sceneSizeSlider = new Slider();
		gameSizeSlider.setShowTickMarks(true);
		gameSizeSlider.setShowTickLabels(true);
		gameSizeSlider.setMin(2);
		gameSizeSlider.setMax(45);
		gameSizeSlider.setValue(4);
		gameSizeSlider.setMajorTickUnit(1);
		gameSizeSlider.setMinWidth(200);
		gameSizeSlider.setSnapToTicks(true);
		gameSizeSlider.setMinorTickCount(0);
		sceneSizeSlider.setShowTickMarks(true);
		sceneSizeSlider.setShowTickLabels(true);
		sceneSizeSlider.setMin(100);
		sceneSizeSlider.setMax(900);
		sceneSizeSlider.setValue(600);
		sceneSizeSlider.setMajorTickUnit(100);
		sceneSizeSlider.setMinWidth(200);
		sceneSizeSlider.setSnapToTicks(true);
			// Configuration Row Setup
		HBox config = new HBox(20);
		config.setAlignment(Pos.CENTER);
		config.setPadding(new Insets(10));
		config.getChildren().add(gameSizeLabel);
		config.getChildren().add(gameSizeSlider);
		config.getChildren().add(sceneSizeLabel);
		config.getChildren().add(sceneSizeSlider);
		BorderPane.setAlignment(config, Pos.CENTER);
			// Adding Elements to the startPane
		startPane.setTop(welcome);
		startPane.setBottom(config);
		startPane.setCenter(startButton);
			// Creating the Starting Scene
		Scene startScene = new Scene(startPane, 600, 400);
		startStage.setScene(startScene);
			// Showing the Starting Scene
		startStage.show();
		// Starting Scene has been Created

		imageArr = new Image[17];
		for (int i = 0; i < 17; i++) {
			int tileNum;
			if (i == 0) {
				tileNum = 0;
			} else {
				tileNum = (int)Math.pow(2, i);
			}
			imageArr[i] = new Image("TileImages/Tile_" + tileNum + ".png");
		}
		
		// Start Button handler, creates a new gameScene
		startButton.setOnAction(e -> {
				// Reads input from the sliders
			gameSize = (int) gameSizeSlider.getValue();
			sceneSize = (int) sceneSizeSlider.getValue();
				// Makes a new clear Game Board to play on
			gameBoard = new GameBoard(gameSize);

			// ImageView array of objects of the proper size of the gameboard
			imageBoard = new ImageView[gameSize][gameSize];
			for (int i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					imageBoard[i][j] = new ImageView("TileImages/Tile_0.png");
					imageBoard[i][j].setFitHeight(sceneSize / gameSize);
					imageBoard[i][j].setFitWidth(sceneSize / gameSize);
				}
			}
			
			// Building the gridPane to play the game on
			GridPane grid = new GridPane();
			grid.setMaxSize(gameSize, gameSize);
			grid.setMaxHeight(600);
			grid.setMaxWidth(600);
			grid.setAlignment(Pos.CENTER);

			for (int i = 0; i < gameSize; i++) {
				for (int j = 0; j < gameSize; j++) {
					grid.add(imageBoard[i][j], i, j);
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
			Scene gameScene = new Scene(view, sceneSize , sceneSize + 15);
			
			gameStage.setScene(gameScene);
			gameStage.setTitle("2048 - Can You Do it?");
			gameStage.setWidth(sceneSize + 40);
			gameStage.setHeight(sceneSize + 100);
			gameStage.show();
			startStage.hide();
			
			gameBoard.generatePiece();
			gameBoardToImageBoard();
			
			
			// Controls the game based on keyboard input
			gameScene.setOnKeyPressed(f -> {
				completedMove = gameBoard.slide(f.getCode().toString());

				if (completedMove) {
					gameBoard.generatePiece();
					completedMove = false;
				}
				gameBoardToImageBoard();

				if (gameBoard.isGameOver()) {
					startPane.setTop(loserLabel);
					startStage.show();

				}
			});
		});
	}
	
	/**
	 * Transfers the data from the gameBoard to the imageBoard, where the
	 * imageBoard directly updates the gridPane (shared references)
	 */
	private void gameBoardToImageBoard() {
		scoreLabel.setText("Score: " + gameBoard.getScore());
		for (int row = 0; row < gameSize; row++) {
			for (int col = 0; col < gameSize; col++) {
				int tileIndex;
				int tileValue = gameBoard.get(col, row).getValue();
				// is (column, row) because gridPane has weird indexing
				if (tileValue == 0) {
					tileIndex = 0;
				} else {
					tileIndex = tileValue;
				}
				imageBoard[row][col].setImage(imageArr[tileIndex]);
			}
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
