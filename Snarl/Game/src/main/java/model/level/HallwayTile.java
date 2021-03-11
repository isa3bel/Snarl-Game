package model.level;

import model.level.SpaceVisitor;
import model.level.Tile;

/**
 * An available space in a room.
 */
public class HallwayTile extends Tile {

  /**
   * Constructor for this HallwayTile
   * @param hallway that this tile is in
   */
  public HallwayTile(String hallway) {
    super(hallway);
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitHallwayTile(this);
  }
}
