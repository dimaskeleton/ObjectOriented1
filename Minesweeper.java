import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

//This our cell class which represents each of the cells in the game grid.
class Cell {
  boolean isMine; // if a cell is a mine
  boolean isRevealed; //cell that is revealed
  boolean isFlag; // cell that is flagged
  List<Cell> neighbors; // list of neighboring cells
  static final int CELL_SIZE = 20; // Size of each cell
  static final int WIDTH_OF_GRID = 2; // Width of the grid lines/squares
  //CONSTRUCTOR:
  Cell() {
    this.isMine = false;
    this.isRevealed = false;
    this.isFlag = false;
    this.neighbors = new ArrayList<>();
  }

    /* TEMPLATE
    Fields:
    ... this.isMine ...      -- boolean
    ... this.isRevealed ...  -- boolean
    ... this.isFlag ...      -- boolean
    ... this.neighbors ...   -- List<Cell>
    Methods:
    ... this.addNeighbor(Cell) ...           -- void
    ... this.countMines() ...                -- int
    ... this.getImage() ...                 -- WorldImage
    */

  // This is the method that links the cells together when the game is initialized so that every cell has a list of its neighbors
  void addNeighbor(Cell neighbor) { //type cell neighbor -> neighbor = list of cell objects
    neighbors.add(neighbor); // adding a neighbor to current list
  }
  // this  method counts how many nearby cells are mines.
  // It goes through its neighboring cells, adds 1 to the count when it finds a mine, and returns the total count of mines
  int countMines() {
    int mineCount = 0; //original mineCount is zero
    for (Cell neighbor : neighbors) {
      if (neighbor.isMine) {
        mineCount++; //goes up by 1 when finding a mine
      }
    }
    return mineCount;
  }
  //This method creates an image for a cell in our game. It checks if the cell is a mine, revealed, or flagged.
  // If it's a mine, it displays the letter "M" in red. If it's revealed, it shows the number of neighboring mines in blue numbers.
  // If it's neither a mine nor revealed, it displays a gray rectangle. If it's flagged, it adds a red flag to the cell.
  // Lastly, it adds a black border around the cell and returns the created image.
  public WorldImage getImage() {
    WorldImage baseImage;
    // if the cell is revealed :
    if (this.isRevealed) {
      if (this.isMine) {
        // Display red "M" for mines when revealed
        baseImage = new TextImage("M", 12, Color.RED);
      } else {
        // if not a mine, calculate the number of neighboring mines using countMines() method -> blue number that displays count
        int mineCount = countMines();
        baseImage = new TextImage(String.valueOf(mineCount), 12, Color.BLUE);
      }
    } else {
      // if cell is flagged, it overlayes a red flag on top of grey rectangle
      baseImage = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.GRAY);
      if (this.isFlag) {
        WorldImage flag = new TriangleImage(new Posn(0, 0), new Posn(CELL_SIZE / 2, CELL_SIZE), new Posn(0, CELL_SIZE), OutlineMode.SOLID, Color.RED);
        baseImage = new OverlayImage(flag, baseImage);
      }
    }
    // black border around the cells using overlay
    WorldImage borderImage = new RectangleImage(CELL_SIZE + WIDTH_OF_GRID, CELL_SIZE + WIDTH_OF_GRID, OutlineMode.OUTLINE, Color.BLACK);
    return new OverlayImage(baseImage, borderImage);
  }
}

//This is the main game class that sets the game grid we have, places the mines on the grid, and manages the game state
class MinesweeperGame extends World {
  ArrayList<ArrayList<Cell>> grid;
  int numMines; //store number of mines in our game
  boolean gameEnded = false; //boolean to indicate if game has ended (that's why its false)
  WorldImage endGameOverlay; // To store the image at the end game (overlay image)
  static final int WIDTH = 30;  // 30x16 size of grid
  static final int HEIGHT = 16; // 30x16 size of grid
  //CONSTRUCTOR:
  MinesweeperGame(int numMines) {
    this.numMines = numMines;
    initializeGrid(WIDTH, HEIGHT);
    placeMines(numMines);
  }
    /* TEMPLATE
    Fields:
    ... this.grid ...          -- ArrayList<ArrayList<Cell>>
    ... this.numMines ...      -- int
    ... this.gameEnded ...     -- boolean
    ... this.endGameOverlay ... -- WorldImage

    Methods:
    ... this.initializeGrid(int, int) ...         -- void
    ... this.assignNeighbors() ...                -- void
    ... this.addNeighborsToCell(int, int) ...     -- void
    ... this.isValidPosition(int, int) ...        -- boolean
    ... this.placeMines(int) ...                  -- void
    ... this.makeScene() ...                     -- WorldScene
    ... this.onMouseClicked(Posn, String) ...     -- void
    ... this.revealCell(Cell) ...                -- void
    ... this.switchFlag(Cell) ...                -- void
    ... this.checkWinCondition() ...             -- void
    ... this.endGame(boolean) ...                -- void
    ... this.revealAllMines() ...               -- void
    */

