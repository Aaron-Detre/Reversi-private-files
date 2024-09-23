package cs3500.reversi.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.event.MouseInputAdapter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import cs3500.reversi.controller.MockController;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.RowColCoords;

import static java.awt.event.MouseEvent.BUTTON1;
import static java.awt.event.MouseEvent.MOUSE_RELEASED;

/**
 * Tests for the view interfaces created and updated for Reversi part 3: IReversiPanel,
 * IReversiView, and PlayerActions.
 */
public class GUITests {
  StringBuilder log;
  MockView mockView;
  MockController mockController;
  PlayerActions actions;
  ROReversiModel model;
  ReversiView view;


  @Before
  public void init() {
    log = new StringBuilder();
    mockView = new MockView(new MockPanel(log), log);
    mockController = new MockController(mockView, log);
    actions = new HumanPlayerActions(mockView, mockController);
    model = ReversiModel.create(3);
    view = new ReversiView(model);
    view.setPanelActions(new HumanPlayerActions(mockView, mockController));
  }

  @Test
  public void testMessages() {
    mockView.illegalMoveMessage();
    mockView.yourTurnMessage();
    mockView.notActivePlayerMessage();
    mockView.endGame();
    String out = mockView.getLog();

    Assert.assertTrue(out.contains("not active player message delivered"));
    Assert.assertTrue(out.contains("illegal move message delivered"));
    Assert.assertTrue(out.contains("your turn message delivered"));
    Assert.assertTrue(out.contains("end game message delivered"));
  }

  @Test
  public void testMockPanel() {
    mockView.setPanelActions(new NoActions());
    mockView.setSelectedCell(new RowColCoords(1, 2));
    mockView.deselectSelectedCell();
    String out = mockView.getLog();

    Assert.assertTrue(out.contains("action listeners set"));
    Assert.assertTrue(out.contains("attempted to select the cell at row 1 and column 2"));
    Assert.assertTrue(out.contains("deselected the selected cell"));
  }

  @Test
  public void testRender() {
    mockView.render();
    mockView.rerender();
    String out = mockView.getLog();

    Assert.assertTrue(out.contains("view rendered"));
    Assert.assertTrue(out.contains("view re-rendered"));
  }

  @Test
  public void testPlayerActions() {
    actions.placeDiscInSelectedCell(new RowColCoords(5, 1));
    actions.passTurn();
    actions.selectCell(new RowColCoords(3, 4));
    actions.deselectCell();
    String out = mockController.getLog();

    Assert.assertTrue(out.contains("attempted to place disc at row 5, column 1"));
    Assert.assertTrue(out.contains("attempted to pass"));
    Assert.assertTrue(out.contains("attempted to select the cell at row 3 and column 4"));
    Assert.assertTrue(out.contains("deselected the selected cell"));
  }


  @Test
  public void testMouseReleased() {
    MouseInputAdapter mouseListener = view.getMouseListener();

    MouseEvent m1 = new MouseEvent(view, MOUSE_RELEASED, 1000, 0,
            10, 10, 1, false, BUTTON1);
    MouseEvent m2 = new MouseEvent(view, MOUSE_RELEASED, 1000, 0,
            250, 250, 1, false, BUTTON1);
    MouseEvent m3 = new MouseEvent(view, MOUSE_RELEASED, 1000, 0,
            300, 300, 1, false, BUTTON1);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Assert.fail();
    }

    mouseListener.mouseReleased(m2);
    view.setSelectedCell(new RowColCoords(2, 2));
    mouseListener.mouseReleased(m1);
    mouseListener.mouseReleased(m2);
    mouseListener.mouseReleased(m3);

    String out = mockView.getLog();

    Assert.assertTrue(out.contains("attempted to select the cell at row 2 and column 2"));
    Assert.assertTrue(out.contains("deselected the selected cell\ndeselected the selected cell"));
    Assert.assertTrue(out.contains("attempted to select the cell at row 3 and column 2"));
  }

  @Test
  public void testKeyTyped() {
    KeyListener keyListener = view.getKeyListener();

    KeyEvent enter = new KeyEvent(view, MOUSE_RELEASED, 1000, 0,
            KeyEvent.VK_ENTER);
    KeyEvent pass = new KeyEvent(view, MOUSE_RELEASED, 1000, 0,
            KeyEvent.VK_SPACE);
    KeyEvent random = new KeyEvent(view, MOUSE_RELEASED, 1000, 0,
            KeyEvent.VK_H);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      Assert.fail();
    }

    view.setSelectedCell(new RowColCoords(1, 2));
    keyListener.keyTyped(enter);
    keyListener.keyTyped(pass);
    keyListener.keyTyped(random);

    String out = mockController.getLog();

    Assert.assertTrue(out.contains("attempted to place disc at row 1, column 2"));
    Assert.assertTrue(out.contains("attempted to pass"));
  }
}
