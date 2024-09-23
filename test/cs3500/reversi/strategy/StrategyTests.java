package cs3500.reversi.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.reversi.model.CoordinateTranscriptMock;
import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.ForcedMoveMock;
import cs3500.reversi.model.GameState;
import cs3500.reversi.model.HumanPlayer;
import cs3500.reversi.model.IPlayer;
import cs3500.reversi.model.MutableReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * Tests for the strategies a computer could use to play Reversi.
 */
public class StrategyTests {
  MutableReversiModel tinyModel;
  MutableReversiModel smallModel;
  MutableReversiModel mediumModel;
  IPlayer p1Tiny;
  IPlayer p2Tiny;
  IPlayer p1Small;
  IPlayer p2Small;
  IPlayer p1Med;
  IPlayer p2Med;
  FilteringReversiStrategy maxCapture;
  FilteringReversiStrategy avoidNextToCorners;
  FilteringReversiStrategy playToCorners;
  FilteringReversiStrategy minimizeOpponentMaxCapture;
  ReversiStrategy upperLeftStrategy;
  ReversiStrategy maxCaptureStrategy;
  ReversiStrategy avoidNextToCornersStrategy;
  ReversiStrategy playToCornersStrategy;
  ReversiStrategy miniMaxStrategy;
  ReversiStrategy combinedStrategy1;
  ReversiStrategy combinedStrategy2;
  ReversiStrategy combinedStrategy3;

  @Before
  public void init() {
    tinyModel = ReversiModel.create(2);
    smallModel = ReversiModel.create(3);
    mediumModel = ReversiModel.create(4);
    p1Tiny = HumanPlayer.create(DiscColor.BLACK, tinyModel);
    p2Tiny = HumanPlayer.create(DiscColor.WHITE, tinyModel);
    p1Small = HumanPlayer.create(DiscColor.BLACK, smallModel);
    p2Small = HumanPlayer.create(DiscColor.WHITE, smallModel);
    p1Med = HumanPlayer.create(DiscColor.BLACK, mediumModel);
    p2Med = HumanPlayer.create(DiscColor.WHITE, mediumModel);
    maxCapture = new MaxCapture();
    avoidNextToCorners = new AvoidNextToCorners();
    playToCorners = new PlayToCorners();
    minimizeOpponentMaxCapture = new MinimizeOpponentMaxCapture();
    upperLeftStrategy = new AIStrategy();
    maxCaptureStrategy = new AIStrategy(maxCapture);
    avoidNextToCornersStrategy = new AIStrategy(avoidNextToCorners);
    playToCornersStrategy = new AIStrategy(playToCorners);
    miniMaxStrategy = new AIStrategy(minimizeOpponentMaxCapture);
    combinedStrategy1 = new AIStrategy(playToCorners, avoidNextToCorners, maxCapture);
    combinedStrategy2 = new AIStrategy(minimizeOpponentMaxCapture, maxCapture);
    combinedStrategy3
        = new AIStrategy(playToCorners, avoidNextToCorners, minimizeOpponentMaxCapture);
  }

  @Test
  public void testCoordinateTranscriptMaxCapture() {
    List<String> log = coordinateTranscriptMockTestHelper(maxCaptureStrategy, 4);
    String gridLog = log.get(0);
    String validityCheckLog = log.get(1);

    // For maxCaptureStrategy, every cell of the grid should be checked for validity
    Assert.assertEquals(gridLog, validityCheckLog);
  }

  @Test
  public void testCoordinateTranscriptAvoidNextToCorners() {
    List<String> log = coordinateTranscriptMockTestHelper(avoidNextToCornersStrategy, 4);
    String gridLog = log.get(0);
    String validityCheckLog = log.get(1);

    // For avoidNextToCornersStrategy, at least every cell of the grid that isn't next to a
    // corner should be checked for validity (every cell that's next to a corner is also checked,
    // but that's not important for this test)
    Assert.assertEquals(gridLog, validityCheckLog);
  }

