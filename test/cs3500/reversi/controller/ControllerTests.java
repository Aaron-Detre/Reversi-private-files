package cs3500.reversi.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.GameState;
import cs3500.reversi.model.HumanPlayer;
import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.MutableReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.view.HumanPlayerActions;
import cs3500.reversi.view.MockPanel;
import cs3500.reversi.view.MockView;
import cs3500.reversi.view.PlayerActions;
import cs3500.reversi.view.ReversiView;

/**
 * Tests for the IReversiController interface.
 */
public class ControllerTests {
  StringBuilder log;
  MockView mockView;
  MockController mockController;
  PlayerActions actions;
  MutableReversiModel model;
  ReversiView view;
  IReversiController controller1;
  IReversiController controller2;
  IPlayer player1;
  IPlayer player2;

  @Before
  public void init() {
    log = new StringBuilder();
    mockView = new MockView(new MockPanel(log), log);
    mockController = new MockController(mockView, log);
    actions = new HumanPlayerActions(mockView, mockController);
    model = ReversiModel.create(3);
    view = new ReversiView(model);
    view.setPanelActions(new HumanPlayerActions(mockView, mockController));

    player1 = HumanPlayer.create(DiscColor.BLACK, model);
    player2 = HumanPlayer.create(DiscColor.WHITE, model);
    // Mock view but real controller, so that real moves get played but messages aren't output
    controller1 = new ReversiController(model, mockView, player1);
    controller2 = new ReversiController(model, mockView, player2);

    model.startGame();
  }

  @Test
  public void testMockController() {
    mockController.placeDiscInGivenCell(new RowColCoords(1, 2));
    mockController.pass();
    mockController.endGame();
    mockController.updateView();
    mockController.nextPlayerTurn();

    String out = mockController.getLog();

    Assert.assertTrue(out.contains("attempted to place disc at row 1, column 2"));
    Assert.assertTrue(out.contains("attempted to pass"));
    Assert.assertTrue(out.contains("end game message delivered"));
    Assert.assertTrue(out.contains("view re-rendered"));
    Assert.assertTrue(out.contains("your turn message delivered"));
  }

  @Test
  public void testRealController() {
    // Making an illegal move
    controller1.placeDiscInGivenCell(new RowColCoords(0, 0));
    String out1 = mockView.getLog();
    Assert.assertTrue(out1.contains("illegal move message delivered"));

    // Placing a move
    RowColCoords zeroOne = new RowColCoords(0, 1);
    RowColCoords oneTwo = new RowColCoords(1, 2);
    Assert.assertEquals(DiscColor.NONE, model.getColorAt(zeroOne));
    Assert.assertEquals(GameState.BLACKMOVE, model.getGameState());
    controller1.placeDiscInGivenCell(zeroOne);
    Assert.assertEquals(DiscColor.BLACK, model.getColorAt(zeroOne));
    Assert.assertEquals(DiscColor.BLACK, model.getColorAt(oneTwo));
    Assert.assertEquals(GameState.WHITEMOVE, model.getGameState());

    // Playing out of turn
    controller1.pass();
    String out2 = mockView.getLog();
    Assert.assertTrue(out2.contains("not active player message delivered"));

    // Passing
    Assert.assertEquals(0, model.getPassCounter());
    Assert.assertEquals(GameState.WHITEMOVE, model.getGameState());
    controller2.pass();
    Assert.assertEquals(1, model.getPassCounter());
    Assert.assertEquals(GameState.BLACKMOVE, model.getGameState());

    // Ending the game
    Assert.assertFalse(model.isGameOver());
    controller1.pass();
    Assert.assertEquals(2, model.getPassCounter());
    Assert.assertTrue(model.isGameOver());
    Assert.assertEquals(GameState.GAMEOVER, model.getGameState());
    String out3 = mockView.getLog();
    Assert.assertTrue(out3.contains("end game message delivered"));
  }
}