  // this method creates our grid of Cell objects with the width and height we gave it in the MinesweeperGame class
  void initializeGrid(int width, int height) {
    grid = new ArrayList<>(height);
    for (int row = 0; row < height; row++) {
      ArrayList<Cell> rowList = new ArrayList<>(width);
      for (int col = 0; col < width; col++) {
        rowList.add(new Cell());
      }
      grid.add(rowList);
    }
    assignNeighbors();
  }
  // this method goes through each cell in the game grid and connects each cell with its neighboring cells.
  // addNeighborsTo Cell method -> adds neighboring cells to the current cell's list of neighbors, this lets us know that  each cell knows which other cells are adjacent to it.
  void assignNeighbors() {
    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        addNeighborsToCell(row, col);
      }
    }
  }
  //helps our cells make friends with their neighboring cells.
  //This method goes through each cell in the group, and for each cell, it checks its  surrounding cells (up, down, left, right, and diagonally) to see if they exist.
  // If a neighboring cell is found, (meaning they're not in each other's neighbor list), our method introduces them as neighbors.
  // This way, each cell knows which other cells are close by and can interact with them during the game.
  void addNeighborsToCell(int row, int col) {
    Cell currentCell = grid.get(row).get(col);
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) continue; // Skip the cell itself
        int neighborRow = row + i;
        int neighborCol = col + j;
        if (isValidPosition(neighborRow, neighborCol)) {
          Cell neighborCell = grid.get(neighborRow).get(neighborCol);
          if (!currentCell.neighbors.contains(neighborCell)) {
            currentCell.addNeighbor(neighborCell);
          }
        }
      }
    }
  }

  // method is like a navigator for our game grid. It helps us check if a given row and column are within the boundaries of our grid.
  //It makes sure that we're not trying to access a spot that's outside the borders of our grid. If the row and column values are within the grid's limits, it tells us that it's a valid position, allowing us to proceed with our game.
  // Otherwise, it tells us that we're trying to go somewhere outside our map, and we should stay within the boundaries.
  boolean isValidPosition(int row, int col) {
    return row >= 0 && row < grid.size() && col >= 0 && col < grid.get(0).size();
  }

  // Using random to place mines
  // First, we create a list called positions to keep track of all the spots where we can hide mines.
  // This list represents all the possible hiding places on our game grid.
  void placeMines(int numMines) {
    ArrayList<int[]> positions = new ArrayList<>();
    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(0).size(); col++) {
        positions.add(new int[]{row, col});
      }
    }
    // Next, we want to shuffle this checklist so that we can randomly pick spots to hide the mines
    //  We use a random process/object for this. purpose: We swap items in our checklist randomly, ensuring that each spot has an equal chance of being picked.
    Random random = new Random();
    for (int i = 0; i < positions.size(); i++) {
      int swapWithIndex = random.nextInt(positions.size());
      int[] temp = positions.get(i);
      positions.set(i, positions.get(swapWithIndex));
      positions.set(swapWithIndex, temp);
    }
    //Finally, we go through our shuffled checklist and pick the first numMines spots. These are the spots where we'll hide the mines. We mark these spots on our game grid as having mines,
    // purpose:  ensures that our game has the right number of mines, and they are placed randomly.
    for (int i = 0; i < numMines; i++) {
      int[] position = positions.get(i);
      int row = position[0];
      int col = position[1];
      grid.get(row).get(col).isMine = true;
    }
  }
  // This method creates a scene for our game for our world
  // It goes through the cells, gets their images, and places them on the game scene
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();

    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        Cell cell = grid.get(row).get(col);
        scene.placeImageXY(cell.getImage(), col * Cell.CELL_SIZE + Cell.CELL_SIZE / 2, row * Cell.CELL_SIZE + Cell.CELL_SIZE / 2);
      }
    }

    if (gameEnded && endGameOverlay != null) {
      // Overlay the end game message if the game has ended
      scene.placeImageXY(endGameOverlay, WIDTH * Cell.CELL_SIZE / 2, HEIGHT * Cell.CELL_SIZE / 2);
    }

    return scene;
  }

  // This is the method to work our mouse clicks depending if it's the leftbutton or rightbutton
  // left mouse lick handling - reveal cells
  // right mouse click - flag/unflag cells
  public void onMouseClicked(Posn pos, String buttonName) {
    // We first need to check if the game has ended, and if it did, return the next steps
    if (gameEnded) {
      return;
    }

    int row = pos.y / Cell.CELL_SIZE;
    int col = pos.x / Cell.CELL_SIZE;

    if (isValidPosition(row, col)) {
      Cell clickedCell = grid.get(row).get(col);
      // If the left mouse button is clicked, it reveals the content of the clicked cell and checks if it contains a mine
      if (buttonName.equals("LeftButton")) {
        revealCell(clickedCell);
        if (clickedCell.isMine) {
          endGame(false); // Player clicked on a mine, so the game ends
        }
        //If the right mouse button is clicked, it switchs a flag on the clicked cell, allowing the player to mark or unmark cells.
      } else if (buttonName.equals("RightButton")) {
        switchFlag(clickedCell);
      }
    }
    // win condition to check if the player has completed/won the game
    checkWinCondition();
  }

  // this method reveals a single (or specific) cell for our game
  // We used depth-first search in our code to implement the flood-fill behavior (you told us to try this search technique)
  // purpose: to iterate over the neighbors of a cell, without having to worry about with their coordinates.
  public void revealCell(Cell cell) {
    //purpose:  only unrevealed and unflagged cells are processed.
    if (!cell.isRevealed && !cell.isFlag) {
      cell.isRevealed = true;
      if (cell.countMines() == 0 && !cell.isMine) {
        //// flood-fill implementation for cells with no adjacent mines -> depth first search
        //we are using recursion here, so we can go down each branch of the search tree
        for (Cell neighbor : cell.neighbors) {
          revealCell(neighbor);
        }
      }
    }
  }
  // this method allows the player to switch flags ON or OFF on a cell in the game grid
  // purpose: add or remove a flag (it's the top right key on the trackpad)
  public void switchFlag(Cell cell) {
    //only unrevealed and unflagged cells are considered here.
     if (!cell.isRevealed) {
      cell.isFlag = !cell.isFlag;
    }
  }
  // Purpose: check if the player has won the game
  // for-loop that goes through each arraylist with lists of cell objects
  public void checkWinCondition() {
    for (ArrayList<Cell> row : grid) {
      for (Cell cell : row) {
        if (!cell.isMine && !cell.isRevealed) {
          // check if cell is not a mine and if it has not been revealed
          return; // Game is still going on (cell not revealed by player)
        }
      }
    }
    endGame(true); // Player has won the game -> there are no  un-revealed cells left on game grid (all non-mine cells have been revealed)
  }

  // this is our end game method
  public void endGame(boolean hasWon) {
    gameEnded = true; // Set the game as ended -> true

    WorldImage endGameMessage;
    Color messageColor;
    // determines whether the player has won or lost the game based on this parameter.
    if (hasWon) {
      endGameMessage = new TextImage("You Win!", 36, FontStyle.BOLD, Color.BLACK);
      messageColor = new Color(0, 255, 0, 150); // Semi-transparent green
    } else {
      endGameMessage = new TextImage("Game Over", 36, FontStyle.BOLD, Color.BLACK);
      messageColor = new Color(255, 0, 0, 150); // Semi-transparent red
      revealAllMines(); // Reveal all mines if the player loses
    }

    int sceneWidth = WIDTH * Cell.CELL_SIZE;
    int sceneHeight = HEIGHT * Cell.CELL_SIZE;

    WorldImage overlay = new RectangleImage(sceneWidth, sceneHeight, OutlineMode.SOLID, messageColor);
    endGameOverlay = new OverlayImage(endGameMessage, overlay);

  }


  // Purpose: Reveal all mines if the player loses
  public void revealAllMines() {
    for (ArrayList<Cell> row : grid) {
      for (Cell cell : row) {
        if (cell.isMine) {
          cell.isRevealed = true;
        }
      }
    }
  }
}



