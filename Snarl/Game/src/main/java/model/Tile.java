package model;

/**
 * An available space in a room.
 */
public class Tile extends Space {

  /**
   * Constructor for this Tile
   * @param room the room that this tile will be in
   */
  Tile(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitTile(this);
  }
}
