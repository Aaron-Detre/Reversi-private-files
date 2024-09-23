package cs3500.reversi.view;

import cs3500.reversi.GenericMock;
import cs3500.reversi.model.RowColCoords;

/**
 * A Mock for IReversiView that appends useful messages to a log depending on the
 * methods called.
 */
public class MockView extends GenericMock implements IReversiView {
  private final MockPanel panel;

  /**
   * Constructs a MockView.
   * @param panel the mock panel that this mock view delegates to for certain methods
   * @param log the string output that gets built up by the different methods of the mock
   */
  public MockView(MockPanel panel, StringBuilder log) {
    super(log);
    this.panel = panel;
  }

  @Override
  public void render() {
    this.appendToLog("view rendered");
  }

  @Override
  public void rerender() {
    this.appendToLog("view re-rendered");
  }

  @Override
  public void setPanelActions(PlayerActions viewActions) {
    this.panel.setActionListeners(viewActions);
  }

  @Override
  public void setSelectedCell(RowColCoords cell) throws IllegalArgumentException {
    this.panel.setSelectedCell(cell);
  }

  @Override
  public void deselectSelectedCell() {
    this.panel.deselectSelectedCell();
  }

  @Override
  public void endGame() {
    this.appendToLog("end game message delivered");
  }

  @Override
  public void notActivePlayerMessage() {
    this.appendToLog("not active player message delivered");
  }

  @Override
  public void illegalMoveMessage() {
    this.appendToLog("illegal move message delivered");
  }

  @Override
  public void yourTurnMessage() {
    this.appendToLog("your turn message delivered");
  }
}
