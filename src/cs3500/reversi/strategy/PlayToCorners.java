package cs3500.reversi.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * A FilteringReversiStrategy that tries to filter out moves that aren't corners of the board.
 */
public class PlayToCorners implements FilteringReversiStrategy {
  @Override
  public Map<RowColCoords, Integer> filterMoves(Map<RowColCoords, Integer> possibleMoves,
                                                ROReversiModel model) {
    // Make a copy of the current map of possible moves
    Map<RowColCoords, Integer> copyOfMoves = new HashMap<>(possibleMoves);

    // Get a list of all the corner cells
    List<RowColCoords> corners = StrategyUtils.getCornerCells(model);

    // Remove each cell that isn't a corner cell from the map of possible moves
    for (RowColCoords cell : possibleMoves.keySet()) {
      if (!corners.contains(cell)) {
        copyOfMoves.remove(cell);
      }
    }

    // If doing that removed every option (i.e. there are no corners to move to),
    // return the original map of possible moves
    if (copyOfMoves.isEmpty()) {
      return possibleMoves;
    } else {
      return copyOfMoves;
    }
  }
}
