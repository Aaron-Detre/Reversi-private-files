package cs3500.reversi.model;

import cs3500.reversi.view.PlayerActions;

/**
 * Represents either a human or AI player for the game Reversi.
 */
public interface IPlayer {
  /**
   * Chooses a move that an AI player will play, but does nothing if the player is human since
   * human players choose moves using the view.
   * @param actions the actions that a player can make in the game
   */
  void chooseMove(PlayerActions actions);

  /**
   * Gets the player's color.
   * @return either black or white
   */
  DiscColor getColor();

  /**
   * Determines whether the player is a human or an AI.
   * @return true iff the player is human
   */
  boolean isHuman();
}
