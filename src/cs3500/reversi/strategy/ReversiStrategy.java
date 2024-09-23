package cs3500.reversi.strategy;

import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * High-level strategies for the game Reversi to choose a move to play.
 */
public interface ReversiStrategy {
  /**
   * Chooses a cell to place the player's disc at using some strategy.
   * @param model the model for the game being played
   * @param player the player who is choosing a move
   * @return the cell chosen to be played by the active player
   * @throws IllegalStateException if the player is not the active player or has no valid moves
   */
  RowColCoords chooseCell(ROReversiModel model, IPlayer player) throws IllegalStateException;
}
