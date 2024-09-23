package cs3500.reversi.model;

import java.util.Objects;

/**
 * Represents a coordinate system of (row, column) rather than (x, y).
 */
public class RowColCoords {
  private final int row;
  private final int col;

  /**
   * Constructs a RowColCoords object.
   * @param row the row of the coordinate system
   * @param col the column of the coordinate system
   */
  public RowColCoords(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Gets the row of the coordinate.
   * @return the row
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Gets the column of the coordinate.
   * @return the column
   */
  public int getCol() {
    return this.col;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof RowColCoords)) {
      return false;
    }
    RowColCoords that = (RowColCoords) other;
    return this.row == that.row
            && this.col == that.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.row, this.col);
  }
}
