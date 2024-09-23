package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;

import cs3500.reversi.controller.ModelCallbackListener;

/**
 * An implementation of a ModelCallback whose primary purpose is to notify its listeners
 * when the model is mutated.
 */
public class ModelMutatedBroadcaster implements ModelCallback {
  private final List<ModelCallbackListener> listeners;

  /**
   * Constructs a new ModelMutatedBroadcaster object with an empty list of listeners.
   */
  public ModelMutatedBroadcaster() {
    this.listeners = new ArrayList<>();
  }

  @Override
  public void addListener(ModelCallbackListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeListener(ModelCallbackListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public void modelMutated() {
    updateListenerViews();
    nextPlayerTurn();
  }

  @Override
  public void gameOver() {
    for (ModelCallbackListener listener : this.listeners) {
      listener.endGame();
    }
  }

  /**
   * Updates the views of all listeners.
   */
  private void updateListenerViews() {
    for (ModelCallbackListener listener : this.listeners) {
      listener.updateView();
    }
  }

  /**
   * Signals all the listeners that a new turn has started.
   */
  private void nextPlayerTurn() {
    for (ModelCallbackListener listener : this.listeners) {
      listener.nextPlayerTurn();
    }
  }
}
