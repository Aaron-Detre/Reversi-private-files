package cs3500.reversi.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * A FilteringReversiStrategy that tries to filter out cells adjacent to the corners of the board.
 */
public class AvoidNextToCorners implements FilteringReversiStrategy {
  @Override
  public Map<RowColCoords, Integer> filterMoves(Map<RowColCoords, Integer> possibleMoves,
                                                ROReversiModel model) {
    // Make a copy of the current map of possible moves
    Map<RowColCoords, Integer> copyOfMoves = new HashMap<>(possibleMoves);

    // Get a list of all the cells adjacent to corners
    List<RowColCoords> adjacentToCorners = StrategyUtils.getCellsAdjacentToCorners(model);

    // Remove each cell adjacent to a corner from the map of possible moves
    for (RowColCoords cell : adjacentToCorners) {
      copyOfMoves.remove(cell);
    }

    // If doing that removed every option (i.e. the only possible moves are adjacent to corners),
    // return the original map of possible moves
    if (copyOfMoves.isEmpty()) {
      return possibleMoves;
    } else {
      return copyOfMoves;
    }
  }
}
