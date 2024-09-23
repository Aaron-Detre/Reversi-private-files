package cs3500.reversi.model;

import java.util.List;
import java.util.Map;

/**
 * Represents the read only model interface for playing a game of Reversi.
 */
public interface ROReversiModel {
  /**
   * Determines whether the game has ended.
   * @return true if the game is over
   */
  boolean isGameOver();

  /**
   * Returns the current state of the game.
   * @return the current state of the game
   */
  GameState getGameState();

  /**
   * Returns the grid used by the model.
   * @return all the contents of the cells in the grid in a 0-indexed list of 0-indexed lists
   *         where the inner lists represent each row of the grid
   *         (Ex. listOfList.get(2).get(3) would get the 4th hexagon in the 3rd row)
   */
  List<List<DiscColor>> getGrid();

  /**
   * Returns the color of the player whose turn it is.
   * @return the color of the player whose turn it is
   * @throws IllegalStateException if the game hasn't started or has ended
   */
  DiscColor getActivePlayerColor() throws IllegalStateException;

  /**
   * Returns the score for the selected player.
   * @param c either black or white
   * @return the number of cells that the player has on the grid
   * @throws IllegalArgumentException if the color isn't black or white
   */
  int getPlayerScore(DiscColor c) throws IllegalArgumentException;

  /**
   * Returns the color of the disc (or NONE if no disc) specified by the coordinates.
   * @param coords the row (0-indexed from the top) and column (0-indexed from
   *               the left) coordinates specifying a cell in grid
   * @return the color of the contents of the cell specified by the coordinates
   * @throws IllegalArgumentException if the coordinates do not specify a valid cell in the grid
   */
  DiscColor getColorAt(RowColCoords coords) throws IllegalArgumentException;

  /**
   * Returns the color of the player who has won the game.
   * @return the color of the player who has won the game or Color.NONE if it is a draw
   * @throws IllegalStateException if the game is not over
   */
  DiscColor getWinner() throws IllegalStateException;

  /**
   * Returns the number of cells per edge of the regular hexagonal grid.
   * @return the number of cells
   */
  int getEdgeLength();

  /**
   * Returns the number of consecutive passes in a row at any given moment
   * in the game.
   * @return the number of consecutive passes
   */
  int getPassCounter();

  /**
   * Determines whether the active player has any moves that they could play.
   * @return true iff the player has at least one valid move
   * @throws IllegalStateException if the game hasn't started or is over
   */
  boolean anyLegalMoves() throws IllegalStateException;

  /**
   * Determines whether it would be valid for the active player to place
   * a disc at the given cell by checking that the list of discs to capture
   * is not empty.
   * @param coords a cell in the grid
   * @return true/false if the move is valid/invalid mapped to the number of discs
   *         that would be captured if that move is played (0 if invalid)
   * @throws IllegalArgumentException if the coordinates do not specify a valid cell in the grid
   * @throws IllegalStateException if the game hasn't started or is over
   */
  Map<Boolean, Integer> validMove(RowColCoords coords)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Finds all the discs that would be captured if the active player
   * places their disc on the given cell.
   * @param coords a cell in grid
   * @return the list of all the discs that would be captured
   */
  List<List<RowColCoords>> discsToCapture(RowColCoords coords);
}
