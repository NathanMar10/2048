package logic;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		GameBoard board = new GameBoard();
		boolean continueGame = true;
		boolean completedMove = true;
		while(continueGame) {
			if(completedMove) {
				board.generatePiece();
				completedMove = false;
			}
			System.out.println(board);
			String input = scanner.next();
			if(input.equals("w")) {
				completedMove = board.slide("up");
			}
			if(input.equals("a")) {
				completedMove = board.slide("left");
			}
			if(input.equals("s")) {
				completedMove = board.slide("down");
			}
			if(input.equals("d")) {
				completedMove = board.slide("right");
			}
			if(input.equals("l")) {
				continueGame = false;
			}
		}
		scanner.close();
		
	}
}
