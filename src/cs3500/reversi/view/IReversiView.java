package cs3500.reversi.view;

import cs3500.reversi.model.RowColCoords;

/**
 * An interface for the frame of the view of Reversi. Allows the game to be viewed.
 */
public interface IReversiView {
  /**
   * Sets up and draws the frame's main panel.
   */
  void render();

  /**
   * Deselects the selected cell if there is one and then repaints the frame's panel.
   */
  void rerender();

  /**
   * Sets the actions that a player can make by interacting with the view's panel.
   * @param viewActions all the actions the player can make
   */
  void setPanelActions(PlayerActions viewActions);

  /**
   * Sets the selected cell on the grid the view's panel is displaying.
   * @param cell a cell in the grid
   * @throws IllegalArgumentException if the cell coordinates don't match a cell in the grid
   */
  void setSelectedCell(RowColCoords cell) throws IllegalArgumentException ;

  /**
   * Deselects the selected cell on the grid the view's panel is displaying.
   */
  void deselectSelectedCell();

  /**
   * Displays a game over message if the game is over.
   * @throws IllegalStateException if the game is not over
   */
  void endGame() throws IllegalStateException;

  /**
   * Displays a warning that it is not the player's turn.
   */
  void notActivePlayerMessage();

  /**
   * Displays a warning that a move is illegal.
   */
  void illegalMoveMessage();

  /**
   * Displays a message letting the player know it's their turn.
   */
  void yourTurnMessage();
}
