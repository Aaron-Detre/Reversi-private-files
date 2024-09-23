package cs3500.reversi.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.model.MutableReversiModel;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * A FilteringReversiStrategy that filters out all the moves that, if the opponent
 * were to try to capture the maximum discs possible, minimizes the number of discs
 * they could capture.
 */
public class MinimizeOpponentMaxCapture implements FilteringReversiStrategy {
  @Override
  public Map<RowColCoords, Integer> filterMoves(Map<RowColCoords, Integer> possibleMoves,
                                                ROReversiModel model) {
    Map<RowColCoords, Map<RowColCoords, Integer>>
            allOpponentMovesForEachPlayerMove = getAllOpponentMoves(possibleMoves, model);
    int minMaxOpponentCapture = getMinMaxOpponentCapture(allOpponentMovesForEachPlayerMove);
    removeNonMinimizingMoves(possibleMoves,
            allOpponentMovesForEachPlayerMove, minMaxOpponentCapture);
    return possibleMoves;
  }

  /**
   * Creates a map from each of the given possible move coordinates to a map of all the opponent's
   * possible moves if that move was played.
   * @param possibleMoves the current map of legal moves to the number of discs that move captures
   * @param model the model for the game being played
   * @return a map with all the values inserted
   */
  private Map<RowColCoords, Map<RowColCoords, Integer>> getAllOpponentMoves(
          Map<RowColCoords, Integer> possibleMoves, 
          ROReversiModel model) {
    Map<RowColCoords, Map<RowColCoords, Integer>> map = new HashMap<>();
    for (RowColCoords cell : possibleMoves.keySet()) {
      // For each move in the current set of moves, make that move on a copy of the model
      MutableReversiModel copyModel = ReversiModel.createCopy(model);
      copyModel.placeDiscInCell(cell);

      // Then get all the valid moves the opponent can make
      Map<RowColCoords, Integer> opponentPossibleMoves = StrategyUtils.getAllValidMoves(copyModel);
      map.put(cell, opponentPossibleMoves);
    }
    
    return map;
  }

  /**
   * Finds the smallest number of discs the opponent could capture if they were trying to capture
   * the most discs possible.
   * @param allOpponentMovesForEachPlayerMove a map from each of the player's moves to a map of
   *                                          each of the opponent's moves if the player were to
   *                                          play the corresponding move
   * @return the smallest maximum number of discs that could be captured on the opponent's turn
   *         depending on the player's move
   */
  private int getMinMaxOpponentCapture(
          Map<RowColCoords, Map<RowColCoords, Integer>> allOpponentMovesForEachPlayerMove) {
    int minMaxOpponentCapture = -1;
    for (RowColCoords playerMove : allOpponentMovesForEachPlayerMove.keySet()) {
      // For each of the player's available moves, get all of the opponent's available moves and
      // find the maximum number of discs the opponent could capture from their options
      Map<RowColCoords, Integer> opponentMoves = allOpponentMovesForEachPlayerMove.get(playerMove);
      Optional<Integer> maxValue = opponentMoves.values().stream().max(Integer::compareTo);

      if (maxValue.isPresent()) {
        // The opponent has at least one move to play
        if (minMaxOpponentCapture == -1) {
          minMaxOpponentCapture = maxValue.get();
        } else {
          // Find the smallest maximum capture
          minMaxOpponentCapture = Math.min(minMaxOpponentCapture, maxValue.get());
        }
      } else {
        // The opponent has no possible moves
        // They can't have less than 0, so that is the minimum max capture
        return 0;
      }
    }
    return minMaxOpponentCapture;
  }

  /**
   * Removes all of the player's moves that would allow the opponent to capture more
   * than minMaxOpponentCapture.
   * @param possibleMoves the current map of legal moves to the number of discs that move captures
   * @param allOpponentMovesForEachPlayerMove a map from each of the player's moves to a map of
   *                                          each of the opponent's moves if the player were to
   *                                          play the corresponding move
   * @param minMaxOpponentCapture the smallest maximum number of discs that could be captured on
   *                              the opponent's turn depending on the player's move
   */
  private void removeNonMinimizingMoves(
          Map<RowColCoords, Integer> possibleMoves, Map<RowColCoords,
          Map<RowColCoords, Integer>> allOpponentMovesForEachPlayerMove,
          int minMaxOpponentCapture) {
    for (RowColCoords playerMove : allOpponentMovesForEachPlayerMove.keySet()) {
      // Gets the max capture number for each of the player's moves
      Optional<Integer> maxValue = allOpponentMovesForEachPlayerMove.get(playerMove)
              .values().stream().max(Integer::compareTo);

      // If there's no max value, the opponent has no moves to play
      if (maxValue.isEmpty()) {
        maxValue = Optional.of(0);
      }

      // If the opponent's move max value is greater than the minMax value, remove the player's
      // move that would allow the opponent to capture those discs
      if (maxValue.get() > minMaxOpponentCapture) {
        possibleMoves.remove(playerMove);
      }
    }
  }
}
