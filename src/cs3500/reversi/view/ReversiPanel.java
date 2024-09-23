package cs3500.reversi.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import cs3500.reversi.model.RowColCoords;
import cs3500.reversi.model.DiscColor;

/**
 * The main panel of the game of Reversi containing the view of the board.
 */
public class ReversiPanel extends JPanel implements IReversiPanel {
  // The regular hexagonal grid at some state of the game.
  private final List<List<DiscColor>> grid;
  // The edge length of the grid.
  // Class Invariant: The grid length is equal to the length of the first row in the grid.
  private final int gridEdgeLength;
  // All the Hexagon cells that make up the grid.
  // Not a class invariant because it isn't ensured by the constructor, but once the board
  // has been drawn, cells should have the same number of Path2D.Doubles (Hexagons) as
  // the grid has DiscColors.
  private final List<Path2D.Double> cells;
  // The Hexagon cell in the grid that has been selected.
  // It is empty if no cell has been selected or a selected cell has been deselected.
  private Optional<Path2D.Double> selected;
  // The width of one hexagonal cell in the grid.
  // It is dependent on the size of the panel.
  private double hexagonWidth;
  // The edge length (also equal to half the height) of one hexagonal cell in the grid.
  // It is dependent on the size of the panel.
  private double hexagonEdgeLength;
  // The radius of the players' discs.
  // It is dependent on the size of the panel.
  private double discRadius;
  // The panel's mouse listener, set with the appropriate actions
  private MouseInputAdapter mouseListener;
  // The panel's key listener, set with the appropriate actions
  private KeyListener keyListener;

  /**
   * Constructs a ReversiPanel with a width of 500 and a mouse listener.
   * @param grid the grid of the game of Reversi that is being viewed
   */
  public ReversiPanel(List<List<DiscColor>> grid) {
    this.setFocusable(true);
    this.grid = grid;
    this.gridEdgeLength = grid.get(0).size();
    this.cells = new ArrayList<>();
    this.selected = Optional.empty();
    this.setPreferredSize(new Dimension(500,
            (int) ((3 * this.gridEdgeLength - 1)
                    * (500 / ((this.gridEdgeLength * 2) - 1) / Math.sqrt(3)))));
  }

  @Override
  protected void paintComponent(Graphics g) {
    this.hexagonWidth = this.getBounds().getWidth() / ((this.gridEdgeLength * 2) - 1);
    this.hexagonEdgeLength = hexagonWidth / Math.sqrt(3);
    this.discRadius = this.getBounds().getWidth() / (3 * ((this.gridEdgeLength * 2) - 1));

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    Rectangle bounds = this.getBounds();
    
    // moving the origin to the bottom left
    g2d.translate(0, bounds.height);
    // flipping the y-axis of the panel
    g2d.scale(1, -1);
    
    drawBoard(g2d);
    drawPieces(g2d);
  }

  /**
   * Draws all the pointy-top hexagonal cells of the flat-top hexagonal grid.
   * @param g2d the Graphics2D object that everything is being drawn on
   */
  private void drawBoard(Graphics2D g2d) {
    Color blue = new Color(0, 255, 254);
    Color gray = new Color(192, 192, 192);
    for (int row = 0; row < this.grid.size(); row++) {
      for (int col = 0; col < this.grid.get(row).size(); col++) {
        RowColCoords cell = new RowColCoords(row, col);
        // If this cell is the selected cell, color it in blue, otherwise color it in gray
        Color c = cellIsSelected(cell) ? blue : gray;
        // Draw the hexagon with the appropriate color at the physical coordinates on the panel
        drawHexagon(g2d, c, this.logicalToPhysicalX(cell), this.logicalToPhysicalY(cell));
      }
    }
  }

  /**
   * Determines whether the given cell is the cell selected by the user.
   * @param cell coordinates specifying a cell in the grid
   * @return true iff the cell is the currently selected cell
   */
  private boolean cellIsSelected(RowColCoords cell) {
    if (this.selected.isPresent()) {
      // Checking whether the selected cell contains the point at the center of the given cell
      if (this.selected.get().contains(
              new Point((int) this.logicalToPhysicalX(cell),
                      (int) this.logicalToPhysicalY(cell)))) {
        // Output the logical coordinates
        System.out.printf("Row: %d, Column: %d\n", cell.getRow(), cell.getCol());
        return true;
      }
    }
    return false;
  }

