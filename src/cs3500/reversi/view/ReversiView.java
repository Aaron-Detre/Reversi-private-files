package cs3500.reversi.view;

import java.awt.Color;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * The frame for the game of Reversi, allowing the game to be viewed.
 */
public class ReversiView extends JFrame implements IReversiView {
  private final ReversiPanel panel;
  private final ROReversiModel model;

  /**
   * Constructs a ReversiView object and creates the ReversiPanel that
   * the frame will display.
   * @param model the read-only Reversi model that is being viewed
   */
  public ReversiView(ROReversiModel model) {
    this.panel = new ReversiPanel(model.getGrid());
    this.model = model;
    render();
  }

  @Override
  public void render() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel.setBackground(new Color(64, 64, 64));
    this.add(this.panel);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void rerender() {
    this.panel.deselectSelectedCell();
    this.panel.repaint();
  }

  @Override
  public void setPanelActions(PlayerActions viewActions) {
    this.panel.setActionListeners(viewActions);
  }

  @Override
  public void setSelectedCell(RowColCoords cell) throws IllegalArgumentException {
    this.panel.setSelectedCell(cell);
  }

  @Override
  public void deselectSelectedCell() {
    this.panel.deselectSelectedCell();
  }

  @Override
  public void notActivePlayerMessage() {
    JOptionPane.showMessageDialog(this, "It is not your turn.");
  }

  @Override
  public void illegalMoveMessage() {
    JOptionPane.showMessageDialog(this, "Illegal move.");
  }

  @Override
  public void yourTurnMessage() {
    JOptionPane.showMessageDialog(this, "Your turn!");
  }

  @Override
  public void endGame() throws IllegalStateException {
    if (!this.model.isGameOver()) {
      throw new IllegalStateException("Game is not over");
    }
    JOptionPane.showMessageDialog(this, gameOverMessage());
  }

  /**
   * Constructs a game over message depending on who won the game.
   * @return the completed game over message
   */
  private String gameOverMessage() {
    int blackScore = this.model.getPlayerScore(DiscColor.BLACK);
    int whiteScore = this.model.getPlayerScore(DiscColor.WHITE);
    String winner;
    if (blackScore > whiteScore) {
      winner = "Black wins!";
    } else if (whiteScore > blackScore) {
      winner = "White wins!";
    } else {
      winner = "Tie game.";
    }

    return String.format("Game Over\nBlack: %d\nWhite: %d\n%s", blackScore, whiteScore, winner);
  }

  /**
   * Returns the panel's mouse listener so that the view can be tested.
   * @return the panel's mouse listener
   */
  protected MouseInputAdapter getMouseListener() {
    return this.panel.getMouseListener();
  }

  /**
   * Returns the panel's key listener so that the view can be tested.
   * @return the panel's key listener
   */
  protected KeyListener getKeyListener() {
    return this.panel.getKeyListener();
  }
}
