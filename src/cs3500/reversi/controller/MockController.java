package cs3500.reversi.controller;

import cs3500.reversi.GenericMock;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.view.IReversiView;
import cs3500.reversi.view.MockView;

/**
 * A Mock for the controller that appends useful messages to a log depending on the
 * methods called.
 */
public class MockController extends GenericMock implements IReversiController {
  private final MockView mockView;

  /**
   * Constructs a MockController.
   * @param mockView the mock view that this mock controller delegates to for certain methods
   * @param log the string output that gets built up by the different methods of the mock
   */
  public MockController(MockView mockView, StringBuilder log) {
    super(log);
    this.mockView = mockView;
  }

  @Override
  public void placeDiscInGivenCell(RowColCoords cell) {
    this.appendToLog(String.format("attempted to place disc at row %d, column %d",
            cell.getRow(), cell.getCol()));
  }

  @Override
  public void pass() {
    this.appendToLog("attempted to pass");
  }

  @Override
  public ROReversiModel getModel() {
    return null;
  }

  @Override
  public IReversiView getView() {
    return this.mockView;
  }

  @Override
  public void endGame() {
    this.mockView.endGame();
  }

  @Override
  public void updateView() {
    this.mockView.rerender();
  }

  @Override
  public void nextPlayerTurn() {
    this.mockView.yourTurnMessage();
  }
}
