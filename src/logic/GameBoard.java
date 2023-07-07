package logic;
import java.util.Random;

public class GameBoard {

	final int boardSize;
	final int lastEntry;
	private Tile[][] boardState;
	int score;
	static Random random;

	static {
		random = new Random();
	}
	public GameBoard(int boardSize) {
		this.boardSize = boardSize;
		lastEntry = boardSize - 1;
		boardState = new Tile[boardSize][boardSize];
		random = new Random(System.currentTimeMillis());
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				boardState[row][col] = new Tile(row, col, this);
			}
		}
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				boardState[row][col].initializeLinks(this);
			}
		}
	}
	public GameBoard() {
		this(4);
	}
	
	/**
	 * Slides the board in the given direction, combining pieces as it goes
	 * @param direction
	 * @return true if move succeeded
	 */
	public boolean slide(String direction) {
		boolean moved = false;
		boolean validKey = false;
		int row = -1, col = -1;
		for (int i = 0; i < 4; i++) {
			switch(direction) {
			case "UP": 
			case "up":
			case "W":
				row = 0; col = i; validKey = true; direction = "up"; break;
			case "DOWN": 
			case "down":
			case "S":
				row = 3; col = i; validKey = true; direction = "down"; break;
			case "LEFT":
			case "left":
			case"A":
				row = i; col = 0; validKey = true; direction = "left"; break;
			case "RIGHT": 
			case "right":
			case "D":
				row = i; col = 3; validKey = true; direction = "right"; break;
			}
			if (validKey) {
				moved = boardState[row][col].slide(direction) || moved;
				moved = boardState[row][col].slide(direction) || moved;
				moved = boardState[row][col].combine(direction, false) || moved;
				moved = boardState[row][col].slide(direction) || moved;
				moved = boardState[row][col].slide(direction) || moved;
			}
		}
		return moved;
	}
	


	public String toString() {
		StringBuffer building = new StringBuffer();
		for (Tile[] row : boardState) {
			for (Tile space : row) {
				int number = (int)Math.pow(2, space.value);
				building.append(number == 1 ? 0 : number);
				building.append(" ");
			}
			building.append("\n");
		}
		return building.toString();
	}
	
	public boolean isGameOver() {
		boolean fullBoard = true;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (boardState[i][j].value == 0) {
					fullBoard = false;
				}
			}
		}
		if (!fullBoard) {
			return false;
		} else {
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					if (i != 0) {
						if (boardState[i][j].equals(boardState[i - 1][j])) {
							return false;
						}
					}
					if (j != 0) {
						if (boardState[i][j].equals(boardState[i][j - 1])) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public Tile[][] getBoardState() {
		return boardState;
	}

	public int getScore() {
		return score;
	}
	// Method to generate more pieces on the game board
	public void generatePiece() {
		int value = 1;
		if (random.nextInt(10) == 7) {
			value = 2;
		}
		int openSpaces = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (boardState[i][j].value == 0) {
					openSpaces++;
				}
			}
		} 
		if(openSpaces == 0) {
			System.out.println("Game Over!");
			return;
		}
		int stopCounter = random.nextInt(openSpaces);
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (boardState[i][j].value == 0) {
					openSpaces--;
					if (openSpaces == stopCounter) {
						boardState[i][j].value = value;
					}
				}
			}
		} 
		
	}
	
	
	public Tile get(int row, int col) {
		if (row < 4 && row > -1 && col < 4 && col > -1) {
			return boardState[row][col];
		} else {
			return null;
		}
	}
	
	public double[] getBoardStateNetwork() {
		double[] values = new double[16];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				values[4*i + j] = boardState[i][j].value;
			}
		}
		
		return values;
	}

}
