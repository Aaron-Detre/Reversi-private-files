package cs3500.reversi.model;

import cs3500.reversi.controller.ModelCallbackListener;

/**
 * The interface for an object that can broadcast to its listeners when the model is mutated.
 */
public interface ModelCallback {
  /**
   * Adds a new ModelCallbackListener.
   * @param listener a controller that is listening to the broadcast of model mutations
   */
  void addListener(ModelCallbackListener listener);

  /**
   * Removes a ModelCallbackListener.
   * @param listener a controller that is no longer listening to the broadcast
   *                 of model mutations
   */
  void removeListener(ModelCallbackListener listener);

  /**
   * Broadcasts to its listeners that a move has been made.
   */
  void modelMutated();

  /**
   * Broadcasts to its listeners that the game has ended.
   */
  void gameOver();
}
