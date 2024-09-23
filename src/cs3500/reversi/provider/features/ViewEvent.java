package cs3500.reversi.provider.features;

import cs3500.reversi.provider.model.CubicPosn;

/**
 * Represents a view event listener with the capability to add itself to a data structure.
 * This is a features interface.
 */
public interface ViewEvent {

  /**
   * adds a view listener to this.
   * @param listener the listener
   */
  void addViewEventListener(ViewEventListener listener);

  /**
   * represents a listener for events in the view to relay commands to other structures.
   */
  interface ViewEventListener {

    /**
     * syncs data structures (player on correct turn) to corresponding commands to placing a piece.
     * @param posn the hex to place a piece on
     */
    void onMove(CubicPosn posn);

    /**
     * syncs data structures (model) to corresponding mutation upon passing.
     */
    void onPass();
  }
}
