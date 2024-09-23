package cs3500.reversi.view;

import cs3500.reversi.model.RowColCoords;

/**
 * An implementation of PlayerActions where the player can make no actions.
 */
public class NoActions implements PlayerActions {
  /**
   * Constructs a new NoActions object.
   */
  public NoActions() {
    // Default constructor
  }

  @Override
  public void selectCell(RowColCoords coords) throws IllegalArgumentException {
    // Do nothing
  }

  @Override
  public void deselectCell() {
    // Do nothing
  }

  @Override
  public void placeDiscInSelectedCell(RowColCoords selectedCell) {
    // Do nothing
  }

  @Override
  public void passTurn() {
    // Do nothing
  }
}
