package cs3500.reversi.view;

import java.awt.geom.Path2D;

/**
 * A subclass of Path2D.Double that creates a single regular, pointy-top hexagon
 * path at a given location specifying the center and with a specified edge length.
 */
public class Hexagon extends Path2D.Double {
  /**
   * Constructs a Hexagon Path2D.Double object.
   * @param x the point on the horizontal axis of some panel that the hexagon's
   *          center will be placed at
   * @param y the point on the vertical axis of some panel that the hexagon's
   *          center will be placed at
   * @param edgeLength the length of an edge of the hexagon
   */
  public Hexagon(double x, double y, double edgeLength) {
    double xOffset = edgeLength * Math.cos(Math.toRadians(30));
    double yOffset = edgeLength * Math.sin(Math.toRadians(30));

    // bottom corner
    this.moveTo(x, y - edgeLength);
    // bottom right
    this.lineTo(x + xOffset, y - yOffset);
    // top right
    this.lineTo(x + xOffset, y + yOffset);
    // top corner
    this.lineTo(x, y + edgeLength);
    // top left
    this.lineTo(x - xOffset, y + yOffset);
    // bottom left
    this.lineTo(x - xOffset, y - yOffset);
    this.closePath();
  }
}
