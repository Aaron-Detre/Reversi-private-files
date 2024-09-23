package cs3500.reversi.view;

import cs3500.reversi.model.RowColCoords;

/**
 * The interface for the main panel in Reversi.
 */
public interface IReversiPanel {
  /**
   * Sets the actions that a player can make by interacting with the panel.
   * @param actions all the actions the player can make
   */
  void setActionListeners(PlayerActions actions);

  /**
   * Sets the selected cell on the grid the panel is displaying.
   * @param cell a cell in the grid
   * @throws IllegalArgumentException if the cell coordinates don't match a cell in the grid
   */
  void setSelectedCell(RowColCoords cell) throws IllegalArgumentException ;

  /**
   * Deselects the selected cell on the grid the panel is displaying.
   */
  void deselectSelectedCell();
}
