package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import cs3500.reversi.controller.ModelCallbackListener;

/**
 * Represents the model for a 2-player game of Reversi with a regular hexagonal grid
 * of variable size.
 */
public class ReversiModel implements MutableReversiModel {
  // How many cells are in each edge of the hexagonal grid.
  private final int edgeLength;

  // The regular hexagonal grid of cells.
  // The grid is a list of rows (0-indexed from the top) where each row is a
  // list of colors (0-indexed from the left).
  // The grid is a List<List<Color>> because it seems as though the only pieces
  // of information from a cell that matter are whether it has a disc placed on it
  // and whether the disc placed on it is black or white. The enum Color represents
  // all three of those options. The grid is a coordinate system of rows and columns,
  // so it makes the most sense to represent it as a 2D list.
  private final List<List<DiscColor>> grid;

  // The state of the game (one of BLACKMOVE, WHITEMOVE, or GAMEOVER).
  private GameState gameState;

  // A counter that increases by one each time a player passes and resets each time
  // a player places a disc.
  // Class Invariant: passCounter is never less than 0.
  private int passCounter;

  // The object that broadcasts to its listeners when the model is mutated.
  private final ModelCallback broadcaster;

  /**
   * Constructs a ReversiModel with a variable edge length.
   * @param edgeLength the number of cells per edge of the grid
   * @throws IllegalArgumentException if edgeLength is less than 2
   */
  private ReversiModel(int edgeLength) throws IllegalArgumentException {
    if (edgeLength < 2) {
      throw new IllegalArgumentException("Invalid edge length");
    }
    this.edgeLength = edgeLength;
    this.grid = new ArrayList<>();
    this.passCounter = 0;
    this.gameState = GameState.UNSTARTED;
    this.broadcaster = new ModelMutatedBroadcaster();
    setupGame();
  }

  /**
   * Constructs a ReversiModel with all the fields specified.
   * @param edgeLength the number of cells per edge of the grid
   * @param grid the game board
   * @param gameState the state of the game
   * @param passCounter the number of passes in a row
   */
  private ReversiModel(int edgeLength,
                       List<List<DiscColor>> grid,
                       GameState gameState,
                       int passCounter) {
    this.edgeLength = edgeLength;
    this.grid = grid;
    this.gameState = gameState;
    this.passCounter = passCounter;
    this.broadcaster = new ModelMutatedBroadcaster();
  }

  /**
   * Creates a ReversiModel with a variable edge length.
   * @param edgeLength the number of cells per edge of the grid
   * @return a new ReversiModel with the specified edge length
   * @throws IllegalArgumentException if edgeLength is less than 2
   */
  public static ReversiModel create(int edgeLength) throws IllegalArgumentException {
    return new ReversiModel(edgeLength);
  }

  /**
   * Creates a copy of a Reversi game.
   * @param model a ROReversiModel to be copied
   * @return a new ReversiModel with all the fields of the given model
   */
  public static ReversiModel createCopy(ROReversiModel model) {
    List<List<DiscColor>> gridCopy = new ArrayList<>();
    for (List<DiscColor> row : model.getGrid()) {
      gridCopy.add(new ArrayList<>(row));
    }
    return new ReversiModel(
            model.getEdgeLength(),
            gridCopy,
            model.getGameState(),
            model.getPassCounter());
  }

  @Override
  public void setBroadcastListener(ModelCallbackListener controller) {
    this.broadcaster.addListener(controller);
  }

