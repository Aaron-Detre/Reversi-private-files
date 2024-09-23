package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cs3500.reversi.controller.ModelCallbackListener;

/**
 * Mock model that appends the coordinates of the grid when getGrid is called and the
 * coordinates of any cell that is called with validMove.
 */
public class CoordinateTranscriptMock implements MutableReversiModel {
  private final int edgeLength;
  private final List<List<RowColCoords>> grid;
  private final StringBuilder log;

  /**
   * Constructs a CoordinateTranscriptMock.
   * @param edgeLength the number of cells per edge of the mock's grid
   * @param log a StringBuilder that collects the transcript of the coordinates
   */
  public CoordinateTranscriptMock(int edgeLength, StringBuilder log) {
    this.edgeLength = edgeLength;
    this.grid = new ArrayList<>();
    for (int row = 0; row < edgeLength * 2 - 1; row++) {
      int rowLength =
              ((2 * edgeLength) - 1) - Math.abs(row - (this.getEdgeLength() - 1));
      List<RowColCoords> gridRow = new ArrayList<>();
      for (int col = 0; col < rowLength; col++) {
        gridRow.add(new RowColCoords(row, col));
      }
      this.grid.add(gridRow);
    }
    this.log = log;
  }

  @Override
  public List<List<DiscColor>> getGrid() {
    List<List<DiscColor>> mockGrid = new ArrayList<>();
    for (List<RowColCoords> row : this.grid) {
      List<DiscColor> mockGridRow = new ArrayList<>();
      for (RowColCoords cell : row) {
        this.log.append(String.format("%d %d, ", cell.getRow(), cell.getCol()));
        mockGridRow.add(DiscColor.NONE);
      }
      mockGrid.add(mockGridRow);
    }
    this.log.append("\n");
    return mockGrid;
  }

  @Override
  public Map<Boolean, Integer> validMove(RowColCoords coords) {
    this.log.append(String.format("%d %d, ", coords.getRow(), coords.getCol()));
    return Collections.singletonMap(true, 0);
  }


  // Delegate the rest:

  @Override
  public boolean anyLegalMoves() {
    return true; // avoiding the exception
  }

  @Override
  public GameState getGameState() {
    // assume only testing for black moves I guess? Not sure how else to do it
    return GameState.BLACKMOVE;
  }

  @Override
  public int getEdgeLength() {
    return this.edgeLength;
  }




  // Nothing else is used
  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public List<List<RowColCoords>> discsToCapture(RowColCoords coords) {
    return null;
  }

  @Override
  public DiscColor getActivePlayerColor() throws IllegalStateException {
    return null;
  }

  @Override
  public int getPlayerScore(DiscColor c) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public DiscColor getColorAt(RowColCoords coords) throws IllegalArgumentException {
    return null;
  }

  @Override
  public DiscColor getWinner() throws IllegalStateException {
    return null;
  }

  @Override
  public int getPassCounter() {
    return 0;
  }

  @Override
  public void pass() throws IllegalStateException {
    // Do nothing
  }

  @Override
  public void placeDiscInCell(RowColCoords cell) throws IllegalStateException {
    // Do nothing
  }

  @Override
  public void startGame() throws IllegalStateException {
    // Do nothing
  }

  @Override
  public void setBroadcastListener(ModelCallbackListener controller) {
    // Do nothing
  }

  @Override
  public void removeBroadcastListener(ModelCallbackListener controller) {
    // Do nothing
  }
}
