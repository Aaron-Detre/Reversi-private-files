package cs3500.reversi.view;

import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.model.MutableReversiModel;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * Tests for ReversiTextView.
 */
public class TextViewTests {
  @Test
  public void testNewGame3Edge() {
    MutableReversiModel model = ReversiModel.create(3);
    IReversiTextView view = new ReversiTextView(model);
    String expected1 = "  _ _ _  \n _ X O _ \n_ O _ X _\n _ X O _ \n  _ _ _  ";
    //    _ _ _
    //   _ X O _
    //  _ O _ X _
    //   _ X O _
    //    _ _ _
    String actual1 = view.toString();
    Assert.assertEquals(expected1, actual1);

    model.startGame();
    model.placeDiscInCell(new RowColCoords(1, 3));
    String expected2 = "  _ _ _  \n _ X X X \n_ O _ X _\n _ X O _ \n  _ _ _  ";
    //    _ _ _
    //   _ X X X
    //  _ O _ X _
    //   _ X O _
    //    _ _ _
    String actual2 = view.toString();
    Assert.assertEquals(expected2, actual2);

    model.placeDiscInCell(new RowColCoords(0, 1));
    String expected3 = "  _ O _  \n _ O X X \n_ O _ X _\n _ X O _ \n  _ _ _  ";
    //    _ O _
    //   _ O X X
    //  _ O _ X _
    //   _ X O _
    //    _ _ _
    String actual3 = view.toString();
    Assert.assertEquals(expected3, actual3);
  }

  @Test
  public void testNewGame4Edge() {
    MutableReversiModel model = ReversiModel.create(4);
    IReversiTextView view = new ReversiTextView(model);
    String expected = "   _ _ _ _   \n  _ _ _ _ _  \n _ _ X O _ _ \n_ _ O _ X _ _\n"
            + " _ _ X O _ _ \n  _ _ _ _ _  \n   _ _ _ _   ";
    //     _ _ _ _
    //    _ _ _ _ _
    //   _ _ X O _ _
    //  _ _ O _ X _ _
    //   _ _ X O _ _
    //    _ _ _ _ _
    //     _ _ _ _
    String actual = view.toString();
    Assert.assertEquals(expected, actual);
  }
}