//Our examples class and the Big Bang function to make our grid appear
class ExamplesMinesweeperGame {

  // Test methods for Cell class
  void testCellMethods(Tester t) {
    // Test addNeighbor method
    this.testAddNeighbor(t);
    // Test countMines method
    this.testCountMines(t);
  }

  // Test addNeighbor method
  void testAddNeighbor(Tester t) {
    Cell exampleCell = new Cell();
    Cell neighborCell = new Cell();
    exampleCell.addNeighbor(neighborCell);
    t.checkExpect(exampleCell.neighbors.size(), 1);
    t.checkExpect(exampleCell.neighbors.contains(neighborCell), true);
  }

  // Test countMines method
  void testCountMines(Tester t) {
    // Test with two mine neighbors
    Cell cell = new Cell();
    Cell mineNeighbor1 = new Cell();
    Cell mineNeighbor2 = new Cell();
    mineNeighbor1.isMine = true;
    mineNeighbor2.isMine = true;
    Cell nonMineNeighbor = new Cell();
    cell.addNeighbor(mineNeighbor1);
    cell.addNeighbor(mineNeighbor2);
    cell.addNeighbor(nonMineNeighbor);
    t.checkExpect(cell.countMines(), 2);

    Cell cell1 = new Cell();
    for (int i = 0; i < 3; i++) {
      cell1.addNeighbor(new Cell()); // non-mine neighbors
    }
    t.checkExpect(cell1.countMines(), 0);

    Cell cell2 = new Cell();
    for (int i = 0; i < 3; i++) {
      Cell mine = new Cell();
      mine.isMine = true;
      cell2.addNeighbor(mine);
    }
    t.checkExpect(cell2.countMines(), 3);

    Cell cell3 = new Cell();
    Cell mine1 = new Cell();
    Cell mine2 = new Cell();
    mine1.isMine = true;
    mine2.isMine = true;
    cell3.addNeighbor(mine1);
    cell3.addNeighbor(mine2);
    cell3.addNeighbor(new Cell());
    cell3.addNeighbor(new Cell());
    t.checkExpect(cell3.countMines(), 2);

    // Test with no neighbors
    Cell cell4 = new Cell();
    t.checkExpect(cell4.countMines(), 0);
  }

