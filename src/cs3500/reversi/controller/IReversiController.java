package cs3500.reversi.controller;

import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.view.IReversiView;

/**
 * Represents the controller that listens to the model and view, makes moves, and dictates
 * the flow of a game of Reversi.
 */
public interface IReversiController extends ModelCallbackListener {
  /**
   * Places the player's disc on the model at the specified cell, warns the player if it
   * isn't their turn, or warns the player that the move is illegal.
   * @param cell the row and column that the player is trying to place their disc on.
   */
  void placeDiscInGivenCell(RowColCoords cell);

  /**
   * Pass the player's turn, warns the player if it isn't their turn, or warns the player that
   * the move is illegal.
   */
  void pass();

  ROReversiModel getModel();

  IReversiView getView();
}
