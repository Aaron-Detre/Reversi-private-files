package cs3500.reversi.adapters;

import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.provider.features.ViewEvent;
import cs3500.reversi.provider.model.DiskState;
import cs3500.reversi.provider.view.IView;
import cs3500.reversi.provider.view.ReversiGraphicsView;
import cs3500.reversi.view.IReversiView;
import cs3500.reversi.view.PlayerActions;

/**
 * Adapting our provider's view to our view interface.
 */
public class ViewAdapter implements IReversiView {
  private final IView providerView;

  public ViewAdapter(ROReversiModel model) {
    this.providerView = new ReversiGraphicsView(new ModelAdapter(model));
    render();
  }

  public void addProviderViewEventListener(ViewEvent.ViewEventListener listener) {
    this.providerView.addViewEventListener(listener);
  }

  @Override
  public void render() {
    this.providerView.setVisible(true);
    // I'm guessing this updatePlayer method is supposed to be used in the controller where it
    // knows which player is playing on which view, but this class doesn't know about players
    // and there's no equivalent in our code. Only player2 uses this view, so I'm just
    // hard coding it to white.
    this.providerView.updatePlayer(DiskState.WHITE);
  }

  @Override
  public void rerender() {
    this.providerView.updateScore(DiskState.BLACK);
    this.providerView.updateScore(DiskState.WHITE);
    this.providerView.refresh();
  }

  @Override
  public void setPanelActions(PlayerActions viewActions) {
    // Actions are set automatically in the constructor for their view (ReversiGraphicsView)
  }

  @Override
  public void setSelectedCell(RowColCoords cell) throws IllegalArgumentException {
    // No equivalent method in their view
  }

  @Override
  public void deselectSelectedCell() {
    // No equivalent method in their view
  }

  @Override
  public void endGame() throws IllegalStateException {
    // No equivalent method in their view
  }

  @Override
  public void notActivePlayerMessage() {
    this.providerView.showError("It is not your turn.");
  }

  @Override
  public void illegalMoveMessage() {
    this.providerView.showError("Illegal move.");
  }

  @Override
  public void yourTurnMessage() {
    // No equivalent method in their view
  }
}
