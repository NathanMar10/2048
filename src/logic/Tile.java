package logic;

import java.util.*;


public class Tile {
	int value;
	
	Tile up, down, left, right;
	GameBoard gameBoard;
	final HashMap<String, Tile> slideMap;
	final int row, col;
	
	public Tile(int value, int row, int col, GameBoard gameBoard) {
		this.value = value;
		this.row = row;
		this.col = col;
		slideMap = new HashMap<String, Tile>();
		this.gameBoard = gameBoard;
	}
	public Tile(int row, int col, GameBoard gameBoard) {
		this(0, row, col, gameBoard);
	}
	
	public void initializeLinks(GameBoard board) {
		up = board.get(row - 1, col);
		down = board.get(row + 1, col);
		right = board.get(row, col + 1);
		left = board.get(row, col - 1);
		
		slideMap.put("up", down);
		slideMap.put("down", up);
		slideMap.put("left", right);
		slideMap.put("right", left);
	}
	
	public boolean slide(String direction) {
		Tile prev = slideMap.get(direction);
		boolean moved = false;
		if (prev == null) {
			return false;
		} else {
			if (value == 0 && prev.value != 0) {
				value = prev.value;
				prev.value = 0;
				moved = true;
			}
		}
		return prev.slide(direction) || moved;
	}
	
	public boolean combine(String direction, boolean prevCombined) {
		Tile prev = slideMap.get(direction);
		boolean moved = false;
		boolean combined = false;
		if (prev == null) {
			return false;
		} else {
			if (value != 0 && prev.value == value && !prevCombined) {
				value ++;
				prev.value = 0;
				moved = true;
				gameBoard.score += (int)Math.pow(2, value);
				
			}
		}
		return prev.combine(direction, combined) || moved;
	}

	@Override
	public boolean equals(Object obj) {
		 if (obj == null) {
	            return false;
	        }
	        if (obj.getClass() != this.getClass()) {
	            return false;
	        }
	        final Tile other = (Tile) obj;

	        if (this.value == other.value) {
	            return true;
	        }
	        return false;
	    }

	@Override
	public String toString() {
		return Integer.toString(this.value);
	}
	
	public int getValue() {
		return this.value;
	}
}
