package cs3500.reversi.model;

import cs3500.reversi.controller.ModelCallbackListener;

/**
 * Represents an object that has a ModelCallback object and can add or remove controllers
 * as listeners to it.
 */
public interface ModelBroadcaster {
  /**
   * Adds a new ModelCallbackListener to the object's ModelCallback object.
   * @param controller a controller that is listening to the broadcast of model mutations
   */
  void setBroadcastListener(ModelCallbackListener controller);

  /**
   * Removes a ModelCallbackListener from the object's ModelCallback object.
   * @param controller a controller that is no longer listening to the broadcast
   *                   of model mutations
   */
  void removeBroadcastListener(ModelCallbackListener controller);
}