  /**
   * Draws a single pointy-top hexagon in the grid.
   * @param g2d the Graphics2D object that everything is being drawn on
   * @param c the color of the hexagon
   * @param x the point on the horizontal axis of the panel that the hexagon's
   *          center will be placed at
   * @param y the point on the vertical axis of the panel that the hexagon's
   *          center will be placed at
   */
  private void drawHexagon(Graphics2D g2d, Color c, double x, double y) {
    Path2D.Double hexagon = new Hexagon(x, y, this.hexagonEdgeLength);
    this.cells.add(hexagon);

    Color old = g2d.getColor();
    g2d.setColor(c);
    g2d.fill(hexagon);
    g2d.setColor(Color.BLACK);
    g2d.draw(hexagon);
    g2d.setColor(old);
  }

  /**
   * Draws all the black or white discs currently placed on the board.
   * @param g2d the Graphics2D object that everything is being drawn on
   */
  private void drawPieces(Graphics2D g2d) {
    for (int row = 0; row < this.grid.size(); row++) {
      for (int col = 0; col < this.grid.get(row).size(); col++) {
        DiscColor c = this.grid.get(row).get(col);
        // If there is no disc, don't draw anything
        if (c != DiscColor.NONE) {
          // If there is a disc, draw a circle at the appropriate physical coordinates and
          // with the appropriate color
          RowColCoords cell = new RowColCoords(row, col);
          Color color = (c == DiscColor.WHITE) ? Color.WHITE : Color.BLACK;
          drawCircle(logicalToPhysicalX(cell),
                  logicalToPhysicalY(cell),
                  this.discRadius, g2d, color);
        }
      }
    }
  }

  /**
   * Draws a single circle representing a Reversi disc.
   * @param centerX the point on the horizontal axis of the panel that the circle's
   *                center will be placed at
   * @param centerY the point on the vertical axis of the panel that the circle's
   *                center will be placed at
   * @param radius the radius of the circle
   * @param g2d the Graphics2D object that everything is being drawn on
   * @param c the color of the circle
   */
  private void drawCircle(double centerX, double centerY,
                          double radius, Graphics2D g2d, Color c) {
    Color oldColor = g2d.getColor();
    g2d.setColor(c);
    Ellipse2D.Double circle = new Ellipse2D.Double(
            centerX - radius,
            centerY - radius,
            2 * radius,
            2 * radius);
    g2d.fill(circle);
    g2d.setColor(oldColor);
  }

  /**
   * Converts logical row/column coordinates of a cell in the grid into a double
   * representing the horizontal axis position of the center of that cell on the panel.
   * @param cell RowColCoords specifying a cell in the grid
   * @return the horizontal center position of that cell on the panel
   */
  private double logicalToPhysicalX(RowColCoords cell) {
    return this.getBounds().getX()
            + getHexagonXOffset(cell.getRow())
            + (cell.getCol() * this.hexagonWidth);
  }

  /**
   * Calculates the starting horizontal position of the first cell in each row.
   * Without this, the grid would look like a pentagon since the first cell of each
   * row would be in a straight vertical line.
   * @param row the row of the cell being placed
   * @return how far to the right each cell in that row should be offset by
   */
  private double getHexagonXOffset(double row) {
    return (((double) this.gridEdgeLength / 2)
            - (((this.gridEdgeLength - 1) - Math.abs((this.gridEdgeLength - 1) - row)) * .5))
            * this.hexagonWidth;
  }

  /**
   * Converts logical row/column coordinates of a cell in the grid into a double
   * representing the vertical axis position of the center of that cell on the panel.
   * @param cell RowColCoords specifying a cell in the grid
   * @return the vertical center position of that cell on the panel
   */
  private double logicalToPhysicalY(RowColCoords cell) {
    return this.getBounds().getHeight()
            - this.hexagonEdgeLength
            - (this.hexagonEdgeLength * 1.5 * cell.getRow());
  }

