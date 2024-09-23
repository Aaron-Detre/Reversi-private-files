package cs3500.reversi.model;

/**
 * Represents the mutable model interface for playing a game of Reversi.
 */
public interface MutableReversiModel extends ROReversiModel, ModelBroadcaster {
  /**
   * Passes the active player's turn and makes the other player the new active player.
   * @throws IllegalStateException if the game hasn't started or is over
   */
  void pass() throws IllegalStateException;

  /**
   * Sets the selected cell's color to that of the active player.
   * @throws IllegalStateException if the game hasn't started, is over, if there is no selected
   *                               cell, or if the move is invalid
   */
  void placeDiscInCell(RowColCoords cell) throws IllegalStateException;

  /**
   * Starts the game by changing the game state from UNSTARTED to BLACKMOVE.
   * @throws IllegalStateException if the game has already started
   */
  void startGame() throws IllegalStateException;
}