  @Test
  public void testCoordinateTranscriptPlayToCorners() {
    List<String> log = coordinateTranscriptMockTestHelper(playToCornersStrategy, 3);
    String validityCheckLog = log.get(1);

    // For playToCornersStrategy, every corner cell of the grid should be checked for validity
    Assert.assertTrue(validityCheckLog.contains("0 0")); // top left
    Assert.assertTrue(validityCheckLog.contains("0 2")); // top right
    Assert.assertTrue(validityCheckLog.contains("2 0")); // center left
    Assert.assertTrue(validityCheckLog.contains("2 4")); // center right
    Assert.assertTrue(validityCheckLog.contains("4 0")); // bottom left
    Assert.assertTrue(validityCheckLog.contains("4 2")); // bottom right
  }

  @Test
  public void testCoordinateTranscriptCombineStrategy1() {
    List<String> log = coordinateTranscriptMockTestHelper(combinedStrategy1, 5);
    String gridLog = log.get(0);
    String validityCheckLog = log.get(1);

    // For combine strategy which combines playToCorners, avoidNextToCorners, and maxCapture,
    // every cell of the grid should be checked for validity
    Assert.assertEquals(gridLog, validityCheckLog);
  }

  /*
   I don't know how to test this for any strategy using minimizeOpponentMaxCapture because
   it creates an actual ReversiModel copy of the mock and then tries to place the mock moves
   on it which obviously doesn't work. I think we would have to change our implementation to get
   it to work with the mock which seems kind of backwards?
  */

  /**
   * Helper method for the CoordinateTranscriptMock that runs chooseCell with the given
   * strategy on a mock.
   * @param strategy the strategy being applied to the mock
   * @param gridEdgeLength the edge length of the grid being simulated by the mock
   * @return a list containing one string with what was appended to the log in the getGrid
   *         method and one string with what was appended in the validMove methods
   */
  private List<String> coordinateTranscriptMockTestHelper(
          ReversiStrategy strategy, int gridEdgeLength) {
    StringBuilder log = new StringBuilder();
    MutableReversiModel mock = new CoordinateTranscriptMock(gridEdgeLength, log);
    IPlayer mockPlayer = HumanPlayer.create(DiscColor.BLACK, mock);
    strategy.chooseCell(mock, mockPlayer);
    String[] lines = log.toString().split("\n");
    return List.of(lines[0], lines[1]);
  }

  @Test
  public void testForcedMoveMock() {
    RowColCoords centerCell = new RowColCoords(3, 3);
    MutableReversiModel mock = new ForcedMoveMock(4, centerCell);
    mock.startGame();
    RowColCoords maxCapture = this.maxCaptureStrategy.chooseCell(mock,
            HumanPlayer.create(DiscColor.BLACK, mock));

    // Trying to place disc on the center cell which would not be valid regularly
    Assert.assertEquals(centerCell, maxCapture);
  }

  @Test
  public void testNoMoves() {
    Assert.assertThrows(IllegalStateException.class,
        () -> upperLeftStrategy.chooseCell(tinyModel, p1Tiny));
    Assert.assertThrows(IllegalStateException.class,
        () -> maxCaptureStrategy.chooseCell(tinyModel, p1Tiny));
    Assert.assertThrows(IllegalStateException.class,
        () -> combinedStrategy1.chooseCell(tinyModel, p1Tiny));
  }

  @Test
  public void testNotActivePlayer() {
    Assert.assertEquals(GameState.UNSTARTED, smallModel.getGameState());
    Assert.assertThrows(IllegalStateException.class,
        () -> upperLeftStrategy.chooseCell(smallModel, p2Small));
    Assert.assertThrows(IllegalStateException.class,
        () -> maxCaptureStrategy.chooseCell(smallModel, p2Small));
    Assert.assertThrows(IllegalStateException.class,
        () -> combinedStrategy1.chooseCell(smallModel, p2Small));

    smallModel.startGame();
    smallModel.placeDiscInCell(new RowColCoords(1, 3));

    Assert.assertEquals(GameState.WHITEMOVE, smallModel.getGameState());
    Assert.assertThrows(IllegalStateException.class,
        () -> avoidNextToCornersStrategy.chooseCell(smallModel, p1Small));
  }

  @Test
  public void testNullsCaught() {
    Assert.assertThrows(NullPointerException.class,
        () -> upperLeftStrategy.chooseCell(null, p1Small));
    Assert.assertThrows(NullPointerException.class,
        () -> combinedStrategy2.chooseCell(smallModel, null));
  }
}