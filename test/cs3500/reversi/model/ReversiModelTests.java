package cs3500.reversi.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Tests for the various classes and methods of Reversi's model.
 */
public class ReversiModelTests {
  MutableReversiModel m2;
  MutableReversiModel m3;
  MutableReversiModel gameoverModel;
  HumanPlayer blackM2;
  HumanPlayer whiteM2;
  HumanPlayer blackM3;
  HumanPlayer whiteM3;
  RowColCoords tl;

  @Before
  public void init() {
    m2 = ReversiModel.create(2);
    m3 = ReversiModel.create(3);
    m2.startGame();
    m3.startGame();
    blackM2 = HumanPlayer.create(DiscColor.BLACK, m2);
    blackM3 = HumanPlayer.create(DiscColor.BLACK, m3);
    whiteM2 = HumanPlayer.create(DiscColor.WHITE, m2);
    whiteM3 = HumanPlayer.create(DiscColor.WHITE, m3);
    tl = new RowColCoords(0, 0);
    gameoverModel = ReversiModel.create(6);
    gameoverModel.startGame();
    gameoverModel.pass();
    gameoverModel.pass();
  }

  @Test
  public void testEqualModels() {
    MutableReversiModel m2_2 = ReversiModel.create(2);
    m2_2.startGame();
    Assert.assertEquals(m2, m2_2);
    Assert.assertNotEquals(m2, m3);
  }

  @Test
  public void testEqualPlayers() {
    HumanPlayer p1 = HumanPlayer.create(DiscColor.BLACK, m2);
    HumanPlayer p2 = HumanPlayer.create(DiscColor.BLACK, m2);
    HumanPlayer p3 = HumanPlayer.create(DiscColor.BLACK, m3);
    HumanPlayer p4 = HumanPlayer.create(DiscColor.WHITE, m2);
    Assert.assertEquals(p1, p2);
    Assert.assertNotEquals(p1, p3);
    Assert.assertNotEquals(p1, p4);
  }

  @Test
  public void testEqualCoords() {
    RowColCoords c1 = new RowColCoords(1, 1);
    RowColCoords c2 = new RowColCoords(1, 1);
    RowColCoords c3 = new RowColCoords(1, 2);
    Assert.assertEquals(c1, c2);
    Assert.assertNotEquals(c1, c3);
  }

  @Test
  public void testGetGameState() {
    // Checking that the game starts with black's move
    Assert.assertEquals(GameState.BLACKMOVE, m3.getGameState());

    m3.pass();

    // Checking that the game state is white's move after black's turn
    Assert.assertEquals(GameState.WHITEMOVE, m3.getGameState());

    m3.pass();

    // Checking that the game state is game over when both players pass
    Assert.assertEquals(GameState.GAMEOVER, m3.getGameState());
  }

  @Test
  public void testIsGameOver() {
    // Checking that even though there are no moves to play, the game hasn't ended
    Assert.assertFalse(m2.isGameOver());

    m2.pass();
    m2.pass();

    // Checking that the game is over after two passes in a row
    Assert.assertTrue(m2.isGameOver());
  }

  @Test
  public void testGetGrid() {
    // Checking that the grid is set up and returned properly
    List<DiscColor> row1 = Arrays.asList(DiscColor.BLACK, DiscColor.WHITE);
    List<DiscColor> row2 = Arrays.asList(DiscColor.WHITE, DiscColor.NONE, DiscColor.BLACK);
    List<DiscColor> row3 = Arrays.asList(DiscColor.BLACK, DiscColor.WHITE);
    List<List<DiscColor>> expectedGrid = Arrays.asList(row1, row2, row3);
    Assert.assertEquals(expectedGrid, m2.getGrid());
  }

  @Test
  public void testGetActivePlayer() {
    // Checking that the players are set up and the active player
    // is returned properly
    Assert.assertEquals(m2.getActivePlayerColor(), DiscColor.BLACK);
    m2.pass();
    Assert.assertEquals(m2.getActivePlayerColor(), DiscColor.WHITE);
  }