  @Override
  public void removeBroadcastListener(ModelCallbackListener controller) {
    this.broadcaster.removeListener(controller);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ReversiModel)) {
      return false;
    }
    ReversiModel that = (ReversiModel) other;
    return this.grid.equals(that.grid)
            && this.gameState == that.gameState
            && this.passCounter == that.passCounter;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.edgeLength, this.grid, this.gameState, this.passCounter);
  }

  /**
   * Sets up the game by setting up the players, the board, and the starting pieces.
   */
  private void setupGame() {
    // set the grid
    setupGrid();

    // place the starting pieces
    placeStartingPieces();
  }

  /**
   * Sets up the grid that the game will be played on.
   */
  private void setupGrid() {
    for (int row = 0; row < this.edgeLength * 2 - 1; row++) {
      int rowLength = ((2 * edgeLength) - 1) - Math.abs(row - (this.edgeLength - 1));
      List<DiscColor> gridRow = new ArrayList<>();
      for (int col = 0; col < rowLength; col++) {
        gridRow.add(DiscColor.NONE);
      }
      this.grid.add(gridRow);
    }
  }

  /**
   * Places the starting pieces onto the grid.
   */
  private void placeStartingPieces() {
    // Getting the coordinate of the center cell
    RowColCoords gridCenter = new RowColCoords(
            this.edgeLength - 1,
            this.edgeLength - 1);

    // Getting all the coordinates adjacent to the center
    RowColCoords topLeft = new RowColCoords(
            gridCenter.getRow() - 1, gridCenter.getCol() - 1);
    RowColCoords topRight = new RowColCoords(
            gridCenter.getRow() - 1, gridCenter.getCol());
    RowColCoords centerLeft = new RowColCoords(
            gridCenter.getRow(), gridCenter.getCol() - 1);
    RowColCoords centerRight = new RowColCoords(
            gridCenter.getRow(), gridCenter.getCol() + 1);
    RowColCoords bottomLeft = new RowColCoords(
            gridCenter.getRow() + 1, gridCenter.getCol() - 1);
    RowColCoords bottomRight = new RowColCoords(
            gridCenter.getRow() + 1, gridCenter.getCol());

    // Placing the starting discs on adjacent cells
    placeDisc(topLeft, DiscColor.BLACK);
    placeDisc(topRight, DiscColor.WHITE);
    placeDisc(centerLeft, DiscColor.WHITE);
    placeDisc(centerRight, DiscColor.BLACK);
    placeDisc(bottomLeft, DiscColor.BLACK);
    placeDisc(bottomRight, DiscColor.WHITE);
  }

  @Override
  public void startGame() throws IllegalStateException {
    throwIfGameStarted();
    nextPlayerTurn();
  }

  @Override
  public void pass() throws IllegalStateException {
    throwIfGameUnstarted();
    throwIfGameOver();
    this.passCounter++;
    if (isGameOver()) {
      this.broadcaster.gameOver();
    } else {
      nextPlayerTurn();
    }
  }

  @Override
  public void placeDiscInCell(RowColCoords cell) throws IllegalStateException {
    throwIfGameUnstarted();
    throwIfGameOver();
    throwIfInvalidCoords(cell);
    throwIfInvalidMove(cell);
    placeDisc(cell, getActivePlayerColor());
    captureDiscs(cell);
    this.passCounter = 0;
    nextPlayerTurn();
  }

  @Override
  public boolean anyLegalMoves() throws IllegalStateException {
    throwIfGameUnstarted();
    throwIfGameOver();
    for (int row = 0; row < this.grid.size(); row++) {
      for (int col = 0; col < this.grid.get(row).size(); col++) {
        if (validMove(new RowColCoords(row, col)).containsKey(true)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Map<Boolean, Integer> validMove(RowColCoords coords)
          throws IllegalArgumentException, IllegalStateException {
    throwIfGameUnstarted();
    throwIfGameOver();
    throwIfInvalidCoords(coords);

    if (!getColorAt(coords).equals(DiscColor.NONE)) {
      return Collections.singletonMap(false, 0);
    } else {
      List<List<RowColCoords>> toCapture = discsToCapture(coords);
      boolean empty = true;
      for (List<RowColCoords> line : toCapture) {
        if (!line.isEmpty()) {
          empty = false;
          break;
        }
      }
      return Collections.singletonMap(!empty, toCapture.size());
    }
  }

  /**
   * An enum representing the six directions around a pointy-top hexagon.
   */
  private enum Direction {
    topLeft,
    topRight,
    centerLeft,
    centerRight,
    bottomLeft,
    bottomRight
  }

  @Override
  public List<List<RowColCoords>> discsToCapture(RowColCoords coords) {
    List<List<RowColCoords>> discs = new ArrayList<>();
    // Check all cells adjacent to the given cell for discs of the opposite color
    Map<RowColCoords, Direction> adjacentOppositeColor = checkAdjacent(coords);
    // For each of those adjacent cells, go down the line
    for (RowColCoords cell : adjacentOppositeColor.keySet()) {
      List<RowColCoords> discsInLine = discsToCaptureInLine(cell, cell,
              adjacentOppositeColor, new ArrayList<>());
      if (!discsInLine.isEmpty()) {
        discs.add(discsInLine);
      }
    }
    return discs;
  }

  /**
   * Finds all the discs to capture in a single line from the given cell.
   * @param adjacentCell one of the six cells adjacent to the given cell
   * @param currentCell the current cell in the line (starting from adjacentCell)
   * @param allAdjacent the map of all adjacent cells to their direction from the
   *                    given cell
   * @param tempDiscs the running total list of discs that will be captured if the
   *                  line of enemy discs ends in the player's own disc rather than
   *                  an empty cell or going off the board
   * @return the list of all cells in one line from the given cell to be captured
   */
  private List<RowColCoords> discsToCaptureInLine(RowColCoords adjacentCell,
                                                  RowColCoords currentCell, 
                                                  Map<RowColCoords, Direction> allAdjacent,
                                                  List<RowColCoords> tempDiscs) {
    // Get the coordinates of the next cell in the line
    RowColCoords nextCell = nextInLine(currentCell, allAdjacent.get(adjacentCell));
    
    // Get the color of the disc corresponding to that cell
    DiscColor nextColor;
    try {
      nextColor = getColorAt(nextCell);
    } catch (IllegalArgumentException e) { // If nextCell is not a valid cell
      return new ArrayList<>();
    }
    
    // If the next cell has no disc, ignore tempDiscs and return nothing
    if (nextColor == DiscColor.NONE) {
      return new ArrayList<>();
    }
    // If the next disc's color is the color of the active player, the line is complete,
    // so add all the enemy discs in that line (tempDiscs) to the list of discs to capture
    // and return that list
    else if (nextColor == getActivePlayerColor()) {
      tempDiscs.add(adjacentCell);
      return tempDiscs;
    }
    // If the next disc's color is opposite of the active player's, the line is continuing,
    // so add the next cell to tempDiscs and then return discsToCaptureInLine, setting
    // "currentCell" to nextCell
    else if (nextColor == DiscColor.oppositeColor(getActivePlayerColor())) {
      tempDiscs.add(nextCell);
      return discsToCaptureInLine(adjacentCell, nextCell, allAdjacent, tempDiscs);
    } else {
      throw new IllegalStateException("No 4th color");
    }
  }

  /**
   * Finds all cells adjacent to the given cell that contain discs
   * of the color opposite to that of the active player, and then
   * maps those cells to the direction they are in relation to the
   * given cell.
   * @param coords a cell in grid
   * @return The map of adjacent cells to their direction from the
   *         given cell
   */
  private Map<RowColCoords, Direction> checkAdjacent(RowColCoords coords) {
    Map<RowColCoords, Direction> coordsWithDirections = new HashMap<>();

    // Center Left
    addAdjacentDirection(coordsWithDirections, Direction.centerLeft, coords);

    // Center Right
    addAdjacentDirection(coordsWithDirections, Direction.centerRight, coords);

    // Top Left
    addAdjacentDirection(coordsWithDirections, Direction.topLeft, coords);

    // Top Right
    addAdjacentDirection(coordsWithDirections, Direction.topRight, coords);

    // Bottom Left
    addAdjacentDirection(coordsWithDirections, Direction.bottomLeft, coords);

    // Bottom Right
    addAdjacentDirection(coordsWithDirections, Direction.bottomRight, coords);

    return coordsWithDirections;
  }

  /**
   * Gets the coordinates of a single direction adjacent to the given cell and then
   * puts them into the map of adjacent cells along with the direction they are from 
   * the given cell.
   * @param adjacentCells the map of all cells adjacent to the given cell to the
   *                      direction they are from the given cell
   * @param direction the direction specifying which of the six adjacent coordinates
   *                  to add to the map
   * @param coords a cell in grid
   */
  private void addAdjacentDirection(Map<RowColCoords, Direction> adjacentCells,
                                      Direction direction, RowColCoords coords) {
    RowColCoords adjacentModifiers = adjacentModifier(direction, coords);
    Optional<RowColCoords> coordinate = addToAdjacent(
            adjacentModifiers.getRow(), adjacentModifiers.getCol(), coords);
    coordinate.ifPresent(cell -> adjacentCells.put(cell, direction));
  }

  /**
   * Finds the proper modifiers to the given cell for each of the six directions.
   * @param direction the direction from the given cell to one of the six adjacent cells
   * @return the coordinates of one of the six adjacent cells
   */
  private RowColCoords adjacentModifier(Direction direction, RowColCoords coords) {
    switch (direction) {
      case topLeft:
        if (coords.getRow() > this.edgeLength - 1) {
          return new RowColCoords(-1, 0);
        } else {
          return new RowColCoords(-1, -1);
        }
      case topRight:
        if (coords.getRow() <= this.edgeLength - 1) {
          return new RowColCoords(-1, 0);
        } else {
          return new RowColCoords(-1, 1);
        }
      case centerLeft:
        return new RowColCoords(0, -1);
      case centerRight:
        return new RowColCoords(0, 1);
      case bottomLeft:
        if (coords.getRow() >= this.edgeLength - 1) {
          return new RowColCoords(1, -1);
        } else {
          return new RowColCoords(1, 0);
        }
      case bottomRight:
        if (coords.getRow() >= this.edgeLength - 1) {
          return new RowColCoords(1, 0);
        } else {
          return new RowColCoords(1, 1);
        }
      default:
        throw new IllegalStateException("No 7th direction");
    }
  }

  /**
   * Finds a cell adjacent to the given cell and returns its coordinates if it
   * contains a disc of the color opposite to that of the active player.
   * @param rowMod what the row of the given cell is modified by to find
   *               an adjacent cell (-1, 0, 1)
   * @param colMod what the column of the given cell is modified by to find
   *               an adjacent cell (-1, 0, 1)
   * @return the coordinates of the adjacent cell if it contains a disc with the
   *         correct color or Optional.empty() if the disc is the wrong color or the cell is empty.
   */
  private Optional<RowColCoords> addToAdjacent(int rowMod, int colMod, RowColCoords coords) {
    RowColCoords cell = new RowColCoords(
            coords.getRow() + rowMod,
            coords.getCol() + colMod);

    DiscColor oppositePlayerColor = DiscColor.oppositeColor(getActivePlayerColor());
    DiscColor colorAtCoords;
    try {
      colorAtCoords = getColorAt(cell);
    } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
      return Optional.empty();
    }

    if (oppositePlayerColor == colorAtCoords) {
      return Optional.of(cell);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Finds the next cell in the line starting from the given cell and moving
   * in one of the six possible directions.
   * @param cell the most recent cell in the line from selectedCell to cell
   * @param dir the direction from selectedCell to cell
   * @return the coordinates of the next cell in the line
   */
  private RowColCoords nextInLine(RowColCoords cell, Direction dir) {
    RowColCoords modifiers = adjacentModifier(dir, cell);
    return new RowColCoords(cell.getRow() + modifiers.getRow(),
            cell.getCol() + modifiers.getCol());
  }

  /**
   * Captures all valid enemy discs after a move is played.
   */
  private void captureDiscs(RowColCoords coords) {
    List<List<RowColCoords>> discs = discsToCapture(coords);
    for (List<RowColCoords> line : discs) {
      for (RowColCoords disc : line) {
        flipDisc(disc);
      }
    }
  }

  @Override
  public boolean isGameOver() {
    if (this.passCounter >= 2) {
      this.gameState = GameState.GAMEOVER;
    }
    return this.gameState == GameState.GAMEOVER;
  }

  @Override
  public GameState getGameState() {
    return this.gameState;
  }

  @Override
  public List<List<DiscColor>> getGrid() {
    List<List<DiscColor>> gridCopy = new ArrayList<>();
    for (List<DiscColor> row : this.grid) {
      gridCopy.add(Collections.unmodifiableList(row));
    }
    return Collections.unmodifiableList(gridCopy);
  }

  @Override
  public DiscColor getActivePlayerColor() throws IllegalStateException {
    if (this.gameState == GameState.BLACKMOVE) {
      return DiscColor.BLACK;
    } else if (this.gameState == GameState.WHITEMOVE) {
      return DiscColor.WHITE;
    } else if (this.gameState == GameState.UNSTARTED) {
      throw new IllegalStateException("The game hasn't started");
    } else {
      throw new IllegalStateException("The game has already ended");
    }
  }

  @Override
  public int getPlayerScore(DiscColor c) throws IllegalArgumentException {
    throwIfInvalidPlayer(c);
    int playerScore = 0;
    for (int row = 0; row < this.grid.size(); row++) {
      for (int col = 0; col < this.grid.get(row).size(); col++) {
        if (getColorAt(new RowColCoords(row, col)) == c) {
          playerScore++;
        }
      }
    }
    return playerScore;
  }

  @Override
  public DiscColor getColorAt(RowColCoords coords) throws IllegalArgumentException {
    throwIfInvalidCoords(coords);
    return this.grid.get(coords.getRow()).get(coords.getCol());
  }

  @Override
  public DiscColor getWinner() throws IllegalStateException {
    throwIfGameNotOver();
    int p1Score = getPlayerScore(DiscColor.BLACK);
    int p2Score = getPlayerScore(DiscColor.WHITE);
    if (p1Score > p2Score) {
      return DiscColor.BLACK;
    } else if (p2Score > p1Score) {
      return DiscColor.WHITE;
    } else {
      return DiscColor.NONE;
    }
  }

  @Override
  public int getEdgeLength() {
    return this.edgeLength;
  }

  @Override
  public int getPassCounter() {
    return this.passCounter;
  }

  /**
   * Switches the active player to the next player and updates the game state.
   */
  private void nextPlayerTurn() {
    if (this.gameState == GameState.UNSTARTED) {
      this.gameState = GameState.BLACKMOVE;
    } else {
      switchGameState();
    }
    this.broadcaster.modelMutated();
  }

  /**
   * Switches the game state between BLACKMOVE and WHITEMOVE.
   */
  private void switchGameState() {
    if (this.gameState == GameState.BLACKMOVE) {
      this.gameState = GameState.WHITEMOVE;
    } else if (this.gameState == GameState.WHITEMOVE) {
      this.gameState = GameState.BLACKMOVE;
    }
  }

  /**
   * Places a disc on the grid.
   * @param coords the row/column coordinates specifying a cell in the grid
   * @param c the color of the disc to be placed
   */
  private void placeDisc(RowColCoords coords, DiscColor c) {
    this.grid.get(coords.getRow()).set(coords.getCol(), c);
  }

  /**
   * Flips the color of a cell in the grid.
   * @param coords the row (0-indexed from the top) and column (0-indexed from
   *              the left) coordinates specifying a cell in grid
   * @throws IllegalArgumentException if the coordinates do not specify a valid
   *                                  cell in the grid or if the specified cell
   *                                  does not contain a disc
   */
  private void flipDisc(RowColCoords coords) throws IllegalArgumentException {
    throwIfInvalidCoords(coords);
    DiscColor discColor = getColorAt(coords);
    throwIfNoDisc(discColor);
    placeDisc(coords, DiscColor.oppositeColor(discColor));
  }

  private void throwIfGameStarted() throws IllegalStateException {
    if (this.gameState != GameState.UNSTARTED) {
      throw new IllegalStateException("The game has already started");
    }
  }

  private void throwIfGameUnstarted() throws IllegalStateException {
    if (this.gameState == GameState.UNSTARTED) {
      throw new IllegalStateException("The game hasn't started yet");
    }
  }

  private void throwIfGameOver() throws IllegalStateException {
    if (this.gameState == GameState.GAMEOVER) {
      throw new IllegalStateException("The game has ended");
    }
  }

  private void throwIfGameNotOver() throws IllegalStateException {
    if (this.gameState != GameState.GAMEOVER) {
      throw new IllegalStateException("The game is not over yet");
    }
  }

  private void throwIfInvalidPlayer(DiscColor c) throws IllegalArgumentException {
    if (c == DiscColor.NONE) {
      throw new IllegalArgumentException("Invalid player");
    }
  }

  private void throwIfNoDisc(DiscColor c) {
    if (c == DiscColor.NONE) {
      throw new IllegalStateException("No disc");
    }
  }

  private void throwIfInvalidCoords(RowColCoords coords) {
    if (coords.getRow() < 0
            || coords.getRow() >= this.grid.size()
            || coords.getCol() < 0
            || coords.getCol() >= this.grid.get(coords.getRow()).size()) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
  }

  private void throwIfInvalidMove(RowColCoords coords) {
    if (validMove(coords).containsKey(false)) {
      throw new IllegalStateException("Invalid move");
    }
  }
}
