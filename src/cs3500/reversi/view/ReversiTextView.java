package cs3500.reversi.view;

import java.util.List;

import cs3500.reversi.model.DiscColor;
import cs3500.reversi.model.ROReversiModel;
import cs3500.reversi.model.RowColCoords;

/**
 * Represents a simple text view for the game of Reversi.
 */
public class ReversiTextView implements IReversiTextView {
  private final ROReversiModel m;

  /**
   * Constructs a ReversiTextView object.
   * @param m the model being viewed
   */
  public ReversiTextView(ROReversiModel m) {
    this.m = m;
  }

  @Override
  public String toString() {
    String view = "";
    List<List<DiscColor>> grid = m.getGrid();
    int edgeLength = grid.get(0).size();
    int height = grid.size();

    for (int row = 0; row < height; row++) { // For each row of the text view
      view = this.addSpaces(view, Math.abs(row - (edgeLength - 1))); // Beginning spaces
      for (int col = 0; col < grid.get(row).size(); col++) {
        // The actual characters
        view = view.concat(colorToCharacter(m.getColorAt(new RowColCoords(row, col))));
        if (col != grid.get(row).size() - 1) {
          view = view.concat(" "); // Spaces in between characters
        }
      }
      view = this.addSpaces(view, Math.abs(row - (edgeLength - 1))); // Ending spaces
      if (row != height - 1) {
        view = view.concat("\n"); // Line break
      }
    }

    return view;
  }

  /**
   * Converts a color in the model's grid to either an X, O, or _.
   * @param c the color of a cell in the grid
   * @return the character as a String
   */
  private String colorToCharacter(DiscColor c) {
    switch (c) {
      case BLACK:
        return "X";
      case WHITE:
        return "O";
      case NONE:
        return "_";
      default:
        throw new IllegalArgumentException("Invalid color");
    }
  }

  /**
   * Adds multiple spaces to the view at a time.
   * @param view the text view being created
   * @param times how many spaces to be added
   * @return the view with that many spaces concatenated to it
   */
  private String addSpaces(String view, int times) {
    for (int i = 0; i < times; i++) {
      view = view.concat(" ");
    }
    return view;
  }
}