  @Test
  public void testGetPlayerScore() {
    // Checking the scores before any move is played
    Assert.assertEquals(3, m3.getPlayerScore(DiscColor.BLACK));
    Assert.assertEquals(3, m3.getPlayerScore(DiscColor.WHITE));

    m3.placeDiscInCell(new RowColCoords(1, 3));
    /*
      _ _ _         _ _ _
     _ X O _       _ X X X
    _ O _ X _ --> _ O _ X _
     _ X O _       _ X O _
      _ _ _         _ _ _
    */

    // Checking the scores after a move is played
    Assert.assertEquals(5, m3.getPlayerScore(DiscColor.BLACK));
    Assert.assertEquals(2, m3.getPlayerScore(DiscColor.WHITE));
  }

  @Test
  public void testGetColorAt() {
    RowColCoords colorNone = new RowColCoords(1, 1);
    RowColCoords colorWhite = new RowColCoords(1, 0);
    RowColCoords colorBlack = tl;
    Assert.assertEquals(DiscColor.NONE, m2.getColorAt(colorNone));
    Assert.assertEquals(DiscColor.WHITE, m2.getColorAt(colorWhite));
    Assert.assertEquals(DiscColor.BLACK, m2.getColorAt(colorBlack));
  }

  @Test
  public void testGetWinner() {
    // Checking a tie game has no winning color
    m2.pass();
    m2.pass();
    Assert.assertEquals(DiscColor.NONE, m2.getWinner());

    // Checking a game where black has more pieces has black as the winner
    m3.placeDiscInCell(new RowColCoords(1, 3));
    m3.pass();
    m3.pass();
    Assert.assertEquals(DiscColor.BLACK, m3.getWinner());
  }

  @Test
  public void testPlaceDiscInCell() {
    RowColCoords oneThree = new RowColCoords(1, 3);
    RowColCoords oneTwo = new RowColCoords(1, 2);

    // Checking the colors of (1, 2) and (1, 3) before the move is made
    Assert.assertEquals(DiscColor.NONE, m3.getColorAt(oneThree));
    Assert.assertEquals(DiscColor.WHITE, m3.getColorAt(oneTwo));

    m3.placeDiscInCell(new RowColCoords(1, 3));

    // Checking the colors of (1, 2) and (1, 3) after the move is made
    Assert.assertEquals(DiscColor.BLACK, m3.getColorAt(oneThree));
    Assert.assertEquals(DiscColor.BLACK, m3.getColorAt(oneTwo));
  }

  @Test
  public void testPassCounterResets() {
    m3.pass();
    m3.placeDiscInCell(new RowColCoords(0, 1));
    m3.pass();
    Assert.assertFalse(m3.isGameOver());
    m3.pass();
    Assert.assertTrue(m3.isGameOver());
  }

  // Not sure if these tests are meaningful at all
  @Test
  public void testNoBadMutation() {
    // Checking getGameState is not mutable
    GameState state = m2.getGameState();
    state = GameState.GAMEOVER;
    Assert.assertNotEquals(GameState.GAMEOVER, m2.getGameState());

    // Checking getGrid is not mutable
    List<List<DiscColor>> grid = m3.getGrid();
    grid.clear();
    Assert.assertNotEquals(new ArrayList<>(), m3.getGrid());

    // Checking getActivePlayer is not mutable
    DiscColor active = m2.getActivePlayerColor();
    active = DiscColor.WHITE;
    Assert.assertNotEquals(DiscColor.WHITE, m2.getActivePlayerColor());

    // Checking getPlayerScore is not mutable
    int score = m2.getPlayerScore(DiscColor.BLACK);
    score = 100;
    Assert.assertNotEquals(100, m2.getPlayerScore(DiscColor.BLACK));

    // Checking getColorAt is not mutable
    DiscColor color = m2.getColorAt(tl);
    color = DiscColor.NONE;
    Assert.assertNotEquals(DiscColor.NONE, m2.getColorAt(tl));

    // Checking getWinner is not mutable
    m2.pass();
    m2.pass();
    DiscColor winner = m2.getWinner();
    winner = DiscColor.BLACK;
    Assert.assertNotEquals(DiscColor.BLACK, m2.getWinner());
  }

