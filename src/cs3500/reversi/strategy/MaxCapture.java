package cs3500.reversi.strategy;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * A FilteringReversiStrategy that filters out all the moves that don't capture the
 * maximum number of the opponent's discs.
 */
public class MaxCapture implements FilteringReversiStrategy {
  @Override
  public Map<RowColCoords, Integer> filterMoves(Map<RowColCoords, Integer> possibleMoves,
                                                ROReversiModel model) {
    int currentMax = 0;

    // Find the maximum number of discs that could be captured from the current map of moves
    for (int score : possibleMoves.values()) {
      currentMax = Math.max(currentMax, score);
    }

    // Remove every entry in the current map of possible moves that has a number of discs to
    // capture less than that maximum
    List<RowColCoords> cells = possibleMoves.keySet().stream().collect(Collectors.toList());
    for (RowColCoords cell : cells) {
      if (possibleMoves.get(cell) < currentMax) {
        possibleMoves.remove(cell);
      }
    }

    return possibleMoves;
  }
}
