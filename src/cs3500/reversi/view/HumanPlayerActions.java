package cs3500.reversi.view;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.model.RowColCoords;

/**
 * Represents all the actions a HumanPlayer is able to make.
 */
public class HumanPlayerActions implements PlayerActions {
  private final IReversiView view;
  private final IReversiController controller;

  /**
   * Constructs a new HumanPlayerActions object.
   * @param view the view that the player can interact with
   * @param controller the controller that is controlling this player
   */
  public HumanPlayerActions(IReversiView view, IReversiController controller) {
    this.view = view;
    this.controller = controller;
  }

  @Override
  public void selectCell(RowColCoords coords) throws IllegalArgumentException {
    this.view.setSelectedCell(coords);
  }

  @Override
  public void deselectCell() {
    this.view.deselectSelectedCell();
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
