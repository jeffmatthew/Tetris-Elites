import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

//responsible for handling much of the game logic and reading user input
public class Tetris extends JFrame {
	//The number of milliseconds per fram
	private static final long FRAME_TIME = 1000L / 50L;

	//The number of pieces that exist
	private static final int TYPE_COUNT = TileType.values().length;

	//The BoardPanel instance
	private BoardPanel board;

	//The SidePanel instance
	private SidePanel side;

	//Whether the game is paused or not
	private boolean isPaused;

	//Whether the game has been played or not yet.
	//This is set to true initially and then set to false when the game starts
	private boolean isNewGame;

	//Whether the game is over or not yet
	private boolean isGameOver;

	//The current level the player's on
	private int level;

	//The current score
	private int score;

	//	The random number generator.
//	This is used to spit out pieces randomly.
	private Random random;

	//The clock that handles the update logic
	private Clock logicTimer;

	//The current type of tile
	private TileType currentType;

	//The next type of tile
	private TileType nextType;

	//The current column of our tile
	private int currentCol;

	//The current row of our tile
	private int currentRow;

	//The current rotation of our tile
	private int currentRotation;

	//Ensures that a certain amount of time passes after a piece is spawned before it can be dropped
	private int dropCooldown;

	//The speed of the game
	private float gameSpeed;

	//Creates a new Tetris instance.
	//Sets up the window's properties and adds a controller listener
	private Tetris() {
		//Set the basic properties of the window
		super("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		//Initialize the BoardPanel and SidePanel instances
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);

		//Add the BoardPanel and SidePanel instances to the window
		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);

		//Adds a custom anonymous KeyListener to the frame
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				switch(e.getKeyCode()) {

					//Drop - When pressed, the program checks to see that the game is not paused and that there is no drop cooldown, then set the logic timer to run at a speed of 25 cycles per second
					case KeyEvent.VK_S:
						if(!isPaused && dropCooldown == 0) {
							logicTimer.setCyclesPerSecond(25.0f);
						}
						break;

					//Move Left - When pressed, the program checks to see that the game is not paused and that the position to the left of the current position is valid. If so, we decrement the current column by 1.
					case KeyEvent.VK_A:
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
							currentCol--;
						}
						break;

