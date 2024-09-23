package cs3500.reversi.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import cs3500.reversi.controller.ModelCallbackListener;

/**
 * Mock model that sets all possible moves to be invalid except for one given cell.
 */
public class ForcedMoveMock implements MutableReversiModel {
  private final ReversiModel delegateModel;
  private final RowColCoords forcedMoveCell;

  /**
   * Constructs a ForcedMoveMock.
   * @param edgeLength the number of cells per edge of the mock's grid
   * @param forcedMoveCell the move that the mock is forcing the MaxCapture strategy to choose
   */
  public ForcedMoveMock(int edgeLength, RowColCoords forcedMoveCell) {
    this.delegateModel = ReversiModel.create(edgeLength);
    this.forcedMoveCell = forcedMoveCell;
  }

  @Override
  public Map<Boolean, Integer> validMove(RowColCoords coords)
          throws IllegalArgumentException, IllegalStateException {
    if (this.forcedMoveCell.equals(coords)) {
      return Collections.singletonMap(true, 100); // Say the forced move is worth 100 points
    } else {
      return Collections.singletonMap(false, 0); // Say every other move is invalid and worth 0
    }
  }

  @Override
  public List<List<RowColCoords>> discsToCapture(RowColCoords coords) {
    return delegateModel.discsToCapture(coords);
  }


  // Delegate the rest:

  @Override
  public List<List<DiscColor>> getGrid() {
    return delegateModel.getGrid();
  }

  @Override
  public boolean anyLegalMoves() {
    return delegateModel.anyLegalMoves();
  }

  @Override
  public GameState getGameState() {
    return delegateModel.getGameState();
  }

  @Override
  public int getEdgeLength() {
    return delegateModel.getEdgeLength();
  }

  @Override
  public boolean isGameOver() {
    return delegateModel.isGameOver();
  }

  @Override
  public DiscColor getActivePlayerColor() throws IllegalStateException {
    return delegateModel.getActivePlayerColor();
  }

  @Override
  public int getPlayerScore(DiscColor c) throws IllegalArgumentException {
    return delegateModel.getPlayerScore(c);
  }

  @Override
  public DiscColor getColorAt(RowColCoords coords) throws IllegalArgumentException {
    return delegateModel.getColorAt(coords);
  }

  @Override
  public DiscColor getWinner() throws IllegalStateException {
    return delegateModel.getWinner();
  }

  @Override
  public int getPassCounter() {
    return delegateModel.getPassCounter();
  }

  @Override
  public void pass() throws IllegalStateException {
    delegateModel.pass();
  }

  @Override
  public void placeDiscInCell(RowColCoords cell) throws IllegalStateException {
    delegateModel.placeDiscInCell(cell);
  }

  @Override
  public void startGame() throws IllegalStateException {
    delegateModel.startGame();
  }

  @Override
  public void setBroadcastListener(ModelCallbackListener controller) {
    delegateModel.setBroadcastListener(controller);
  }

  @Override
  public void removeBroadcastListener(ModelCallbackListener controller) {
    delegateModel.removeBroadcastListener(controller);
  }
}
