package cs3500.reversi.view;

import cs3500.reversi.GenericMock;
import cs3500.reversi.model.RowColCoords;

/**
 * A Mock for IReversiPanel that appends useful messages to a log depending on the
 * methods called.
 */
public class MockPanel extends GenericMock implements IReversiPanel {
  /**
   * Constructs a MockPanel.
   * @param log the string output that gets built up by the different methods of the mock
   */
  public MockPanel(StringBuilder log) {
    super(log);
  }

  @Override
  public void setActionListeners(PlayerActions features) {
    this.appendToLog("action listeners set");
  }

  @Override
  public void setSelectedCell(RowColCoords cell) throws IllegalArgumentException {
    this.appendToLog(String.format("attempted to select the cell at row %d and column %d",
            cell.getRow(), cell.getCol()));
  }

  @Override
  public void deselectSelectedCell() {
    this.appendToLog("deselected the selected cell");
  }
}
