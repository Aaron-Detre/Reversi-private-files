package cs3500.reversi.provider.strategy;

import java.util.List;

import cs3500.reversi.provider.model.CubicPosn;
import cs3500.reversi.provider.model.DiskState;
import cs3500.reversi.provider.model.ReadOnlyReversiModel;

/**
 * a strategy that prioritizes corners.
 */
public class PrioritizeCorners extends CaptureMostPieces {

  /**
   * filters the list of moves and removes if it is not a corner.
   *
   * @param coords the list of possible moves
   * @param player the player its working for
   */
  @Override
  public void filter(List<CubicPosn> coords, DiskState player, ReadOnlyReversiModel model) {
    int size = model.getBoardSize();
    CubicPosn corner = null;
    boolean foundCorner = false;
    for (CubicPosn coord : coords) {
      if (coord.equals(new CubicPosn(size, 0))
              || coord.equals(new CubicPosn(0, size))
              || coord.equals(new CubicPosn(-size, size))
              || coord.equals(new CubicPosn(-size, 0))
              || coord.equals(new CubicPosn(0, -size))
              || coord.equals(new CubicPosn(size, -size))) {
        corner = coord;
        foundCorner = true;
      }
    }
    if (foundCorner) {
      coords.clear();
      coords.add(corner);
    }
  }
}
