package cs3500.reversi.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * General use methods for Reversi strategies.
 */
public class StrategyUtils {
  /**
   * Finds the coordinates of all the corner cells in the grid.
   * @param model some game of Reversi
   * @return the list of all corner cells
   */
  public static List<RowColCoords> getCornerCells(ROReversiModel model) {
    return StrategyUtils.getCornersAndAdjacentCells(model).subList(0, 6);
  }

  /**
   * Finds the coordinates of all the cells directly adjacent to corner cells in the grid.
   * @param model some game of Reversi
   * @return the list of all cells adjacent to corner cells
   */
  public static List<RowColCoords> getCellsAdjacentToCorners(ROReversiModel model) {
    return StrategyUtils.getCornersAndAdjacentCells(model).subList(6, 24);
  }

  /**
   * Finds all the valid moves the active player has available to them.
   * @param model some game of Reversi
   * @return a map of the coordinates of each valid move to the number of discs that
   *         would be captured by placing in that cell
   */
  public static Map<RowColCoords, Integer> getAllValidMoves(ROReversiModel model) {
    List<List<DiscColor>> grid = model.getGrid();
    Map<RowColCoords, Integer> moves = new HashMap<>();

    // Put every valid move's row/col of the grid into the map along with its point value
    for (int row = 0; row < grid.size(); row++) {
      for (int col = 0; col < grid.get(row).size(); col++) {
        RowColCoords cell = new RowColCoords(row, col);
        Map<Boolean, Integer> valid = model.validMove(cell);
        if (valid.containsKey(true)) {
          moves.put(cell, valid.get(true));
        }
      }
    }

    return moves;
  }

  /**
   * Finds all the corner cells and all the cells adjacent to those cells in the grid.
   * @param model some game of Reversi
   * @return the list of all those cells
   */
  private static List<RowColCoords> getCornersAndAdjacentCells(ROReversiModel model) {
    int edgeLength = model.getEdgeLength();

    // Corner cells and their 3 adjacent cells
    RowColCoords tl = new RowColCoords(0, 0);
    RowColCoords tl1 = new RowColCoords(0, 1);
    RowColCoords tl2 = new RowColCoords(1, 0);
    RowColCoords tl3 = new RowColCoords(1, 1);

    RowColCoords tr = new RowColCoords(0, edgeLength - 1);
    RowColCoords tr1 = new RowColCoords(0, edgeLength - 2);
    RowColCoords tr2 = new RowColCoords(1, edgeLength - 1);
    RowColCoords tr3 = new RowColCoords(1, edgeLength);

    RowColCoords cl = new RowColCoords(edgeLength - 1, 0);
    RowColCoords cl1 = new RowColCoords(edgeLength - 2, 0);
    RowColCoords cl2 = new RowColCoords(edgeLength - 1, 1);
    RowColCoords cl3 = new RowColCoords(edgeLength, 0);

    RowColCoords cr = new RowColCoords(edgeLength - 1, edgeLength * 2 - 2);
    RowColCoords cr1 = new RowColCoords(edgeLength - 2, edgeLength * 2 - 3);
    RowColCoords cr2 = new RowColCoords(edgeLength - 1, edgeLength * 2 - 3);
    RowColCoords cr3 = new RowColCoords(edgeLength, edgeLength * 2 - 3);

    RowColCoords bl = new RowColCoords(edgeLength * 2 - 2, 0);
    RowColCoords bl1 = new RowColCoords(edgeLength * 2 - 3, 0);
    RowColCoords bl2 = new RowColCoords(edgeLength * 2 - 3, 1);
    RowColCoords bl3 = new RowColCoords(edgeLength * 2 - 2, 1);

    RowColCoords br = new RowColCoords(edgeLength * 2 - 2, edgeLength - 1);
    RowColCoords br1 = new RowColCoords(edgeLength * 2 - 3, edgeLength);
    RowColCoords br2 = new RowColCoords(edgeLength * 2 - 3, edgeLength - 1);
    RowColCoords br3 = new RowColCoords(edgeLength * 2 - 2, edgeLength - 2);

    return new ArrayList<>(List.of(tl, tr, cl, cr, bl, br,
            tl1, tl2, tl3,
            tr1, tr2, tr3,
            cl1, cl2, cl3,
            cr1, cr2, cr3,
            bl1, bl2, bl3,
            br1, br2, br3));
  }
}
