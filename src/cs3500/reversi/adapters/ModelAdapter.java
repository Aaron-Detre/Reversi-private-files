package cs3500.reversi.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.provider.model.CubicPosn;
import cs3500.reversi.provider.model.DiskState;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;

/**
 * Adapting our model to our provider's read only model interface.
 */
public class ModelAdapter implements ReadOnlyReversiModel {
  private final ROReversiModel delegateModel;
  private final AdapterUtils utils;

  public ModelAdapter(ROReversiModel delegateModel) {
    this.delegateModel = delegateModel;
    this.utils = new AdapterUtils();
  }

  @Override
  public int getBoardSize() {
    // I'm not sure why their board size is edge length - 1, but that's what their strategies use
    return delegateModel.getEdgeLength() - 1;
  }

  @Override
  public DiskState getState(CubicPosn posn) {
    return this.utils.colorToState(
            delegateModel.getColorAt(this.utils.cubicToRowCol(posn, delegateModel)));
  }

  @Override
  public boolean isValidMove(CubicPosn posn, DiskState color) {
    return isCurrentPlayer(color)
            && delegateModel.validMove(this.utils.cubicToRowCol(posn, delegateModel))
            .containsKey(true);
  }

  @Override
  public int getScore(DiskState disk) {
    return delegateModel.getPlayerScore(this.utils.stateToColor(disk));
  }

  @Override
  public boolean anyValidMoves(DiskState state) {
    return isCurrentPlayer(state) && delegateModel.anyLegalMoves();
  }

  @Override
  public boolean isGameOver() {
    return delegateModel.isGameOver();
  }

  @Override
  public boolean isCurrentPlayer(DiskState diskState) {
    return diskState.equals(this.utils.colorToState(delegateModel.getActivePlayerColor()));
  }

  @Override
  public List<List<CubicPosn>> findAllSeams(CubicPosn posn, DiskState color) {
    if (!isCurrentPlayer(color)) {
      throw new IllegalStateException("Not active player");
    }
    RowColCoords rowCol = this.utils.cubicToRowCol(posn, this.delegateModel);
    if (this.delegateModel.getColorAt(rowCol) != DiscColor.NONE) {
      return new ArrayList<>();
    }
    List<List<RowColCoords>> toCapture = delegateModel.discsToCapture(rowCol);
    List<List<CubicPosn>> allSeams = new ArrayList<>();
    for (List<RowColCoords> line : toCapture) {
      List<CubicPosn> seam = new ArrayList<>();
      for (RowColCoords coords : line) {
        seam.add(this.utils.rowColToCubic(coords, delegateModel));
      }
      allSeams.add(seam);
    }
    return allSeams;
  }

  @Override
  public HashMap<CubicPosn, DiskState> getBoard() {
    HashMap<CubicPosn, DiskState> board = new HashMap<>();
    List<List<DiscColor>> grid = delegateModel.getGrid();
    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        RowColCoords coords = new RowColCoords(row, col);
        board.put(this.utils.rowColToCubic(coords, delegateModel),
                this.utils.colorToState(delegateModel.getColorAt(coords)));
      }
    }
    return board;
  }
}