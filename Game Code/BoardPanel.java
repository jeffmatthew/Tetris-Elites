import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

//Responsible for displaying the game grid and handling things related to the game board
public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 5055679736784226108L;

	//Min & Max color component values for tiles. This is required to show both light and dark shading on our tiles.
	public static final int COLOR_MIN = 35;
	public static final int COLOR_MAX = 255 - COLOR_MIN;

	//The width of the border around the game board.
	private static final int BORDER_WIDTH = 5;

	//The number of columns on the board.
	public static final int COL_COUNT = 10;
		
	//The number of visible rows on the board.
	private static final int VISIBLE_ROW_COUNT = 20;
	
	//The number of rows that are hidden from view.
	private static final int HIDDEN_ROW_COUNT = 2;
	
	//The total number of rows that the board contains.
	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	
	//The number of pixels that a tile takes up.
	public static final int TILE_SIZE = 24;
	
	//The width of the shading on the tiles.
	public static final int SHADE_WIDTH = 4;
	
	//The central x and y coordinate on the game board.
	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		
	//The total width & height of the panel.
	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	
	//The larger font to display.
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

	//The smaller font to display.
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
	

	private Tetris tetris;
	
	//The tiles that make up the board.
	private TileType[][] tiles;
		
	//Creates a new GameBoard instance.
	public BoardPanel(Tetris tetris) {
		this.tetris = tetris;
		this.tiles = new TileType[ROW_COUNT][COL_COUNT];
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.BLACK);
	}
	
	//Resets the board and clears away any tiles.
	public void clear() {
		//Works by looping through every tile index and set its value to null to clear the board to reset.
		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				tiles[i][j] = null;
			}
		}
	}
	
	//Determines whether a piece can be placed at specific coordinates.
	public boolean isValidAndEmpty(TileType type, int x, int y, int rotation) {
				
		//Ensure the piece is in a valid column.
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}
		
		//Ensure the piece is in a valid row.
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}
		
		//Loops through every tile in the piece and see if it conflicts with an existing tile.
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	
	//Adds a piece to the game board.
	public void addPiece(TileType type, int x, int y, int rotation) {
		//Loop through every tile within the piece and add it to the board only if the boolean that represents that tile is set to true.
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation)) {
					setTile(col + x, row + y, type);
				}
			}
		}
	}
	
	//Checks the board to see if any lines have been cleared and removes them from the game.
	public int checkLines() {
		int completedLines = 0;
		
		//Loops through every line and check it to see if it's been cleared or not.
		//		 If it has, it increments the number of completed lines and checks the next row.
		//		 The checkLine function handles clearing the line and shifting the rest of the board down.
		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}
			
	//Checks whether the row is full.
	private boolean checkLine(int line) {
		/*
		 * Iterate through every column in this row.
		 * If any of them are empty, then the row is not full.
		 */
		for(int col = 0; col < COL_COUNT; col++) {
			if(!isOccupied(col, line)) {
				return false;
			}
		}
		
		//Remove the filled line from the game by shifting every row above it down
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTile(col, row + 1, getTile(col, row));
			}
		}
		return true;
	}
	
	
	//Checks to see if the tile is already occupied.
	private boolean isOccupied(int x, int y) {
		return tiles[y][x] != null;
	}
	
	//Sets a tile located at the desired column and row.
	private void setTile(int  x, int y, TileType type) {
		tiles[y][x] = type;
	}

	//Gets the tile by its column and row.
	private TileType getTile(int x, int y) {
		return tiles[y][x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Simplifying the positioning of things.
		g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		//Draw the board differently depending on the current game state.
		if(tetris.isPaused()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			String msg = "PAUSED";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
		} else if(tetris.isNewGame() || tetris.isGameOver()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);
			
			//Game over and New game screens
			String msg = tetris.isNewGame() ? "TETRIS" : "GAME OVER";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Press Enter to Play" + (tetris.isNewGame() ? "" : "Again");
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
		} else {
			
			//Draw the tiles onto the board.
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					TileType tile = getTile(x, y);
					if(tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			//Draw the current piece
			TileType type = tetris.getPieceType();
			int pieceCol = tetris.getPieceCol();
			int pieceRow = tetris.getPieceRow();
			int rotation = tetris.getPieceRotation();
			
			//Draw the piece onto the board.
			for(int col = 0; col < type.getDimension(); col++) {
				for(int row = 0; row < type.getDimension(); row++) {
					if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			
			//Draw the ghost piece (Transparent piece)
			Color base = type.getBaseColor();
			base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
			for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
				//If no collision is detected, try the next row.
				if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {					
					continue;
				}
				
				//Draw the ghost one row higher than the one the collision took place at.
				lowest--;
				
				//Draw the ghost piece.
				for(int col = 0; col < type.getDimension(); col++) {
					for(int row = 0; row < type.getDimension(); row++) {
						if(lowest + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}
				
				break;
			}
			
			//Draw the background grid above the pieces
			g.setColor(Color.DARK_GRAY);
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}
		
		//Draw outline
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
	}
	
	//Draws a tile onto the board.
	private void drawTile(TileType type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
	}
	
	/**
	 * Draws: a tile onto the board.
	 * base: The base color of tile.
	 * light: The light color of the tile.
	 * dark: The dark color of the tile.
	 * x: The column.
	 * y: The row.
	 * g: The graphics object.
	 */
	private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
		
		//Fill the entire tile with the base color.
		g.setColor(base);
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		
		//Fill the bottom and right edges of the tile with the dark shading color.
		g.setColor(dark);
		g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
		
		//Fill the top and left edges with the light shading
		g.setColor(light);
		for(int i = 0; i < SHADE_WIDTH; i++) {
			g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
		}
	}

}