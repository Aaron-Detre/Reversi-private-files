package cs3500.reversi.strategy;

import java.util.Map;

import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * A function that uses some strategy to narrow down the possible moves that can be made.
 */
public interface FilteringReversiStrategy {
  /**
   * Narrows down the set of legal moves that are being considered using some strategy.
   * @param possibleMoves the map of cells being considered along with how many discs
   *                      would be captured if the active player placed in that cell
   * @param model the model for the game being played
   * @return the map of possible moves after filtering out moves based on the strategy used.
   */
  Map<RowColCoords, Integer> filterMoves(Map<RowColCoords, Integer> possibleMoves,
                                         ROReversiModel model);
}