  @Test
  public void testAllExceptions() {
    RowColCoords invalid1 = new RowColCoords(-1, 0);
    RowColCoords invalid2 = new RowColCoords(0, -1);
    RowColCoords invalid3 = new RowColCoords(10, 0);
    RowColCoords invalid4 = new RowColCoords(0, 10);

    Assert.assertThrows(IllegalStateException.class,
        gameoverModel::pass); // Trying to pass after game is over

    // Checking placeDiscInCell exceptions
    // Trying to place after game is over
    Assert.assertThrows(IllegalStateException.class,
        () -> gameoverModel.placeDiscInCell(new RowColCoords(2, 2)));
    // Trying to place in invalid cell
    Assert.assertThrows(IllegalArgumentException.class,
        () -> m3.placeDiscInCell(new RowColCoords(-1, 2)));
    // Trying to make an invalid move
    Assert.assertThrows(IllegalStateException.class,
        () -> m3.placeDiscInCell(new RowColCoords(2, 2)));

    // Checking getActivePlayer exceptions
    Assert.assertThrows(IllegalStateException.class,
        gameoverModel::getActivePlayerColor); // Trying to get active player after game is over

    // Checking getColorAt exceptions
    Assert.assertThrows(IllegalArgumentException.class,
        () -> m3.getColorAt(invalid1)); // Trying to get color at an invalid cell
    Assert.assertThrows(IllegalArgumentException.class,
        () -> m3.getColorAt(invalid2)); // Trying to get color at an invalid cell
    Assert.assertThrows(IllegalArgumentException.class,
        () -> m3.getColorAt(invalid3)); // Trying to get color at an invalid cell
    Assert.assertThrows(IllegalArgumentException.class,
        () -> m3.getColorAt(invalid4)); // Trying to get color at an invalid cell

    // Checking getWinner exceptions
    Assert.assertThrows(IllegalStateException.class, m3::getWinner);
    // Trying to get winner after game is started but before the game is over
  }

  @Test
  public void testAnyLegalMoves() {
    Assert.assertTrue(m3.anyLegalMoves());

    m3.placeDiscInCell(new RowColCoords(0, 1));
    m3.placeDiscInCell(new RowColCoords(4, 1));
    m3.placeDiscInCell(new RowColCoords(3, 0));
    m3.placeDiscInCell(new RowColCoords(1, 3));
    m3.placeDiscInCell(new RowColCoords(3, 3));
    m3.placeDiscInCell(new RowColCoords(1, 0));

    Assert.assertFalse(m3.anyLegalMoves());
    m3.pass();
    Assert.assertFalse(m3.anyLegalMoves());
    m3.pass();
    Assert.assertTrue(m3.isGameOver());
    Assert.assertThrows(IllegalStateException.class, m3::anyLegalMoves);
  }

  @Test
  public void testValidMove() {
    Map<Boolean, Integer> capturesOne = m3.validMove(new RowColCoords(0, 1));
    Assert.assertTrue(capturesOne.containsKey(true));
    Assert.assertTrue(capturesOne.containsValue(1));

    Map<Boolean, Integer> illegalMove = m3.validMove(new RowColCoords(2, 2));
    Assert.assertFalse(illegalMove.containsKey(true));
    Assert.assertTrue(illegalMove.containsValue(0));

    Assert.assertThrows(IllegalArgumentException.class,
        () -> m3.validMove(new RowColCoords(-1, -1)));

    m3.pass();
    m3.pass();
    Assert.assertThrows(IllegalStateException.class,
        () -> m3.validMove(new RowColCoords(0, 0)));
  }
}