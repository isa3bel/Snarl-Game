package model;

/**
 * An available space in a room.
 */
public class Tile extends Space {

  Tile(String room) {
    this.group = room;
  }

  @Override
  public String acceptVisitor(SpaceVisitor visitor) {
    return visitor.visitTile(this);
  }
}
