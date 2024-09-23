package cs3500.reversi.controller;

/**
 * The interface for an object that can listen to the ModelCallback's broadcasts when the model
 * is mutated and can continue the flow of the game.
 */
public interface ModelCallbackListener {
  /**
   * Refreshes the IReversiView that is being controlled by this object.
   */
  void updateView();

  /**
   * Tells a human player that it's their turn or gets an AI player to choose a move.
   */
  void nextPlayerTurn();

  /**
   * Outputs a game over message telling the player who won and stops the player from
   * trying to continue making moves.
   */
  void endGame();
}