  @Override
  public void setActionListeners(PlayerActions viewActions) {
    // Remove the previous mouse listener
    this.removeMouseListener(this.mouseListener);
    // Set the new mouse listener
    this.mouseListener = new MouseInputAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        // Get the exact point that the user released their mouse at
        Point p = new Point(e.getX(), Math.abs(e.getY() - ReversiPanel.this.getHeight()));
        Optional<RowColCoords> mouseReleasedLogical = ReversiPanel.this.pointToLogical(p);

        Optional<RowColCoords> selectedLogical;
        if (ReversiPanel.this.selected.isPresent()) {
          selectedLogical = pathToLogical(ReversiPanel.this.selected.get());
        } else {
          selectedLogical = Optional.empty();
        }

        if (selectedLogical.isPresent()) {
          if (mouseReleasedLogical.isEmpty()) {
            // If mouse clicked off the grid, deselect the selected cell
            viewActions.deselectCell();
          } else {
            if (mouseReleasedLogical.get().equals(selectedLogical.get())) {
              // If mouse clicked on the selected cell, deselect it
              viewActions.deselectCell();
            } else {
              // If mouse clicked on grid but not on selected cell, select the clicked cell
              viewActions.selectCell(mouseReleasedLogical.get());
            }
          }
        } else {
          // If mouse clicked on grid and there is no selected cell, select the clicked cell
          mouseReleasedLogical.ifPresent(viewActions::selectCell);
        }

        ReversiPanel.this.repaint();
      }
    };
    // Add the new mouse listener to the panel
    this.addMouseListener(this.mouseListener);


    // Remove the previous key listener
    this.removeKeyListener(this.keyListener);
    // Set the new key listener
    this.keyListener = new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        if (key == '\n') { // Press Enter to place disc
          if (ReversiPanel.this.selected.isPresent()) {
            Optional<RowColCoords> coords = pathToLogical(ReversiPanel.this.selected.get());
            coords.ifPresent(viewActions::placeDiscInSelectedCell);
          }
        } else if (key == ' ') { // Press Spacebar to pass
          viewActions.passTurn();
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // nothing
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // nothing
      }
    };
    // Add the new key listener to the panel
    this.addKeyListener(this.keyListener);
  }

  @Override
  public void setSelectedCell(RowColCoords cell) throws IllegalArgumentException {
    this.selected = Optional.of(this.logicalToCell(cell));
  }

  /**
   * Finds the physical cell in the grid that corresponds to given logical
   * row/column coordinates.
   * @param coords coordinates specifying some cell in the grid
   * @return the physical cell that corresponds to the coordinates
   * @throws IllegalArgumentException if the coordinates don't match any cell in the grid
   */
  private Path2D.Double logicalToCell(RowColCoords coords) throws IllegalArgumentException {
    Point p = new Point(
            (int) logicalToPhysicalX(coords),
            (int) logicalToPhysicalY(coords));
    for (Path2D.Double cell : this.cells) {
      if (cell.contains(p)) {
        return cell;
      }
    }
    throw new IllegalArgumentException("Coords don't match any cell in the grid");
  }

  /**
   * Finds the logical row/column coordinate that correspond to some physical point
   * on the panel.
   * @param point some point on the panel
   * @return the logical coordinates or Optional.empty() if the point was off the grid
   */
  private Optional<RowColCoords> pointToLogical(Point point) {
    int cellCounter = 0;
    for (int row = 0; row < this.grid.size(); row++) {
      for (int col = 0; col < this.grid.get(row).size(); col++) {
        if (this.cells.get(cellCounter).contains(point)) {
          return Optional.of(new RowColCoords(row, col));
        }
        cellCounter++;
      }
    }
    return Optional.empty();
  }

  /**
   * Finds the logical row/column coordinate that correspond to the center of some
   * physical Path2D.Double on the panel.
   * @param path some path (generally a hexagonal cell) on the panel
   * @return the logical coordinates or Optional.empty() if the center of the path
   *         was off the grid
   */
  private Optional<RowColCoords> pathToLogical(Path2D.Double path) {
    Rectangle bounds = path.getBounds();
    Point center = new Point(
            (int) (bounds.getX() + .5 * bounds.getWidth()),
            (int) (bounds.getY() + .5 * bounds.getHeight()));
    return pointToLogical(center);
  }

  @Override
  public void deselectSelectedCell() {
    this.selected = Optional.empty();
  }

  /**
   * Returns the panel's mouse listener so that the view can be tested.
   * @return the panel's mouse listener
   */
  protected MouseInputAdapter getMouseListener() {
    return this.mouseListener;
  }

  /**
   * Returns the panel's key listener so that the view can be tested.
   * @return the panel's key listener
   */
  protected KeyListener getKeyListener() {
    return this.keyListener;
  }
}