  // Test methods for MinesweeperGame class
  void testMinesweeperGameMethods(Tester t) {
    // Test for initializing the grid and assigning neighbors
    this.testGridAndNeighborsAssignment(t);
    // Test for valid and invalid positions
    this.testValidInvalidPositions(t);
    // Test for mine placement
    this.testPlaceMines(t);
  }

  // Test for setting the grid and assigning neighbors
  void testGridAndNeighborsAssignment(Tester t) {
    MinesweeperGame game = new MinesweeperGame(10);
    game.initializeGrid(3, 3); // Smaller grid for easier testing
    game.assignNeighbors();

    // Test for a corner cell
    Cell cornerCell = game.grid.get(0).get(0);
    t.checkExpect(cornerCell.neighbors.size(), 3);

    // Test for an edge cell
    Cell edgeCell = game.grid.get(0).get(1);
    t.checkExpect(edgeCell.neighbors.size(), 5);

    // Test for a middle cell
    Cell middleCell = game.grid.get(1).get(1);
    t.checkExpect(middleCell.neighbors.size(), 8);
  }

  // Test for valid and invalid positions
  void testValidInvalidPositions(Tester t) {
    MinesweeperGame exampleGame = new MinesweeperGame(10);
    t.checkExpect(exampleGame.isValidPosition(0, 0), true);
    t.checkExpect(exampleGame.isValidPosition(-1, -1), false);
  }

  // Test for mine placement
  void testPlaceMines(Tester t) {
    int mineCount = 10;
    MinesweeperGame game = new MinesweeperGame(mineCount);
    game.initializeGrid(5, 5); // Use a 5x5 grid for testing
    game.placeMines(mineCount);

    // Count the total number of mines placed
    int totalMines = countTotalMines(game);
    t.checkExpect(totalMines, mineCount);
  }

  // Helper method to count total mines in the game grid
  int countTotalMines(MinesweeperGame game) {
    int totalMines = 0;
    for (ArrayList<Cell> row : game.grid) {
      for (Cell cell : row) {
        if (cell.isMine) {
          totalMines++;
        }
      }
    }
    return totalMines;
  }

  void testRevealCell(Tester t) {
    MinesweeperGame game = new MinesweeperGame(10);
    game.initializeGrid(5, 5);
    game.placeMines(10);

    Cell cell = game.grid.get(0).get(0);
    game.revealCell(cell);

    t.checkExpect(cell.isRevealed, true);
  }

  // Test for the switchFlag method
  void testswitchFlag(Tester t) {
    MinesweeperGame game = new MinesweeperGame(10);
    game.initializeGrid(5, 5);
    game.placeMines(10);

    Cell cell = game.grid.get(0).get(0);
    game.switchFlag(cell); // Flag the cell
    t.checkExpect(cell.isFlag, true);

    game.switchFlag(cell); // Unflag the cell
    t.checkExpect(cell.isFlag, false);
  }

  // Test the checkWinCondition method
  void testCheckWinCondition(Tester t) {
    MinesweeperGame game = new MinesweeperGame(1);
    game.initializeGrid(2, 2);
    game.placeMines(1);

    // Manually reveal all non-mine cells
    for (ArrayList<Cell> row : game.grid) {
      for (Cell cell : row) {
        if (!cell.isMine) {
          cell.isRevealed = true;
        }
      }
    }

    game.checkWinCondition();
    t.checkExpect(game.gameEnded, true); // Assuming gameEnded is set to true when game is won
  }

  // BIG BANG FUNCTION TO MAKE THE GAME RUN/APPEAR
  void testGame(Tester t) {
    new MinesweeperGame(99).bigBang(Cell.CELL_SIZE * MinesweeperGame.WIDTH, Cell.CELL_SIZE * MinesweeperGame.HEIGHT);
  }
}