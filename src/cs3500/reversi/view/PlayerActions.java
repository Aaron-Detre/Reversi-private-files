package cs3500.reversi.view;

import cs3500.reversi.model.RowColCoords;

/**
 * An interface describing all the actions a player can make while playing Reversi.
 */
public interface PlayerActions {
  /**
   * The player attempts to select a cell in the grid on the view.
   * @param coords coordinates specifying a cell in the grid
   * @throws IllegalArgumentException if the cell coordinates don't match a cell in the grid
   */
  void selectCell(RowColCoords coords) throws IllegalArgumentException;

  /**
   * The player attempts to deselect the selected cell in the grid on the view.
   */
  void deselectCell();

  /**
   * The player attempts to place their disc on the selected cell of the board.
   * @param selectedCell the coordinates of the cell the player wants to place their disc on
   *                     (the selected cell for humans, the cell returned by chooseMove for AIs)
   */
  void placeDiscInSelectedCell(RowColCoords selectedCell);

  /**
   * The player attempts to pass their turn.
   */
  void passTurn();
}