					//Move Right - When pressed, the program checks to see that the game is not paused and that the position to the right of the current position is valid. If so, we increment the current column by 1.
					case KeyEvent.VK_D:
						if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
							currentCol++;
						}
						break;

					//Rotate Anticlockwise - When pressed, the program checks to see that the game is not pausedand then attempt to rotate the piece anticlockwise
					// Because of the size and complexity of the rotation code, as well as it's similarity to clockwise rotation, the code for rotating the piece is handled in another method
					case KeyEvent.VK_Q:
						if(!isPaused) {
							rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
						}
						break;

					/*
					 * Rotate Clockwise - When pressed, check to see that the game is not paused and then attempt to rotate the piece clockwise.
					 * Because of the size and complexity of the rotation code, as well as it's similarity to anticlockwise rotation, the code for rotating the piece is handled in another method.
					 */
					case KeyEvent.VK_E:
						if(!isPaused) {
							rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
						}
						break;

					/*
					 * Pause Game - When pressed, check to see that the players are currently playing a game.
					 * If the players are, toggle the pause variable and update the logic timer to reflect this change, otherwise the game will execute a huge number of updates and essentially cause an instant game over when the players unpause if the players stay paused for more than a minute.
					 */
					case KeyEvent.VK_P:
						if(!isGameOver && !isNewGame) {
							isPaused = !isPaused;
							logicTimer.setPaused(isPaused);
						}
						break;

					//Start Game - When pressed, check to see that we're in either a game over or new game state.
					case KeyEvent.VK_ENTER:
						if(isGameOver || isNewGame) {
							resetGame();
						}
						break;

				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

				switch(e.getKeyCode()) {

					//Drop - When released, the speed of the logic time is set back to whatever the current game speed is and clear out any cycles that might still be elapsed.
					case KeyEvent.VK_S:
						logicTimer.setCyclesPerSecond(gameSpeed);
						logicTimer.reset();
						break;
				}

			}

		});

		//Resize the frame to hold the BoardPanel and SidePanel instances, center the window on the screen, and show it to the user.
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	//Starts the game and initializes game loop
	private void startGame() {
		//Initialize the random number generator, logic timer, and new game variables
		this.random = new Random();
		this.isNewGame = true;
		this.gameSpeed = 1.0f;

		//Setup the timer to keep the game from running before the user presses enter to start it
		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);

		while(true) {
			//Get the time that the frame started.
			long start = System.nanoTime();

			//Update the logic timer.
			logicTimer.update();

			//Update the game if a cycle has elapsed on the timer
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}

			//Decrement the drop cool down if necessary.
			if(dropCooldown > 0) {
				dropCooldown--;
			}

			//Display the window to the user.
			renderGame();

			//Sleep to cap the framerate.
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Updates the game and handles the bulk of it's logic
	private void updateGame() {
		//Check to see if the piece's position can move down to the next row
		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
			//Increment the current row if it's safe to do so.
			currentRow++;
		} else {
			board.addPiece(currentType, currentCol, currentRow, currentRotation);

			/*
			 * Check to see if adding the new piece resulted in any cleared lines.
			 * If there are cleared lines, increase the player's score. (Up to 4 lines can be cleared in a single go)
			 * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]).
			 */
			int cleared = board.checkLines();
			if(cleared > 0) {
				score += 50 << cleared;
			}

			//Increase the speed slightly for the next piece and update the game's timer
			gameSpeed += 0.035f;
			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();

			//Set the drop cooldown (0.5 second buffer)
			dropCooldown = 25;

			//Update the difficulty level
			level = (int)(gameSpeed * 1.70f);

			//Spawn a new piece
			spawnPiece();
		}
	}

	//Forces the BoardPanel and SidePanel to repaint
	private void renderGame() {
		board.repaint();
		side.repaint();
	}

	//Resets the game variables to their default values at the start of a new game
	private void resetGame() {
		this.level = 1;
		this.score = 0;
		this.gameSpeed = 1.0f;
		this.nextType = TileType.values()[random.nextInt(TYPE_COUNT)];
		this.isNewGame = false;
		this.isGameOver = false;
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}

	//Spawns a new piece and resets our piece's variables to their default values
	private void spawnPiece() {
		//Check what the last shape used was, and reset our position and rotation to their original settings. Then, choose the next shape to use.
		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = TileType.values()[random.nextInt(TYPE_COUNT)];

		//Checks if the spawn point is still valid
		//If it is invalid, pause the game and show the losing screen
		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			logicTimer.setPaused(true);
		}
	}


	//Attempts to set the rotation of the current piece to newRotation
	//newRotation: The rotation of the new piece
	private void rotatePiece(int newRotation) {
		//In certain situations, we may need to move pieces when rotating them to prevent them from going outside the board.
		//To handle this, we save temporary row and column values in case we need to shift the tile accordingly.
		int newColumn = currentCol;
		int newRow = currentRow;

		//Get the insets for each of the sides.
		//These are used to determine how many empty rows or columns there are on a given side.
		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);

		//Move the piece away from the left right edges so that the piece doesn't clip out of the map and automatically become invalid
		if(currentCol < -left) {
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}

		//Move the piece away from the top bottom edges so that the piece doesn't clip out of the map and automatically become invalid
		if(currentRow < -top) {
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}

		/*
		 * Check to see if the new position is acceptable.
		 * If it is, update the rotation and position of the piece.
		 */
		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}

	//Checks to see whether the game is paused
	public boolean isPaused() {
		return isPaused;
	}

	//Checks to see whether the game is over
	public boolean isGameOver() {
		return isGameOver;
	}

	//Checks to see whether the players are on a new game
	public boolean isNewGame() {
		return isNewGame;
	}

	//Gets the current score
	public int getScore() {
		return score;
	}

	//Gets the current level
	public int getLevel() {
		return level;
	}

	//Gets the current type of piece that is used currently
	public TileType getPieceType() {
		return currentType;
	}

	//Gets the next type of piece that is going to be used
	public TileType getNextPieceType() {
		return nextType;
	}

	//Gets the column of the current piece
	public int getPieceCol() {
		return currentCol;
	}

	//Gets the row of the current piece
	public int getPieceRow() {
		return currentRow;
	}

	//Gets the rotation of the current piece
	public int getPieceRotation() {
		return currentRotation;
	}

	//Entry-point of the game. Responsible for creating and starting a new game instance
	public static void main(String[] args) {
		Tetris tetris = new Tetris();
		tetris.startGame();
	}
}