package cs3500.reversi.model;

import java.util.Objects;

import cs3500.reversi.view.PlayerActions;

/**
 * Represents a human player of the game Reversi.
 */
public class HumanPlayer implements IPlayer {
  // A HumanPlayer's color can only be black or white
  // Class Invariant: color != Color.NONE
  private final DiscColor color;

  // The game that the player is playing
  // Class Invariant: game != null
  private final ROReversiModel game;

  /**
   * Constructs a new HumanPlayer.
   * @param color the color of discs that this player will use
   * @param game the game this player is playing
   * @throws IllegalArgumentException if the color is not black or white
   */
  private HumanPlayer(DiscColor color, ROReversiModel game)
          throws IllegalArgumentException {
    Objects.requireNonNull(game);
    if (color != DiscColor.BLACK && color != DiscColor.WHITE)  {
      throw new IllegalArgumentException("Invalid player color");
    }
    this.color = color;
    this.game = game;
  }

  /**
   * Creates a HumanPlayer object.
   * @param color the color of discs that this player will use
   * @param game the game this player is playing
   * @return a new HumanPlayer
   * @throws IllegalArgumentException if the color is not black or white
   */
  public static HumanPlayer create(DiscColor color, ROReversiModel game)
          throws IllegalArgumentException {
    return new HumanPlayer(color, game);
  }

  @Override
  public void chooseMove(PlayerActions actions) {
    // Do nothing because human players choose their moves using the view
  }

  @Override
  public DiscColor getColor() {
    return this.color;
  }

  @Override
  public boolean isHuman() {
    return true;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof HumanPlayer)) {
      return false;
    }
    HumanPlayer that = (HumanPlayer) other;
    return this.color == that.color
            && this.game.equals(that.game);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color);
  }
}
