package model;

/**
 * An available space in a room.
 */
public class HallwayTile extends Tile {

  /**
   * Constructor for this HallwayTile
   * @param hallway that this tile is in
   */
  HallwayTile(String hallway) {
    super(hallway);
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitHallwayTile(this);
  }
}
