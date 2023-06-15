# Tetris Elites - Jeff Matthew(2602118906)

Welcome to Tetris Elites! This classic puzzle game will test your strategic thinking, quick reflexes, and ability to stack falling shapes. Read this README to familiarize yourself with the game and its features.

**Objective:**

Arrange the falling tetromino shapes at the bottom of the grid (playfield) to form complete rows without any gaps. When a row is filled, it will disappear, and you'll earn points. The game continues until the stack of shapes reaches the top of the playfield.

**Gameplay**

Tetromino shapes, consisting of four squares, appear at the top of the playfield and start to descend towards the bottom.
You can move the falling shape left or right using the arrow keys to position it correctly.
Use the rotation control to adjust the orientation of the shape to fit the available spaces.
Once the shape reaches the bottom or lands on top of existing blocks, it will freeze in place, and a new shape will appear.
When a row is completely filled with shapes, it will clear, and any blocks above it will shift down to occupy the vacated space.

**Controls:**

1. **''A''** - Move Left
2. **''D''** - Move Right
3. **''Q''** - Rotate Anticlockwise
4. **''E''** - Rotate Clockwise
5. **''S''** - Drop
6. **''P''** - Pause / Resume
7. **''ENTER''** - Start

**Logic**

  The game logic is handled in the Tetris class, which includes methods for starting the game, updating the game state, and handling user input. The game consists of a game board where Tetris pieces (tiles) fall from the top, and the player's goal is to manipulate the pieces to create complete horizontal lines.
  The code includes variables and methods for managing the game state, such as the current level, score, and the current and next types of Tetris pieces. It also handles user input for moving and rotating the pieces, pausing the game, and starting a new game. The game speed increases as the player progresses, and cleared lines contribute to the player's score.
  The code uses various Java libraries, such as java.awt, javax.swing, and java.util, for graphical components, event handling, random number generation, and timing. Overall, it provides the foundation for running and managing the Tetris game.
 
![Screenshot 2023-06-15 212524](https://github.com/jeffmatthew/Tetris-Elites/assets/114067378/986950f6-6f9b-4de0-93d1-b801f6fb5f79)

**Have Fun and Enjoy!**
