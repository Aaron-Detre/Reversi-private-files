package cs3500.reversi.controller;

import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.MutableReversiModel;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.view.AIPlayerActions;
import cs3500.reversi.view.IReversiView;
import cs3500.reversi.view.NoActions;
import cs3500.reversi.view.PlayerActions;
import cs3500.reversi.view.HumanPlayerActions;

/**
 * An implementation of a controller for the game of Reversi that allows the game to
 * be played.
 */
public class ReversiController implements IReversiController {
  private final MutableReversiModel model;
  private final IReversiView view;
  private final IPlayer player;
  private final PlayerActions actions;
  private boolean gameOver;

  /**
   * Constructs a ReversiController, determines the actions the player can make both on
   * the view and just in general, and subscribes itself as a listener to the broadcaster
   * in the model.
   * @param model the model for the game being played using this controller
   * @param view the graphical view for the game being played using this controller
   * @param player one of the two players in the game being played using this controller
   */
  public ReversiController(MutableReversiModel model, IReversiView view, IPlayer player) {
    this.model = model;
    this.view = view;
    this.player = player;
    this.gameOver = false;
    this.model.setBroadcastListener(this);

    // All the actions the player can make
    this.actions = getPlayerActions();

    // Just the actions that the player can make on the view (AI can't click on the
    // view, so no actions)
    this.view.setPanelActions(this.player.isHuman() ? this.actions : new NoActions());
  }

  /**
   * Determines the actions this controller's player can make in the game depending on
   * whether they are human or AI.
   * @return the PlayerActions describing the actions that the player can make in the game.
   */
  private PlayerActions getPlayerActions() {
    if (this.player.isHuman()) {
      return new HumanPlayerActions(this.view, this);
    } else {
      return new AIPlayerActions(this);
    }
  }

  @Override
  public void updateView() {
    this.view.rerender();
  }

  @Override
  public void nextPlayerTurn() {
    if (this.gameOver) {
      return;
    }
    if (thisPlayersTurn()) {
      if (this.player.isHuman()) {
        this.view.yourTurnMessage();
      } else {
        this.player.chooseMove(this.actions);
      }
    }
  }

  @Override
  public void placeDiscInGivenCell(RowColCoords coords) {
    try {
      if (!warnIfNotActivePlayer()) {
        this.model.placeDiscInCell(coords);
      }
    } catch (IllegalStateException e) {
      this.view.illegalMoveMessage();
    }
  }

  @Override
  public void pass() {
    try {
      if (!warnIfNotActivePlayer()) {
        this.model.pass();
      }
    } catch (IllegalStateException e) {
      this.view.illegalMoveMessage();
    }
  }

  @Override
  public ROReversiModel getModel() {
    return this.model;
  }

  @Override
  public IReversiView getView() {
    return this.view;
  }

  @Override
  public void endGame() {
    this.view.endGame();
    this.gameOver = true;
    // Disable any actions on the view when the game has ended
    this.view.setPanelActions(new NoActions());
  }

  /**
   * Tells the view to warn the player if they are trying to play when it isn't their turn.
   * @return true if the player was warned and false if they weren't
   */
  private boolean warnIfNotActivePlayer() {
    if (!thisPlayersTurn()) {
      this.view.notActivePlayerMessage();
      return true;
    }
    return false;
  }

  /**
   * Determines whether this controller's player is the model's active player.
   * @return true iff it is the player's turn
   */
  private boolean thisPlayersTurn() {
    return this.model.getActivePlayerColor().equals(this.player.getColor());
  }
}
