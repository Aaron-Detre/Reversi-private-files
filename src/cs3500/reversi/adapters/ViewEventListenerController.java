package cs3500.reversi.adapters;

import cs3500.reversi.controller.IReversiController;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.provider.features.ViewEvent;
import cs3500.reversi.provider.model.CubicPosn;
import cs3500.reversi.view.IReversiView;

/**
 * A decorator class for an IReversiController that is also a ViewEventListener, meaning it
 * can play on our provider's view.
 */
public class ViewEventListenerController
        implements IReversiController, ViewEvent.ViewEventListener {
  private IReversiController controller;

  /**
   * Constructor for ViewEventListenerController. It isn't given a controller yet because
   * this has to be created before the controller this class is decorating is actually created
   * since controllers need to be initialized with a view and the view can't be
   */
  public ViewEventListenerController() {
    // Default constructor
  }

  /**
   * Sets the controller that this class is decorating.
   * @param controller the controller in charge of our provider's view
   */
  public void setController(IReversiController controller) {
    this.controller = controller;
  }

  @Override
  public void placeDiscInGivenCell(RowColCoords cell) {
    this.controller.placeDiscInGivenCell(cell);
  }

  @Override
  public void pass() {
    this.controller.pass();
  }

  @Override
  public ROReversiModel getModel() {
    return this.controller.getModel();
  }

  @Override
  public IReversiView getView() {
    return this.controller.getView();
  }

  @Override
  public void updateView() {
    this.controller.updateView();
  }

  @Override
  public void nextPlayerTurn() {
    this.controller.nextPlayerTurn();
  }

  @Override
  public void endGame() {
    this.controller.endGame();
  }

  @Override
  public void onMove(CubicPosn posn) {
    placeDiscInGivenCell(new AdapterUtils().cubicToRowCol(posn, this.controller.getModel()));
  }

  @Override
  public void onPass() {
    pass();
  }
}
