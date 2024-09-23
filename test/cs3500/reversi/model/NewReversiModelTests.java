package cs3500.reversi.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.controller.MockController;
import cs3500.reversi.strategy.AIStrategy;
import cs3500.reversi.view.AIPlayerActions;
import cs3500.reversi.view.HumanPlayerActions;
import cs3500.reversi.view.MockPanel;
import cs3500.reversi.view.MockView;
import cs3500.reversi.view.PlayerActions;
import cs3500.reversi.view.ReversiView;

/**
 * Tests for the model interfaces created and updated for Reversi part 3: ModelCallback
 * and IPlayer.
 */
public class NewReversiModelTests {
  StringBuilder log;
  MockView mockView;
  MockController mockController;
  PlayerActions actions;
  MutableReversiModel model;
  ReversiView view;
  IPlayer ai;

  @Before
  public void init() {
    log = new StringBuilder();
    mockView = new MockView(new MockPanel(log), log);
    mockController = new MockController(mockView, log);
    actions = new HumanPlayerActions(mockView, mockController);
    model = ReversiModel.create(3);
    view = new ReversiView(model);
    view.setPanelActions(new HumanPlayerActions(mockView, mockController));
    ai = AIPlayer.create(DiscColor.BLACK, model, new AIStrategy());
    model.startGame();
  }

  @Test
  public void testModelCallback() {
    ModelCallback broadcaster = new ModelMutatedBroadcaster();
    broadcaster.addListener(mockController);
    broadcaster.modelMutated();
    broadcaster.gameOver();

    String out = mockController.getLog();

    Assert.assertTrue(out.contains("your turn message delivered"));
    Assert.assertTrue(out.contains("end game message delivered"));
  }

  @Test
  public void testAIPlayerChooseMove() {
    PlayerActions aiActions = new AIPlayerActions(mockController);

    ai.chooseMove(aiActions);

    String out = mockController.getLog();

    Assert.assertTrue(out.contains("attempted to place disc at row 0, column 1"));
  }

  @Test
  public void testPlayerIsHuman() {
    Assert.assertFalse(ai.isHuman());
    Assert.assertTrue(HumanPlayer.create(DiscColor.WHITE, model).isHuman());
  }
}
