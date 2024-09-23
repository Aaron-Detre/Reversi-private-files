package cs3500.reversi.model;

/**
 * Represents the color of a disc in the game Reversi.
 */
public enum DiscColor {
  BLACK,
  WHITE,
  NONE;

  /**
   * Finds the opposite color of the one given.
   * @param c the color black or white
   * @return the opposite of the color given (B -> W, W -> B)
   * @throws IllegalArgumentException if the color is not black or white
   */
  public static DiscColor oppositeColor(DiscColor c) throws IllegalArgumentException {
    if (c == DiscColor.BLACK) {
      return DiscColor.WHITE;
    } else if (c == DiscColor.WHITE) {
      return DiscColor.BLACK;
    } else {
      throw new IllegalArgumentException("There is no opposite of the color given");
    }
  }
}

