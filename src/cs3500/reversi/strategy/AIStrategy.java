package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.GameState;
import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * A customizable strategy that a computer can use to choose a move to play
 * in a game of Reversi.
 */
public class AIStrategy implements ReversiStrategy {
  // The list of all strategies that will be used in order to filter down the options
  // of what move to pick
  private final List<FilteringReversiStrategy> strategiesToApply;

  /**
   * Constructs an AIStrategy object with as many FilteringReversiStrategies as
   * desired in the order that they will be applied to the available choices.
   * @param strategies the methods by which the valid moves will be narrowed down
   *                   (if no filtering strategies are provided, it will pick the
   *                   upper leftmost valid move)
   */
  public AIStrategy(FilteringReversiStrategy... strategies) {
    this.strategiesToApply = new ArrayList<>();
    this.strategiesToApply.addAll(Arrays.stream(strategies).collect(Collectors.toList()));
  }

  @Override
  public RowColCoords chooseCell(ROReversiModel model, IPlayer player)
          throws IllegalStateException {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    throwIfNotPlayersTurn(model, player);
    throwIfNoLegalMoves(model);

    // Get all valid moves
    Map<RowColCoords, Integer> filteredMoves = StrategyUtils.getAllValidMoves(model);

    // Apply each strategy to the map of valid moves, refining the available options
    for (FilteringReversiStrategy strategy : this.strategiesToApply) {
      filteredMoves = strategy.filterMoves(filteredMoves, model);
    }

    // Return the upper-leftmost cell from the filtered options
    return this.chooseUpperLeft(new ArrayList<>(filteredMoves.keySet()));
  }

  /**
   * Finds the upper leftmost cell in a list of cells.
   * Being farther up is valued more than being farther to the left, so of options (0, 10)
   * and (1, 0), (0, 10) would be selected.
   * @param cells the list of cells to choose from
   * @return the upper leftmost cell
   * @throws IllegalStateException if cells is empty
   */
  private RowColCoords chooseUpperLeft(List<RowColCoords> cells) throws IllegalStateException {
    if (cells.isEmpty()) {
      throw new IllegalStateException("No moves. Must pass.");
    }

    List<RowColCoords> cellsList = cells.stream()
            // Secondary sort by leftmost column
            .sorted(Comparator.comparingInt(RowColCoords::getCol))
            // Primary sort by upper row
            .sorted(Comparator.comparingInt(RowColCoords::getRow))
            .collect(Collectors.toList());

    // Return the first of that list (upper leftmost cell)
    return cellsList.get(0);
  }


  private static void throwIfNoLegalMoves(ROReversiModel model)
          throws IllegalStateException {
    if (!model.anyLegalMoves()) {
      throw new IllegalStateException("HumanPlayer has no moves and must pass");
    }
  }

  private void throwIfNotPlayersTurn(ROReversiModel model, IPlayer player)
          throws IllegalStateException {
    if ((model.getGameState() == GameState.BLACKMOVE
            && !player.getColor().equals(DiscColor.BLACK))
            || (model.getGameState() == GameState.WHITEMOVE
            && !player.getColor().equals(DiscColor.WHITE))) {
      throw new IllegalStateException("It is not this player's turn");
    }
  }
}
