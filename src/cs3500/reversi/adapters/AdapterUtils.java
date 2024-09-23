package cs3500.reversi.adapters;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.provider.model.CubicPosn;
import cs3500.reversi.provider.model.DiskState;

/**
 * A class containing methods to convert between the value classes our code and our
 * provider's code.
 */
public class AdapterUtils {
  /**
   * Converts our provider's CubicPosn to our RowColCoords.
   * @param posn some CubicPosn coordinate
   * @param delegateModel the model that coordinate exists in
   * @return the RowColCoords equivalent of the coordinate
   */
  protected RowColCoords cubicToRowCol(CubicPosn posn, ROReversiModel delegateModel) {
    RowColCoords centerOriginCoords;
    if (posn.getY() >= 0) {
      centerOriginCoords = new RowColCoords(posn.getY(), posn.getX());
    } else {
      centerOriginCoords = new RowColCoords(posn.getY(), posn.getX() + posn.getY());
    }
    return moveOrigin(centerOriginCoords, delegateModel, false);
  }

  /**
   * Converts our RowColCoords to our provider's CubicPosn.
   * @param coords some RowColCoords coordinate
   * @param delegateModel the model that coordinate exists in
   * @return the CubicPosn equivalent of the coordinate
   */
  protected CubicPosn rowColToCubic(RowColCoords coords, ROReversiModel delegateModel) {
    RowColCoords centerOrigin = moveOrigin(coords, delegateModel, true);
    if (centerOrigin.getRow() >= 0) {
      return new CubicPosn(centerOrigin.getCol(), centerOrigin.getRow());
    } else {
      return new CubicPosn(centerOrigin.getCol() - centerOrigin.getRow(), centerOrigin.getRow());
    }
  }

  /**
   * Finds what a RowColCoords coordinates would be if the origin of the coordinate system
   * it exists in was shifted either to the center of the board or back to the top left.
   * @param coords some RowColCoords coordinate
   * @param delegateModel the model that coordinate exists in
   * @param toCenter whether the origin is shifting to the center or back to the top left
   * @return the adjusted RowColCoords coordinate
   */
  private RowColCoords moveOrigin(RowColCoords coords, ROReversiModel delegateModel,
                                  boolean toCenter) {
    int offset = (delegateModel.getEdgeLength() - 1) * (toCenter ? -1 : 1);
    return new RowColCoords(coords.getRow() + offset, coords.getCol() + offset);
  }

  /**
   * Converts our DiscColor to our provider's DiskState.
   * @param color some disc color
   * @return the DiskState equivalent of that color
   */
  protected DiskState colorToState(DiscColor color) {
    switch (color) {
      case WHITE:
        return DiskState.WHITE;
      case BLACK:
        return DiskState.BLACK;
      case NONE:
        return DiskState.EMPTY;
      default:
        throw new IllegalArgumentException("Invalid DiscColor");
    }
  }

  /**
   * Converts our provider's DiskState to our DiscColor.
   * @param state some disk state
   * @return the DiscColor equivalent of that state
   */
  protected DiscColor stateToColor(DiskState state) {
    switch (state) {
      case WHITE:
        return DiscColor.WHITE;
      case BLACK:
        return DiscColor.BLACK;
      case EMPTY:
        return DiscColor.NONE;
      default:
        throw new IllegalArgumentException("Invalid DiskState");
    }
  }
}
