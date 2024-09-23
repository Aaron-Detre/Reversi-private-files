package cs3500.reversi.model;

import java.util.NoSuchElementException;
import java.util.Objects;

import cs3500.reversi.strategy.ReversiStrategy;
import cs3500.reversi.view.PlayerActions;

/**
 * Represents an AI player of the game Reversi.
 */
public class AIPlayer implements IPlayer {
  private final ROReversiModel model;
  private final DiscColor color;
  private final ReversiStrategy strategy;

  /**
   * Constructs an AIPlayer.
   * @param color the color of the player
   * @param model the model of the game the player is playing
   * @param strategy the strategy this player is using to choose moves
   */
  private AIPlayer(DiscColor color, ROReversiModel model, ReversiStrategy strategy) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(color);
    Objects.requireNonNull(strategy);
    this.model = model;
    this.color = color;
    this.strategy = strategy;
  }

  /**
   * Creates an AIPlayer object with the specified fields.
   * @param color the color of the player
   * @param model the model of the game the player is playing
   * @param strategy the strategy this player is using to choose moves
   * @return a new AIPlayer
   */
  public static AIPlayer create(DiscColor color, ROReversiModel model, ReversiStrategy strategy) {
    return new AIPlayer(color, model, strategy);
  }

  @Override
  public void chooseMove(PlayerActions actions) {
    RowColCoords cell;
    try {
      cell = this.strategy.chooseCell(model, this);
    // We have to add the NoSuchElementException catch due to a bug in our provider's
    // AvoidSecondRing strategy
    } catch (IllegalStateException | NoSuchElementException e) {
      actions.passTurn();
      return;
    }
    actions.placeDiscInSelectedCell(cell);
  }

  @Override
  public DiscColor getColor() {
    return this.color;
  }

  @Override
  public boolean isHuman() {
    return false;
  }
}
