package cs3500.reversi.view;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.model.RowColCoords;

/**
 * Represents all the actions an AIPlayer is able to make.
 */
public class AIPlayerActions implements PlayerActions {
  private final IReversiController controller;

  /**
   * Constructs a new AIPlayerActions object.
   * @param controller the controller that is controlling this player.
   */
  public AIPlayerActions(IReversiController controller) {
    this.controller = controller;
  }

  @Override
  public void selectCell(RowColCoords coords) throws IllegalArgumentException {
    // Do nothing - AI can't interact with the view
  }

  @Override
  public void deselectCell() {
    // Do nothing - AI can't interact with the view
  }

  @Override
  public void placeDiscInSelectedCell(RowColCoords selectedCell) {
    this.controller.placeDiscInGivenCell(selectedCell);
  }

  @Override
  public void passTurn() {
    this.controller.pass();
  }
}
