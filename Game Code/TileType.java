import java.awt.Color;

//Describes the properties of the various pieces that can be used in the game
public enum TileType {

	//Piece Type 1
	TypeI(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX), 4, 4, 1, new boolean[][] {
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false,
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
		}
	}),
	
	//Piece type 2
	TypeJ(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX), 3, 3, 2, new boolean[][] {
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true,
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false,
		}
	}),
	
	//Piece type 3
	TypeL(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true,
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false,
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false,
		}
	}),
	
	//Piece type 4
	TypeO(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 2, 2, 2, new boolean[][] {
		{
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		},
		{	
			true,	true,
			true,	true,
		},
		{
			true,	true,
			true,	true,
		}
	}),
	
	//Piece type 5
	TypeS(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true,
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false,
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	//Piece type 6
	TypeT(new Color(128, BoardPanel.COLOR_MIN, 128), 3, 3, 2, new boolean[][] {
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false,
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false,
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false,
		}
	}),
	
	//Piece type 7
	TypeZ(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false,
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false,
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true,
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false,
		}
	});
		
	//The base color of the tiles
	private Color baseColor;
	
	//The light shading color of the tiles
	private Color lightColor;
	
	//The dark shading color of the tiles
	private Color darkColor;
	
	//The column the tiles spawns in
	private int spawnCol;
	
	//The row the tiles spawns in
	private int spawnRow;
	
	//The dimensions of the array for the piece
	private int dimension;
	
	//The number of rows in the piece. (Only valid when rotation is 0 or 2)
	private int rows;
	
	//The number of columns in the piece. (Only valid when rotation is 0 or 2)
	private int cols;
	
	//The tiles for the piece.
	//Each piece has an array of tiles for each rotation
	private boolean[][] tiles;
	
	/**
	 * Creates a new TileType.
	 * color: The base color of the tile.
	 * dimension: The dimensions of the tiles array.
	 * cols: The number of columns.
	 * rows: The number of rows.
	 * tiles: The tiles.
	 */
	private TileType(Color color, int dimension, int cols, int rows, boolean[][] tiles) {
		this.baseColor = color;
		this.lightColor = color.brighter();
		this.darkColor = color.darker();
		this.dimension = dimension;
		this.tiles = tiles;
		this.cols = cols;
		this.rows = rows;
		
		this.spawnCol = 5 - (dimension >> 1);
		this.spawnRow = getTopInset(0);
	}
	
	//Gets the base color
	public Color getBaseColor() {
		return baseColor;
	}
	
	//Gets the light shading color
	public Color getLightColor() {
		return lightColor;
	}
	
	//Gets the dark shading color
	public Color getDarkColor() {
		return darkColor;
	}
	
	//Gets the dimension
	public int getDimension() {
		return dimension;
	}
	
	// Gets the spawn column
	public int getSpawnColumn() {
		return spawnCol;
	}
	
	//Gets the spawn row
	public int getSpawnRow() {
		return spawnRow;
	}
	
	//Gets the number of rows in this specific piece. (Only valid when rotation is 0 or 2)
	public int getRows() {
		return rows;
	}
	
	//Gets the number of columns in this specific piece. (Only valid when rotation is 0 or 2)
	public int getCols() {
		return cols;
	}
	
	/**
	 * Checks to see if the given coordinates and rotation contain a tile.
	 * x: The x coordinate of the tile.
	 * y: The y coordinate of the tile.
	 * rotation: The rotation to check in.
	 * return: Checks whether a tile resides there.
	 */
	public boolean isTile(int x, int y, int rotation) {
		return tiles[rotation][y * dimension + x];
	}
	
	/**
	 * rotation: The rotation.
	 * return: The left inset.
	 */
	public int getLeftInset(int rotation) {
		//Loop through from left to right until we find a tile then return the column
		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	/**
	 * rotation: The rotation.
	 * return: The right inset.
	 */
	public int getRightInset(int rotation) {
		//Loop through from right to left until a tile is found and then return the column
		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}

	public int getTopInset(int rotation) {
		//Loop through from top to bottom until a tile is found then return the row.
		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}
	public int getBottomInset(int rotation) {
		//Loop through from bottom to top until a tile is found then return the row
		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}

}